<#-- List all of the method's/constructor's parameters, separated with comma -->
<#macro parameter_list parameterTypes parameterNames>
    (
        <#list parameterTypes as parameterType>
            ${parameterType} ${parameterNames[parameterType?index]} <#sep>, </#sep>
        </#list>
    )
</#macro>

<#-- Add "throws" keyword and list exception types separated with comma -->
<#macro throws_clause thrownExceptions>
    <#if thrownExceptions?size != 0> throws ${thrownExceptions?join(", ")} </#if>
</#macro>