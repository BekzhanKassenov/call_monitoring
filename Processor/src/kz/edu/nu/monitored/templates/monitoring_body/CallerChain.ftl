String[] callerChain = {
    <#list method.monitoringInfo.expectedCallStack as stackItem> "${stackItem}" <#sep>, </#sep> </#list>
};
StackTraceElement[] trace = (new Throwable()).getStackTrace();
if (!Monitoring.verifyCallStack(callerChain, trace)) {
    throw new Error("Call stack specification is not satisfied for ${method.name}");
}
