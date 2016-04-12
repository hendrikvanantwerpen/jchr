<#--	
	Doe de nodige imports...
-->
<#import "HashMapStorageKey.ftl" as hashMapStorageKey/>
<#import "HashMapKeyMacros.ftl" as macros/>
<#import "ConjunctMacros.ftl" as cm/>
<#assign solverPrefix="" in cm/>

<#macro innerClass constraint, lookupType>
<#local storageKeyType><@hashMapStorageKey.type constraint, lookupType.category/></#local>
<#local lookupKeyType><@type constraint, lookupType/></#local>
<#local arity = lookupType.guardinfos?size/>

private final class ${lookupKeyType} implements LookupKey {

<#list lookupType.guardinfos as info>
	public ${info.otherType} X${info_index};
</#list>
    private int hashCode;
    
    public ${lookupKeyType} () {
    	// NOP
    }
					
	public ${lookupKeyType}(<@fullArgumentList lookupType/>) {
		<@macros.initArguments arity/>
		<@initHashCode lookupType, arity/>
	}
	
	public void init(<@fullArgumentList lookupType/>) {
		<@macros.initArguments arity/>
		<@initHashCode lookupType, arity/>
	}
				
	@Override
	public boolean equals(Object key) {
		final ${storageKeyType} other = (${storageKeyType})key;
			
		return <#list lookupType.guardinfos as info>
			<#assign one_dummy = "other.X${info_index}" in cm/>
			<#assign other_dummy = "this.X${info_index}" in cm/>
			<@cm.conjunct info.eq />
			<#if info_has_next>&&</#if>
		</#list>;
	}
	
	@Override
	public int hashCode() {
		return hashCode;
	}
}
</#macro>

<#macro type constraint, lookupType>
	${constraint.identifier?cap_first}LookupKey<@global.lookupTypeSuffix lookupType/>
</#macro>

<#macro fullArgumentList lookupType>
	<#list lookupType.guardinfos as info>
		${info.otherType} X${info_index}<#if info_has_next>, </#if>
	</#list>
</#macro>

<#macro initHashCode lookupType, arity>
	this.hashCode = 
	<#list 1..arity as i>37 * (</#list>23<#list 1..arity as i>) + <@hashCode lookupType, i-1/></#list>;
</#macro>

<#macro hashCode lookupType, index>
	<@macros.hashCode "X${index?string.number}", lookupType.guardinfos[index].otherType/>
</#macro>