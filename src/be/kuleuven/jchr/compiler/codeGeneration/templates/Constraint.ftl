<#--
	Doe de nodige imports...
-->
<#import "ConjunctMacros.ftl" as cm/>
<#import "PropagationHistoryMacros.ftl" as history/>

<#--
	Syntactische suiker: afkorting voor de current constraint (d.i. de constraint 
	waarvoor we een bestand aan het	genereren zijn) en zijn variabelen.
	(was vroeger complexere uitdrukking, en had toen meer zin ;-) )
-->	
<#assign currentVariables = current.constraint.variables/>

<#--
	Hulpvariabele om ervoor te zorgen dat slechts 1 keer de code
	voor de propagation-history van een regel wordt gegenereerd per constraint
	(vergemakkelijkt het opstellen van het datamodel).
-->
<#assign lastPropHist = -1/>

<#--
	Zal bijhouden welke identifiers er al gebruikt zijn per constraint,
	en waartegen dus op gelijkheid moet worden getest.
-->
<#assign partnerConstraints = createPartnerConstraintsStore()/>

<@global.header/>

<@global.packageDeclaration/>

import be.kuleuven.jchr.runtime.Constraint;

import java.util.HashMap;
import java.util.Iterator;


/**
<@global.author/>
 */
public final class <@global.fullConstraintType/> extends Constraint {
	<#------------------------------------------------------
		De Handler bevat alle in deze handler gebruikte
		solvers, alsook de constraintstore met alle nodige
		indices.
		Alle aanspraken op een solver gebeuren via dit
		gedeeld object, vandaar het assign-statement.
	------------------------------------------------------->	
	protected final <@global.handlerType/> ${global.handlerName};	
	<#assign solverPrefix="${global.handlerName}." in cm/>
	
	<#------------------------------------------------------
		Declaratie van alle gebruikte variabelen, met een 
		te kiezen identifier om de leesbaarheid
		te verhogen. (bvb X, Y, Z)
	------------------------------------------------------->
	<@variableDeclarationList modifiers="protected final"/>
	
	<#------------------------------------------------------
		De constructor...
	------------------------------------------------------->
	public <@global.constraintTypeName/>(
		<@global.fullVariableList currentVariables/><@global.comma currentVariables/>
		<@global.handlerType/> ${global.handlerName}) {
		
        this.${global.handlerName} = ${global.handlerName};
        
        <#list currentVariables as variable>
        	this.${variable.identifier} = ${variable.identifier};
        </#list>
    }

	<#------------------------------------------------------
		Getter-methodes voor de verschillende variabelen.
	------------------------------------------------------->
    <#list currentVariables as variable>
    	public ${variable.variableType.type} <@global.getVariable variable/> {
    		return this.${variable.identifier};
    	}
    </#list>
    
    /**
     * @inheritDoc
     */
    @Override
    public final void activate() {    	
        <@occurrences true/>
    }

    /**
     * @inheritDoc
     */
    @Override        
    public final void reactivate() {
	    <#if current.occurrences?size == 0>
	    	// NOP
	    <#else>
	    	generation++;
			<@occurrences false/>
		</#if>
    }

    <#list current.occurrences as occurrence>
    	<#-- Eventueel de nodige code voor de propagationHistory genereren -->
    	<#if isPropagationRule(rules[occurrence.ruleNbr - 1]) && lastPropHist != occurrence.ruleNbr>
    		<#-- zorgen dat dit stuk code max eens per regel wordt gegenereerd -->
    		<#assign lastPropHist=occurrence.ruleNbr/>
    		<@history.propagationHistory occurrence.ruleNbr/>
    	</#if>
    	
    	<#-- Hier komt de hoofdbrok! -->
    	<@occurrenceMethod occurrence/>
    </#list>
        
    @Override
    public final String toString() {
    	return new StringBuilder()
    		.append("${current.constraint.identifier}(")
<#list currentVariables as variable>
			.append(${variable.identifier})
	<#if variable_has_next>
			.append(", ")
	</#if>
</#list>
			.append(")#")
			.append(ID)
			.toString();
    }
}

<#-------------------------------------------------------------------->
<#--						MACRODEFINITIES							-->
<#-------------------------------------------------------------------->

<#macro occurrences lateAddition>
	<#if lateAddition && current.occurrences?size == 0>
		<@juststore/>
	<#else>
		if (
		<#list current.occurrences as occurrence>
			<#-- Volgende was eleganter voordat passive occurrences werden ingevoerd... -->
			<@global.occurrenceName nbr=rules[occurrence.ruleNbr - 1].head[occurrence.occurrenceNbr - 1].nbr/>()
			<#if occurrence_has_next> &&</#if>
		</#list>
		) {
			<#if lateAddition><@store/></#if>
		}
	</#if>
