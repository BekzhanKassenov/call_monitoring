package kz.edu.nu.monitored.datamodel.monitoring_info;

import kz.edu.nu.monitored.annotations.CallStack;

import javax.lang.model.element.ExecutableElement;
import java.util.Arrays;
import java.util.List;

public class CallerChain extends MonitoringInfo {
    private List<String> expectedCallStack;

    CallerChain(ExecutableElement methodElement) {
        super(Type.CALLER_CHAIN);
        expectedCallStack = Arrays.asList(methodElement.getAnnotation(CallStack.class).value());
    }

    public List<String> getExpectedCallStack() {
        return expectedCallStack;
    }
}
