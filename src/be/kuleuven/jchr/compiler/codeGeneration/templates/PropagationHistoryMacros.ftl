<#macro name ruleNbr>
	$$propagationHistory_rule${ruleNbr}<#t>
</#macro>

<#macro inHistoryName ruleNbr>
	$$inPropagationHistory_rule${ruleNbr}<#t>
</#macro>

<#macro constraintHashMap occurrence>
	<@hashMap><@global.constraintType occurrence/></@hashMap>
</#macro>

<#macro tupleHashMap occurrences>
	<@hashMap><@global.fullTupleType occurrences/></@hashMap>
</#macro>

<#macro fullNewTuple occurrences, name="tuple">
	<#local type><@global.fullTupleType occurrences/></#local>
	${type} ${name} = new ${type}(<@global.occurrenceList occurrences/>)
</#macro>

<#macro newTuple occurrences>
	new <@global.fullTupleType occurrences/>(<@global.occurrenceList occurrences/>)
</#macro>

<#macro hashMap>
	HashMap<<#nested>, Object>
</#macro>

<#-- NOn-Passive OCcurrence of Current Constraint -->
<#function isNopoccc occurrence>
	<#return occurrence.identifier == current.constraint.identifier && !occurrence.isPassive/>
</#function>

<#-- NOn-Passive Occurrence Of Other Constraint -->
<#function isNopoooc occurrence>
	<#return occurrence.identifier != current.constraint.identifier && !occurrence.isPassive/>
</#function>

<#function containsNopoccc occurrences>
	<#return getFirstNopocccIndex(occurrences) != -1/>	
</#function>

<#function containsNopoooc occurrences>
	<#list occurrences as occurrence>
		<#if isNopoooc(occurrence)>
			<#return true/>
		</#if>
	</#list>
	<#return false/>
</#function>

<#function getFirstNopocccIndex occurrences>
	<#list occurrences as occurrence>
		<#if isNopoccc(occurrence)>
			<#return occurrence_index/>
		</#if>
	</#list>
	<#return -1/>
</#function>

<#function deleteElementAt list, index>
	<#if index == 0>
		<#return list[1..list?size-1]/>
	<#elseif index = list?size-1>
		<#return list[0..list?size-2]/>
	<#else>	
		<#return list[0..index-1] + list[index+1..list?size-1]/>
	</#if>
</#function>

<#-- 
	What do we know allready?
		* Rule ruleNbr is a propagation rule.
		* At least one of the occurrences in its head is one of
			the current constraint
-->
<#macro propagationHistory ruleNbr>
	<#local name><@name ruleNbr/></#local>
	<#local head = rules[ruleNbr - 1].head/>
	<#local firstNopoccc = getFirstNopocccIndex(head)/>

	<#-- If no nopoccc exist, nothing has to be kept -->
	<#if firstNopoccc != -1>
		<#switch head?size>
		
		<#case 1>
			protected boolean ${name};
		<#break>
		
		<#case 2>
			<#if firstNopoccc == 0>
				<#local type><@constraintHashMap head[1]/></#local>
			<#else>
				<#local type><@constraintHashMap head[0]/></#local>
			</#if>
			
			protected ${type} ${name} = new ${type}();
		<#break>
					
		<#default>
			<#if removePassiveOccurrences(head)?size == 1>
				<#local type><@tupleHashMap deleteElementAt(head, firstNopoccc)/></#local>
			<#else>	
				<#local type><@tupleHashMap head/></#local>
			</#if>
			
			protected ${type} ${name} = new ${type}();
		</#switch>
	</#if>
</#macro>

<#-- 
	What do we know allready?
		* Rule ruleNbr is a propagation rule.
		* At least one of the occurrences in its head is one of
			the current constraint, more even: one of them is a nopoccc,
		* The active occurrence is the one with the given number
-->
<#macro notInHistoryTest ruleNbr, occurrenceNbr>
	<#local name><@name ruleNbr/></#local>
	<#local head = rules[ruleNbr - 1].head/>
	
	<#switch head?size>
		<#case 1>
			if (!${name})
		<#break/>
		
		<#case 2>
			<#local other = head[occurrenceNbr % 2]/>
			<#local otherID><@global.occurrenceName other/></#local>
			
			if (!${name}.containsKey(${otherID})
			<#if !other.isPassive>
				&& !${otherID}.${name}.containsKey(this)
			</#if>
			)
		<#break/>
		
		<#default>
			<#if removePassiveOccurrences(head)?size == 1>
				if (! ${name}.containsKey(<@newTuple deleteElementAt(head, occurrenceNbr-1)/>))
			<#else>	
				final <@fullNewTuple head/>;
				if (! (${name}.containsKey(tuple)
				<#list head as occurrence>
					<#if occurrence_index != occurrenceNbr-1 && !occurrence.isPassive>
						|| <@global.occurrenceName occurrence/>.${name}.containsKey(tuple)
					</#if>
				</#list>
				))
			</#if>
	</#switch>
</#macro>

<#-- 
	What do we know allready?
		* Rule ruleNbr is a propagation rule.
		* At least one of the occurrences in its head is one of
			the current constraint, more even: one of them is a nopoccc,
		* The active occurrence is the one with the given number
-->
<#macro addToHistory ruleNbr, occurrenceNbr>
	<#local name><@name ruleNbr/></#local>
	<#local head = rules[ruleNbr - 1].head/>
	
	<#switch head?size>
		<#case 1>
			${name} = true;
		<#break/>
		
		<#case 2>
			${name}.put(<@global.occurrenceName head[occurrenceNbr % 2]/>, PRESENT);
		<#break/>
		
		<#default>
			<#if removePassiveOccurrences(head)?size == 1>
				<#local occurrences = deleteElementAt(head, occurrenceNbr-1)/>
			<#else>	
				<#local occurrences = head/>
			</#if>
			
			${name}.put(<@newTuple occurrences/>, PRESENT);
	</#switch>
</#macro>