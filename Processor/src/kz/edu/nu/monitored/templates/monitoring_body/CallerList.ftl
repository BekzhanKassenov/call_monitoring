String[] callerList = {
    <#list method.monitoringInfo.callers as caller> "${caller}" <#sep>, </#sep> </#list>
};
StackTraceElement[] trace = (new Throwable()).getStackTrace();
if (!Monitoring.verifyCallerList(callerList, trace)) {
    throw new Error("Caller " + trace[1].getMethodName() + " is not allowed for ${method.name}");
}
