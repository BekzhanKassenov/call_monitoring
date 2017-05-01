<#import "Common.ftl" as common>

${constructor.accessModifier!""}   <#-- public|private|protected -->
${constructor.className}_Monitored <#-- "return type" of the constructor -->
<@common.parameter_list constructor.parameterTypes constructor.parameterNames />
<@common.throws_clause constructor.thrownExceptions /> {
    super(${constructor.parameterNames?join(", ")});
}