</#macro>

<#macro variableDeclarationList types=currentVariables arguments=[] modifiers="">
	<#--
		Als er geen argumenten zijn opgegeven, dan gebruiken we de identifiers van
		de type-lijst. (opm: als wel opgegeven en lijst is 0 lang, mag deze tak
		eveneens, want dan moet "types" ook wel 0 lang zijn)
	-->
	<#if arguments?size = 0>
		<#list types as variable>
			${modifiers} ${variable.variableType.type} ${variable.identifier};
		</#list>
	<#else>	<#-- Anders gebruiken we de identifiers van de opgegeven argumenten -->
		<#list types as variable>
			<#local var2=arguments[variable_index].argumentInfo>
			<#if !var2.anonymous>
			${modifiers} ${variable.variableType.type} ${var2.identifier};
			</#if>
		</#list>
	</#if>
</#macro>

<#macro occurrenceMethod occurrence>
	<#local rule = rules[occurrence.ruleNbr - 1]/>
	<#local head = rule.head/>
	<#local thisOccurrenceIndex = occurrence.occurrenceNbr - 1/>
	<#local thisOccurrence = head[thisOccurrenceIndex]/>
	<#local thisOccurrenceName><@global.occurrenceName nbr=thisOccurrence.nbr/></#local>
	<#local nextOccurrenceName><@global.occurrenceName nbr=thisOccurrence.nbr + 1/></#local>
	<#local thisOccurrenceVars = thisOccurrence.arguments/>

	private final boolean ${thisOccurrenceName}() {
		final <@global.constraintType/> ${thisOccurrenceName} = this;
		<#list currentVariables as variable>
			<#local var2 = thisOccurrenceVars[variable_index].argumentInfo/>
			<#if !var2.anonymous>
			final ${variable.variableType.type} 
				${var2.identifier} = this.${variable.identifier};
			</#if>
		</#list>
			
		<#--
			Hulpvariabele die zal bijhouden of de huidige 
			search nog een universele search is, resp of
			een label is gezet.
			Als de actieve constraint verwijderd wordt, dan
			zijn alle searches existentioneel.
		-->
<#--			<#assign universal = !thisOccurrence.isRemoved/>
			<#assign labelSet = false/>
-->
		<#assign guarded = false/>
		
		${partnerConstraints.reset(current.constraint.identifier, thisOccurrenceName)}
		<@recurse_init occurrence.commandos/>
		
		<#-- 
			Er moet (en mag!!) een extra returnstatement zijn voor als 
			 - alle iteratoren gewoon eindigen
			 - er geen iteratoren zijn, maar de guard faalt
			 - er geen iteratoren zijn en geen guard, maar de fired-check
			 		faalt!
		-->
		<#if head?size gt 1  <#-- maw, er zijn iteratoren -->
			|| <#-- er zijn geen iteratoren && -->	   guarded
			|| <#-- geen iteratoren of guard, maar --> isPropagationRule(rule)>
		
			return true;
		</#if>
	}
</#macro>

<#macro recurse_init list>		
	<@recurse {"list":list, "index":0}/>
</#macro>

<#-- 
	Hier maken we ge/misbruik van de .vars - variabele, en het feit dat 
	een macro-naam om een vorm van ``reflectieve macro-oproepen'' te bekomen. 
-->
<#macro recurse iterator>
	<#if iterator.list?size gt iterator.index>
    	<#local 
    		info = iterator.list[iterator.index]
    		new_iterator = {"list":iterator.list, "index":iterator.index+1}
		/>
    	<@.vars["recurse_${info.type}"] info, new_iterator/>
    </#if>
</#macro>

<#macro recurse_LOOKUP info iterator>
	<#local occurrence = info.occurrence/>
	<#local constraint = constraints[occurrence.identifier]>
	<#local iteratorName><@iteratorName occurrence/></#local>
   	<#local occurrenceName><@global.occurrenceName occurrence/></#local>
   	
   	final <@global.iteratorType occurrence/> ${iteratorName} 
   		= ${global.handlerName}.<@global.lookup constraint, info.lookupType/>(
			<@cm.argumentList info.arguments/>
		);
   	<@global.constraintType occurrence/> ${occurrenceName};
   	<@variableDeclarationList types=constraint.variables arguments=occurrence.arguments/>

