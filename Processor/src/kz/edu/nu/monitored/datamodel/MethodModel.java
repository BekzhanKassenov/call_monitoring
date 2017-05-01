package kz.edu.nu.monitored.datamodel;

import kz.edu.nu.monitored.datamodel.monitoring_info.MonitoringInfo;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.*;

/**
 * This class serves as data model in FreeMarker templates.
 * At the same time it performs some validations at instantiating.
 */
public class MethodModel {
    private static final Set <Modifier> ALL_ACCESS_MODIFIERS =
        Collections.unmodifiableSet(
            new HashSet<>(
                Arrays.asList(Modifier.PRIVATE, Modifier.PROTECTED, Modifier.PUBLIC)));

    private ExecutableElement methodElement;
    private Set<Modifier> modifiers;
    private MonitoringInfo monitoringInfo;

    private MethodModel(
        ExecutableElement methodElement, MonitoringInfo monitoringInfo) {

        this.methodElement = methodElement;
        this.modifiers = methodElement.getModifiers();
        this.monitoringInfo = monitoringInfo;
    }

    /**
     * Performs validation of passed rawElement and creates new instance of
     * {@link MethodModel}
     */
    public static MethodModel from(Element rawElement) throws Exception {
        if (rawElement.getKind() != ElementKind.METHOD) {
            throw new Exception("Monitoring annotations can be applied only to methods");
        }

        // Safe cast - we've made sure, that rawElement is method
        ExecutableElement methodElement = (ExecutableElement) rawElement;

        // This might throw exceptions if there's methodElement's annotations
        MonitoringInfo monitoringInfo = MonitoringInfo.from(methodElement);


        // We cannot use private and final modifiers since they prevent from having
        // subclass with the same method (which we're exploiting here).
        //
        // Having @CalledBy on abstract method makes no sense, since the whole system
        // breaks when we try to apply inheritance.
        Set<Modifier> modifiers = methodElement.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)
            || modifiers.contains(Modifier.PRIVATE)
            || modifiers.contains(Modifier.FINAL)) {

            throw new Exception("Monitoring annotations cannot be applied to " +
                    "abstract, private or final methods");
        }

        return new MethodModel(methodElement, monitoringInfo);
    }

    public String getAccessModifier() {
        Set <Modifier> accessModifiers = new HashSet<>(modifiers);
        accessModifiers.retainAll(ALL_ACCESS_MODIFIERS);

        // We assume that class compiles OK, i.e. there're no several access
        // modifiers. So size of accessModifiers is either 0 or 1
        if (accessModifiers.isEmpty()) {
            return null;
        }
        return accessModifiers.toArray()[0].toString();
    }

    public String getStaticModifier() {
        if (!modifiers.contains(Modifier.STATIC)) {
            return null;
        }
        return Modifier.STATIC.toString();
    }

    public String getReturnType() {
        return methodElement.getReturnType().toString();
    }

    public String getName() {
        return methodElement.getSimpleName().toString();
    }

    public List<String> getParameterTypes() {
        List<String> result = new ArrayList<>();
        for (VariableElement parameter : methodElement.getParameters()) {
            result.add(parameter.asType().toString());
        }

        return result;
    }

    public List<String> getParameterNames() {
        List<String> result = new ArrayList<>();
        for (VariableElement parameter : methodElement.getParameters()) {
            result.add(parameter.getSimpleName().toString());
        }

        return result;
    }

    public List<String> getThrownExceptions() {
        List <String> result = new ArrayList<>();
        for (TypeMirror exceptionType : methodElement.getThrownTypes()) {
            result.add(exceptionType.toString());
        }

        return result;
    }

    public MonitoringInfo getMonitoringInfo() {
        return monitoringInfo;
    }
}
