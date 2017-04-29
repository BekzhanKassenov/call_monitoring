package kz.edu.nu.monitored;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kz.edu.nu.monitored.annotations.Monitored;
import kz.edu.nu.monitored.datamodel.ClassInformation;
import kz.edu.nu.monitored.datamodel.MethodInformation;
import kz.edu.nu.monitored.datamodel.monitoring_info.MonitoringInfo;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;

@SupportedAnnotationTypes("kz.edu.nu.monitored.annotations.Monitored")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MonitoredProcessor extends AbstractProcessor {
    private static final String MONITORED_SUFFIX = "_Monitored";

    private Configuration freemarkerConfiguration;

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();

        // It'd be nice to have freemarkerConfiguration as static member in this class
        // and thus make it singleton, as it was recommended in the official
        // documentation but in that case we won't be able to use getClass() in the
        // static initialization block.
        // So, we'll have instance of freemarkerConfiguration per MonitoredProcessor.
        freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_26);
        freemarkerConfiguration.setClassForTemplateLoading(getClass(), "templates");

        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfiguration.setLogTemplateExceptions(false);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        List <ClassInformation> classList = new ArrayList<>();
        for (Element monitoredElement : roundEnvironment.getElementsAnnotatedWith(Monitored.class)) {
            if (monitoredElement.getKind() != ElementKind.CLASS) {
                // Printing message with DiagnosticKind.ERROR will abort compilation
                error("An error occurred while processing element %s: " +
                        "@Monitored annotation can be applied only to classes",
                        monitoredElement.getSimpleName());
            }

            note("Processing class %s", monitoredElement.getSimpleName());

            List <MethodInformation> monitoredMethods = new ArrayList<>();

            for (Element enclosedElement : ((TypeElement) monitoredElement).getEnclosedElements()) {
                if (!hasMonitoringAnnotation(enclosedElement)) {
                    continue;
                }

                try {
                    MethodInformation temp = MethodInformation.from(enclosedElement);
                    monitoredMethods.add(temp);
                } catch (Exception e) {
                    // Printing message with DiagnosticKind.ERROR will abort compilation
                    error("An error occurred while processing class %s: %s",
                            monitoredElement.getSimpleName(), e.getMessage());
                }
            }

            try {
                classList.add(ClassInformation.from(monitoredElement, monitoredMethods));
            } catch (Exception e) {
                // Printing message with DiagnosticKind.ERROR will abort compilation
                error("An error occurred while processing class %s: %s",
                        monitoredElement.getSimpleName(), e.getMessage());
            }
        }

        generateSubclasses(classList);

        return true;
    }

    private static boolean hasMonitoringAnnotation(Element element) {
        for (Class <? extends Annotation> annotationClass : MonitoringInfo.MONITORING_ANNOTATIONS) {
            if (element.getAnnotation(annotationClass) != null) {
                return true;
            }
        }

        return false;
    }

    private void generateSubclasses(List <ClassInformation> classList) {
        String templateName = "ChildClassTemplate.ftl";
        try {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            for (ClassInformation classInformation : classList) {
                JavaFileObject fileObject =
                        filer.createSourceFile(classInformation.getClassFqn() + MONITORED_SUFFIX);

                try (Writer writer = fileObject.openWriter()){
                    template.process(classInformation, writer);
                } catch (IOException | TemplateException e) {
                    error("An error occurred while generating subclass for %s: %s",
                            classInformation.getClassName(), e.getMessage());
                }
            }
        } catch (IOException e) {
            error("An error occurred while loading template %s: %s", templateName, e.getMessage());
        }
    }

    private void note(String format, Object... args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format(format, args));
    }

    private void error(String format, Object... args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(format, args));
    }
}
