package ${classPackage};

import kz.edu.nu.monitored.Monitoring;

import java.util.Arrays;
import java.util.List;

class ${className}_Monitored extends ${className} {
    <#list monitoredMethods as method>
        <#if method.monitoringInfo.monitoringType != "NO_MONITORING">
            <#include "MethodHeader.ftl"> <#-- Setup header of the method -->
            {
                <#switch method.monitoringInfo.monitoringType>
                    <#case "CALLER_LIST">
                        <#include "monitoring_body/CallerList.ftl">
                        <#break>
                    <#case "CALLER_CHAIN">
                        <#include "monitoring_body/CallerChain.ftl">
                        <#break>
                </#switch>

                <#if method.returnType != "void">return </#if>
                super.${method.name}(${method.parameterNames?join(", ")});
            }
        </#if>
    </#list>
} <#-- End of class -->
