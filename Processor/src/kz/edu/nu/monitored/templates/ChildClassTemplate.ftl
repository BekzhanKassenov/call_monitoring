package ${classPackage};

import kz.edu.nu.monitored.Monitoring;

import java.util.Arrays;
import java.util.List;

class ${className}_Monitored extends ${className} {
    <#list monitoredMethods as method>
        ${method.accessModifier!""}
        ${method.staticModifier!""}
        ${method.returnType}
        ${method.name}(
            <#list method.parameterTypes as parameterType>
                ${parameterType} ${method.parameterNames[parameterType?index]} <#sep>, </#sep>
            </#list>
        )
        <#if method.thrownExceptions?size != 0> throws ${method.thrownExceptions?join(", ")} </#if>
        {
            List<String> callerList = Arrays.asList(
                <#list method.callers as caller> "${caller}" <#sep>, </#sep> </#list>
            );
            StackTraceElement[] trace = (new Throwable()).getStackTrace();
            if (trace.length < 2) {
                throw new Error("Too short stack trace for ${classFqn}#${method.name}");
            }

            String callerName = trace[1].getClassName() + "#" + trace[1].getMethodName();
            if (!callerList.contains(callerName)) {
                throw new Error("Caller " + callerName + " is not allowed for quick");
            }

            <#if method.returnType != "void">return </#if>
            super.${method.name}(${method.parameterNames?join(", ")});
        }
    </#list>
}
