${method.accessModifier!""} <#-- public|private|protected -->
${method.staticModifier!""} <#-- static -->
${method.returnType}        <#-- returnType -->
${method.name}(             <#-- name -->
    <#-- List all of the parameters, separated with comma -->
    <#list method.parameterTypes as parameterType>
        ${parameterType} ${method.parameterNames[parameterType?index]} <#sep>, </#sep>
    </#list>
)
<#-- Add "throws" keyword and list exception types separated with comma -->
<#if method.thrownExceptions?size != 0> throws ${method.thrownExceptions?join(", ")} </#if>