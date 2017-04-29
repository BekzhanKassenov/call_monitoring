package kz.edu.nu.monitored;

import java.util.Arrays;

public abstract class Monitoring {
    /**
     * Verifies that all top elements of actualCallStack (except for the
     * last one) match names provided in expectedCallStack.
     *
     * TODO: make all of the verification methods in this class return
     * some useful object with message instead of boolean.
     */
    public static boolean verifyCallStack(
        String[] expectedCallStack, StackTraceElement[] actualCallStack) {

        // Actual call stack should contain at least all expected call stack
        // items, plus examined method itself.
        if (expectedCallStack.length + 1 > actualCallStack.length) {
            return false;
        }

        for (int i = 0; i < expectedCallStack.length; i++) {
            if (!expectedCallStack[i].equals(getMethodFqn(actualCallStack[i + 1]))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks that caller of the method is the one specified in
     * allowedCallers. Delegates to verifyCallStack.
     */
    public static boolean verifyCallerList(
        String[] allowedCallers, StackTraceElement[] actualCallStack) {
        for (int i = 0; i < allowedCallers.length; i++) {
            // We can reuse verifyCallStack method with only one argument
            if (verifyCallStack(
                Arrays.copyOfRange(allowedCallers, i, i + 1), actualCallStack)) {

                return true;
            }
        }

        return false;
    }

    /**
     * Helper method to generate FQN of the method from {@link StackTraceElement}
     */
    private static String getMethodFqn(StackTraceElement stackTraceElement) {
        return stackTraceElement.getClassName() + "#" + stackTraceElement.getMethodName();
    }

    private Monitoring() {}
}
