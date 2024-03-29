package ${classPackage};

import kz.edu.nu.monitored.Monitoring;

import java.util.Arrays;
import java.util.List;

class ${className}_Monitored extends ${className} {
    <#list constructors as constructor>
        <#include "ConstructorTemplate.ftl">
    </#list>

    <#list monitoredMethods as method>
        <#if method.monitoringInfo.monitoringType != "NO_MONITORING">
            <#include "MethodHeader.ftl"> <#-- Setup header of the method -->
            {
                <#include method.monitoringInfo.templateFile>

                <#if method.returnType != "void">return </#if>
                super.${method.name}(${method.parameterNames?join(", ")});
            }
        </#if>
    </#list>
} <#-- End of class -->