<@labelName occurrence/>: while (${iteratorName}.hasNext()) {
   		${occurrenceName} = ${iteratorName}.next();

		<@newPartnerConstraintTest occurrence>
			<#list occurrence.arguments as variable>
				<#local id = variable.argumentInfo.identifier/>
				<#if !variable.argumentInfo.anonymous>
				${id} = ${occurrenceName}.${constraint.variables[variable_index].identifier};
				</#if>
			</#list>

			<@recurse iterator/>
		</@newPartnerConstraintTest>
	}
</#macro>

<#macro labelName occurrence>
	label_<@global.occurrenceName occurrence /><#t>
</#macro>

<#-- 
	Genereert de test of de nieuwe partnerconstraint niet een van de 
	vorige is indien nodig... Merk op dat we enkel vergelijken met
	constraints met dezelfde identifier. Meer optimisatie (hashing,
	binair zoeken, bomen, ...) is waarschijnlijk mogelijk, 
	maar zeker niet nodig zolang het aantal partnerconstraints
	veelal klein is.
-->
<#macro newPartnerConstraintTest constraint>
	<#local occurrenceName><@global.occurrenceName constraint/></#local>
	<#local identifiers = partnerConstraints.getIdentifiers(constraint.identifier)?default([])/>
	
	<#if (identifiers?size) gt 0>
		if (<#list identifiers as identifier>
				${identifier} != ${occurrenceName} <#if identifier_has_next> && </#if>
			</#list>) {
				${partnerConstraints.add(constraint.identifier, occurrenceName)}
				<#nested>
			}
	<#else>
		${partnerConstraints.add(constraint.identifier, occurrenceName)}
		<#nested>
	</#if>
</#macro>

<#macro iteratorName occurrence>
	<@global.occurrenceName occurrence/>_iter<#t>
</#macro>

<#macro recurse_GUARD info iterator>
	<#assign guarded = true/>
	if (<@cm.conjunct info.occurrence/>) {	// Guard
		<@recurse iterator/>
	}	
</#macro>

<#macro recurse_CHECK_FIRED info iterator>
	<@history.notInHistoryTest info.ruleNbr, info.occurrenceNbr/> {
		<@recurse iterator/>
	}
</#macro>

<#macro store>
    if (! stored) {    	
    	<@juststore/>
    	
    	<#list currentVariables as variable>
			<#if !variable.variableType.fixed>
				${variable.identifier}.addConstraintObserver(this);
			</#if>
        </#list>
	}
</#macro>

<#macro juststore>
	stored = true;
	${global.handlerName}.<@global.storeConstraint constraint/>(this);
</#macro>

<#macro recurse_COMMIT info iterator>
	<#local rule = rules[info.ruleNbr - 1]/>
	<#local head = rule.head/>
	<#local body = rule.body/>
	<#local thisOccurrenceIndex = info.occurrenceNbr - 1/>
	<#local simplify = head[thisOccurrenceIndex].isRemoved/>
	
	<#if isPropagationRule(rule)>
		<@history.addToHistory info.ruleNbr, info.occurrenceNbr/>
	<#else>
		<#list head as occurrence>
			<#if occurrence.isRemoved>
				<@global.occurrenceName occurrence/>.kill();
			</#if>
		</#list>
	</#if>
	
//	System.out.println("Rule ${rule.identifier} fired");
	
	<#if !simplify && body?size != 0>
		final int oldGeneration = generation;

		<@store/>
	</#if>
	
	<#list body as conjunct>
 		<@cm.conjunct conjunct/>;
	</#list>

	<#if !rule.endsWithFailure>
		<#if simplify>
			return false;
		<#elseif body?size != 0>
			if (oldGeneration != generation) return false;
			if (! alive) return false;
				
			<#list head as occurrence>
				<#if occurrence.isRemoved>
					continue <@labelName occurrence/>;
					<#break>	<#-- niet vergeten! -->
				<#elseif occurrence_index != thisOccurrenceIndex>
					if (!<@global.occurrenceName occurrence/>.isAlive())
						continue <@labelName occurrence/>;
				</#if>
			</#list>
		<#else>
			<#list head as occurrence>
				<#if occurrence.isRemoved>
					continue <@labelName occurrence/>;
					<#break>
				</#if>
			</#list>
		</#if>
		<#--
			Het laatste geval is wanneer de active constraint niet verwijderd is,
			en alle andere constraints ook niet. In dat geval moeten we gewoon
			de geneste iteratoren hun werk laten doen, maw: niets doen!
		-->
	</#if>
</#macro>