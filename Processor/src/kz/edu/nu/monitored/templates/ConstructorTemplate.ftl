${constructor.accessModifier!""} <#-- public|private|protected -->
${constructor.className} (       <#-- "return type" of the constructor -->
    <#-- List all of the parameters, separated with comma -->
    <#list constructor.parameterTypes as parameterType>
        ${parameterType} ${constructor.parameterNames[parameterType?index]} <#sep>, </#sep>
    </#list>
)
<#-- Add "throws" keyword and list exception types separated with comma -->
<#if method.thrownExceptions?size != 0> throws ${method.thrownExceptions?join(", ")} </#if>