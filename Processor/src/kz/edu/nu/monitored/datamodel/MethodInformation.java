package kz.edu.nu.monitored.datamodel;

import kz.edu.nu.monitored.annotations.CalledBy;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.*;

/**
 * This class serves as data model in FreeMarker templates.
 * At the same time it performs some validations at instantiating.
 */
public class MethodInformation {
    private static final Set <Modifier> ALL_ACCESS_MODIFIERS =
        Collections.unmodifiableSet(
            new HashSet<>(
                Arrays.asList(Modifier.PRIVATE, Modifier.PROTECTED, Modifier.PUBLIC)));

    private ExecutableElement methodElement;
    private Set<Modifier> modifiers;

    private MethodInformation(ExecutableElement methodElement) {
        this.methodElement = methodElement;
        this.modifiers = methodElement.getModifiers();
    }

    /**
     * Performs validation of passed rawElement and creates new instance of
     * {@link MethodInformation}
     */
    public static MethodInformation from(Element rawElement) throws Exception {
        CalledBy calledByAnnotation = rawElement.getAnnotation(CalledBy.class);
        if (calledByAnnotation == null) {
            throw new Exception("Cannot create MethodInformation from element " +
                    "without @CalledBy annotations");
        }

        if (rawElement.getKind() != ElementKind.METHOD) {
            throw new Exception("@CalledBy annotation can applied only to methods");
        }

        // Safe cast - we've made sure, that rawElement is method
        ExecutableElement methodElement = (ExecutableElement) rawElement;

        String[] callers = calledByAnnotation.value();
        if (callers.length == 0) {
            throw new Exception("No callers are specified in @CalledBy annotation");
        }

        /*
         * We cannot use private and final modifiers since they prevent from having
         * subclass with the same method (which we're exploiting here).
         *
         * Having @CalledBy on abstract method makes no sense, since the whole system
         * breaks when we try to apply inheritance.
         */
        Set<Modifier> modifiers = methodElement.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)
            || modifiers.contains(Modifier.PRIVATE)
            || modifiers.contains(Modifier.FINAL)) {

            throw new Exception("@CalledBy annotation cannot be applied to " +
                    "abstract, private or final methods");
        }

        return new MethodInformation(methodElement);
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

    public List<String> getCallers() {
        CalledBy calledByAnnotation = methodElement.getAnnotation(CalledBy.class);
        return Arrays.asList(calledByAnnotation.value());
    }
}
