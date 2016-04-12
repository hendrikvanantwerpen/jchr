<#--
	Doe de nodige imports...
-->
<#import "ConjunctMacros.ftl" as cm/>
<#import "HashMapKeyMacros.ftl" as macros/>
<#assign solverPrefix="" in cm/>

<#-- 
	De hoofdmacro voor deze template: definieert een innerclass voor in
	de Handler...
-->
<#macro innerClass constraint, lookupCategory, hashMapName, constraintListType>
<#local keyType><@type constraint, lookupCategory/></#local>
<#local arity = lookupCategory.variables?size/>

private final class ${keyType} extends MutableStorageKey implements Cloneable<${keyType}> {

<#list lookupCategory.variables as variable>
	public ${variable.variableType.type} X${variable_index};
</#list>
    private int hashCode;
    
    public ${keyType}() {
    	// NOP
    }
					
	public ${keyType}(<@fullArgumentList lookupCategory/>) {
		<@macros.initArguments arity/>
		<@initHashCode lookupCategory, arity/>
	}
	
	public void init(<@fullArgumentList lookupCategory/>) {
		<@macros.initArguments arity/>
		<@initHashCode lookupCategory, arity/>	
	}
	
	@Override
	public void removeSelf() {
		${hashMapName}.remove(this);
	}
	
	@Override
	public boolean rehash() {
		final ${constraintListType} temp1 = ${hashMapName}.remove(this);		
		if (temp1 == null || temp1.isEmpty()) return false;
		<@initHashCode lookupCategory, arity/>
		final ${constraintListType} temp2 = ${hashMapName}.put(this, temp1);
		if (temp2 == null) return true;
		temp1.mergeWith(temp2);
		temp2.clear();
		// Assumption: the old key is still used. This NOT specified
		// in the API, but it is the case in the Sun reference implementation.
		// Note that it is a reasonable assumption other implementations will
		// also not replace the key with the given key, only compare it with
		// the one present in the data-structure...
		return false;
	}

	@Override
	public boolean equals(Object key) {
		final ${keyType} other = (${keyType})key;
			
		return <#list lookupCategory.variables as variable>
			<#assign one_dummy = "this.X${variable_index}" in cm/>
			<#assign other_dummy = "other.X${variable_index}" in cm/>
			<@cm.conjunct variable.variableType.eq />
			<#if variable_has_next>&&</#if>
		</#list>;
	}
	
	@Override
	public int hashCode() {
		return hashCode;
	}
	
	@Override
	public ${keyType} clone() {
		try {			
			return (${keyType})super.clone();
		} catch (CloneNotSupportedException cnse) {
			throw new InternalError();
		}
	}
	
<#--@Override
	public String toString() {
		return String.valueOf(hashCode);
	}-->
}
</#macro>

<#macro type constraint, lookupCategory>
	${constraint.identifier?cap_first}StorageKey<@global.lookupCategorySuffix lookupCategory/>
</#macro>

<#macro fullArgumentList lookupCategory>
	<#list lookupCategory.variables as variable>
		${variable.variableType.type} X${variable_index}<#if variable_has_next>, </#if>
	</#list>
</#macro>

<#macro initHashCode lookupCategory, arity>
	this.hashCode = 
	<#list 1..arity as i>37 * (</#list>23<#list 1..arity as i>) + <@hashCode lookupCategory, i-1/></#list>;
</#macro>

<#macro hashCode lookupCategory, index>
	<@macros.hashCode "X${index?string.number}", lookupCategory.variables[index].variableType.type/>
</#macro>