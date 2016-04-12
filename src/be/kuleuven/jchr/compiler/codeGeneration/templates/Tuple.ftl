<@global.header/>

<@global.simplePackageDeclaration/>

/**
<@global.author/>
 */
public class <@global.tupleTypeName arity/><
	<#list 1..arity as i>
		T${i}<#if i_has_next>, </#if>
	</#list>> {
	<#list 1..arity as i>
		public T${i} X${i};
	</#list>
    private int hashcode;
    
    public <@global.tupleTypeName arity/>() {
    	// NOP
    }
    
    public <@global.tupleTypeName arity/>(<@fullArgumentList/>) {
    	<@init/>
    }
    
    public void init(<@fullArgumentList/>) {
    	<@init/>
    }
    
    /**
     * @pre other instanceof <@global.tupleType arity/>
     */
    @Override
    public boolean equals(Object other) {
        return 
        <#list 1..arity as i>
        	(this.X${i} == ((<@global.tupleTypeName arity/>)other).X${i}) 
        <#if i_has_next>&&</#if>
        </#list>
        ;
    }
    
    @Override
    public int hashCode() {
        return hashcode;
    }
    
    @Override
    public String toString() {
    	<#-- arity * 6 seems a reasonable estimate, no? -->
    	final StringBuilder result = new StringBuilder(${arity * 6});
        result.append('(');
        <#list 1..arity as i>
        	result.append(X${i}.toString());
        	<#if i_has_next>result.append(',');</#if>
        </#list>
        result.append(')');
        return result.toString();
    }
}

<#macro fullArgumentList>
    <#list 1..arity as i>
		T${i} X${i} <#if i_has_next>,</#if>
	</#list>
</#macro>

<#macro init>
    <#list 1..arity as i>
    	this.X${i} = X${i};
    </#list>
    <#-- based upon http://www.javapractices.com/Topic28.cjp -->
    this.hashcode = <#list 1..arity as i>37 * (</#list>23<#list 1..arity as i>) + X${i}.hashCode()</#list>;
</#macro>