<#import "Common.ftl" as common>

${method.accessModifier!""} <#-- public|private|protected -->
${method.staticModifier!""} <#-- static -->
${method.returnType}        <#-- returnType -->
${method.name}              <#-- name -->
<@common.parameter_list method.parameterTypes method.parameterNames />
<@common.throws_clause method.thrownExceptions />