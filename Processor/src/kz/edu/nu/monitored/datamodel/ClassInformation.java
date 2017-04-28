package kz.edu.nu.monitored.datamodel;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.List;

/**
 * This class serves as data model in FreeMarker templates.
 * At the same time it performs some validations at instantiating.
 */
public class ClassInformation {
    private TypeElement classElement;
    private List<MethodInformation> monitoredMethods;

    private ClassInformation(TypeElement classElement, List<MethodInformation> monitoredMethods) {
        this.classElement = classElement;
        this.monitoredMethods = monitoredMethods;
    }

    public String getClassPackage() {
        String fqn = getClassFqn();
        String[] splitFqn = fqn.split("\\.");
        String[] pathElements = Arrays.copyOf(splitFqn, splitFqn.length - 1);
        return String.join(".", pathElements);
    }

    public String getClassName() {
        return classElement.getSimpleName().toString();
    }

    public List<MethodInformation> getMonitoredMethods() {
        return monitoredMethods;
    }

    public String getClassFqn() {
        return classElement.getQualifiedName().toString();
    }

    public static ClassInformation from(Element rawElement, List<MethodInformation> monitoredMethods)
        throws Exception {

        if (rawElement.getKind() != ElementKind.CLASS) {
            throw new Exception("@Monitored annotation can applied only to class elements");
        }

        if (rawElement.getModifiers().contains(Modifier.FINAL)) {
            throw new Exception("@Monitored annotation cannot be applied to final classes");
        }

        return new ClassInformation((TypeElement) rawElement, monitoredMethods);
    }
}
