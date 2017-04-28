String[] callerList = {
    <#list method.callers as caller> "${caller}" <#sep>, </#sep> </#list>
};
StackTraceElement[] trace = (new Throwable()).getStackTrace();
if (!Monitoring.verifyCallerList(callerList, trace)) {
    throw new Error("Caller " + trace[1].getMethodName() + " is not allowed for ${method.name}");
}

<#if method.returnType != "void">return </#if>
super.${method.name}(${method.parameterNames?join(", ")});