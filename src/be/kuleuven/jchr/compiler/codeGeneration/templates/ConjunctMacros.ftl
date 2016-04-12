<#macro conjunct o>
	<#switch o.type>
		<#case "infixConstraint">
			<@infixInvocation o/>
		<#break>
	
		<#case "udConstraint">
			<@udConstraintInvocation o/>
		<#break>
		
		<#case "methodInvocation">
			<@methodInvocation o/>
		<#break>
		
		<#case "declaration">
			<@declaration o/>
		<#break>
		
		<#case "failure">
			throw new runtime.FailureException()
		<#break>
		
		<#default>
			<#stop "Unsupported constraint-type: ${o.type}">
	</#switch>
</#macro>

<#macro udConstraintInvocation constraint>
	new <@global.constraintType constraint/>(
		<@argumentList constraint.arguments/><@comma constraint.arguments/>
		${global.handlerName}).activate()
</#macro>

<#-- 
	Soms is het nodig om enkel een komma te zetten als 
	de lijst ervoor niet leeg is.
-->
<#macro comma list>
	<#if list?size != 0>,</#if>
</#macro>

<#macro constructorInvocation info>
	new ${info.type}(<@argumentList info.arguments/>)
</#macro>

<#macro methodInvocation info>
	<#local size = info.arguments?size/>
	<@argument info.arguments[0]/>.${info.methodName}(
	<#if size gt 1>	
		<@argumentList info.arguments[1..size-1]/>
	</#if>
	)
</#macro>

<#macro declaration info>
	${info.declaringType} <@argument info.arguments[0]/> = <@argument info.arguments[1]/>
</#macro>

<#macro infixInvocation info>
	<@argument info.arguments[0]/> ${info.identifier} <@argument info.arguments[1]/>
</#macro>

<#macro fieldAccess info>
	<@argument info.arguments[0]/>.${info.fieldName}
</#macro>

<#macro argumentList arguments>
	<#list arguments as arg>
		<@argument arg/><#if arg_has_next>, </#if>
	</#list>
</#macro>

<#macro argument arg>
<#switch arg.type>
	<#case "byte">
	<#case "short">
	<#case "int">
	<#case "long">
	<#case "float">
	<#case "double">
		${num2str(arg.argumentInfo.value)}<#t>
	<#break>

	<#case "boolean">
		${arg.argumentInfo.value?string}<#t>
	<#break>

	<#case "string">
		"${arg.argumentInfo.value}"<#t>
	<#break>

	<#case "char">
		'${arg.argumentInfo.value}'<#t>
	<#break>

	<#case "null">
		null<#t>
	<#break>

	<#case "solver">
		${solverPrefix?if_exists}<#case "variable">${arg.argumentInfo.identifier}<#t>
	<#break>
	
	<#case "className">
		${arg.argumentInfo.name}<#t>
	<#break>
	
	<#case "constructorInvocation">
		<@constructorInvocation arg.argumentInfo/>
	<#break>
		
	<#case "methodInvocation">
		<@methodInvocation arg.argumentInfo/>
	<#break>
	
	<#case "field">
		<@fieldAccess arg.argumentInfo/>
	<#break>
	
	<#case "one_dummy">
		${one_dummy}
	<#break>
	
	<#case "other_dummy">
		${other_dummy}
	<#break>

	<#default>
		<#stop "Unsupported argument-type: ${arg.type}">
</#switch>
</#macro>