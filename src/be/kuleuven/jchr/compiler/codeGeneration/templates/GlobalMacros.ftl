<#macro header><#include "header.txt"/></#macro>

<#macro author>
 * @author Peter Van Weert
<#-- * @author Widderok Oitan Veutwerkt -->
</#macro>

<#-- PACKAGE -->
<#assign packageroot = "${chrpackage}"/>

<#-- HANDLERNAME -->
<#assign handlerName="$handler"/>

<#macro packageDeclaration>
	package ${packageroot}.${handler};<#t>
</#macro>

<#macro simplePackageDeclaration>
	package ${packageroot};<#t>
</#macro>

<#-- TYPE VARIABLES -->
<#macro fullTypeParameterList list=typeParameters>
	<#if list?size != 0>
	<<#list list as variable>
		<@fullTypeParameter variable/><#if variable_has_next>, </#if>
	</#list>>
	</#if>
</#macro>

<#macro typeParameterList list=typeParameters>
	<#if list?size != 0>
	<<#list list as variable>
		${variable.name}<#if variable_has_next>, </#if>
	</#list>>
	</#if>
</#macro>

<#macro fullTypeParameter theTypeVariable>
	<#local upperBounds = theTypeVariable.upperBounds />
	${theTypeVariable.name}
	<#if upperBounds?size != 0> extends <@boundList upperBounds/></#if>
</#macro>

<#macro boundList list>
	<#list list as type>${type}<#if type_has_next>& </#if></#list>
</#macro>

<#-- CONSTRAINTS -->
<#macro fullConstraintType constraint=current.constraint>
	<@constraintTypeName constraint/><@fullTypeParameterList/><#t>
</#macro>

<#macro constraintType constraint=current.constraint>
	<@constraintTypeName constraint/><@typeParameterList/><#t>
</#macro>

<#macro constraintTypeList constraints>
	<#list constraints as constraint>
		<@constraintType constraint/><#if constraint_has_next>, </#if>
	</#list>
</#macro>

<#macro constraintTypeName constraint=current.constraint>
	${constraint.identifier?cap_first}Constraint<#t>
</#macro>

<#macro getVariable variable>
	get${variable.identifier?cap_first}()
</#macro>

<#-- HANDLER -->
<#macro handlerTypeName>
	<#list handler?split("_") as s>${s?cap_first}</#list>Handler<#t>
</#macro>

<#macro handlerType>
	<@handlerTypeName/><@typeParameterList/><#t>
</#macro>

<#macro fullHandlerType>
	<@handlerTypeName/><@fullTypeParameterList/><#t>
</#macro>

<#-- TELL -->
<#macro tellConstraint constraint>
	tell${constraint.identifier?cap_first}<#t>
</#macro>

<#-- CONSTRAINT STORES -->
<#macro storeConstraint constraint=current.constraint>
	store${constraint.identifier?cap_first}<#t>
</#macro>

<#macro lookup constraint, lookupType>
	<@lookupBasis constraint/><@lookupTypeSuffix lookupType/><#t>
</#macro>

<#macro masterLookup constraint>
	<@lookupBasis constraint/><#t>
</#macro>

<#macro lookupBasis constraint>lookup${constraint.identifier?cap_first}<#t></#macro>

<#-- SUFFIX used to make unique names for indices, lookupMethods, ... -->	
<#macro lookupTypeSuffix lookupType>
	<#switch lookupType.category.indexType>
		<#case "NONE"><#break>	<#-- NO SUFFIX -->
				
		<#case "HASH_MAP">
			<@lookupCategorySuffix lookupType.category/>_${lookupType.index}<#t>
		<#break>
		
		<#default>
			<#stop "Unsupported index-type: ${lookupType.category.indexType}">
	</#switch>
</#macro>

<#macro lookupCategorySuffix lookupCategory>
	<#switch lookupCategory.indexType>
		<#case "NONE"><#break>	<#-- NO SUFFIX -->
				
		<#case "HASH_MAP">
			_HashMap_${lookupCategory.index}<#t>
		<#break>
		
		<#default>
			<#stop "Unsupported index-type: ${lookupCategory.indexType}">
	</#switch>
</#macro>

<#-- ITERATORS -->
<#macro iteratorType constraint>
	Iterator<<@constraintType constraint/>><#t>
</#macro>

<#-- TUPLES -->
<#macro tupleTypeName arity>
	${"Tuple${arity}"}
</#macro>

<#macro tupleType arity>
	${"Tuple${arity}"}
</#macro>

<#macro fullTupleType constraints>
	<@tupleType constraints?size/><<@constraintTypeList constraints/>>
</#macro>

<#-- 
	Soms is het nodig om enkel een komma te zetten als 
	de lijst ervoor niet leeg is.
-->
<#macro comma list>
	<#if list?size != 0>,</#if>
</#macro>

<#-- VARIABLE LISTS -->
<#macro variableList list=currentVariables>	
	<#list list as variable>
		${variable.identifier}<#if variable_has_next>, </#if>
	</#list>
</#macro>

<#macro fullVariableList list=currentVariables>
	<#list list as variable>
		${variable.variableType.type} ${variable.identifier} <#if variable_has_next>, </#if>
	</#list>
</#macro>

<#-- OCCURRENCE LISTS -->
<#macro occurrenceList list>
	<#list list as occurrence>
		<@occurrenceName occurrence/><#if occurrence_has_next>, </#if>
	</#list>
</#macro>

<#macro fullOccurrenceList list>
	<#list list as occurrence>
		<@constraintType occurrence/> <@occurrenceName occurrence/>
		<#if occurrence_has_next>, </#if>
	</#list>
</#macro>

<#macro occurrenceName occurrence=current.constraint nbr=-1>
	${occurrence.identifier}_${occurrence.nbr?default(nbr)}<#t>
</#macro>