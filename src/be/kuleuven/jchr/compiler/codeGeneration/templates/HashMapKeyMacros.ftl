<#macro initArguments arity>
	<#list 1..arity as i>
	this.X${i-1} = X${i-1};
	</#list>
</#macro>

<#macro hashCode X, type>
	<#switch type>
		<#case "boolean">
			(${X}? 1 : 0)
		<#break>
	
		<#case "int">
		<#case "short">
		<#case "byte">
			${X}
		<#break>
		
		<#case "char">
			(int)${X}
		<#break>
		
		<#case "long">
			(int)(${X} ^ (${X} >>> 32))
		<#break>
		
		<#case "float">
			Float.floatToIntBits(${X})
		<#break>
		
		<#case "double">
			(int)(Double.doubleToLongBits(${X}) ^ (Double.doubleToLongBits(${X}) >>> 32))
		<#break>
		
		<#case "null">
			0
		<#break>
		
		<#default>
			${X}.hashCode()
	</#switch>
</#macro>