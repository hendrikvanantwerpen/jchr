<#--
	Doe de nodige imports...
-->
<#import "HashMapStorageKey.ftl" as hashMapStorageKey/>
<#import "HashMapLookupKey.ftl" as hashMapLookupKey/>

<@global.header/>

<@global.packageDeclaration/>

import be.kuleuven.jchr.runtime.list.ConstraintLinkedList;
import be.kuleuven.jchr.runtime.list.HashMapConstraintLinkedList;
import be.kuleuven.jchr.runtime.Handler;
import be.kuleuven.jchr.runtime.Constraint;

import be.kuleuven.jchr.annotations.JCHR_Constraints;
import be.kuleuven.jchr.annotations.JCHR_Constraint;
import be.kuleuven.jchr.annotations.JCHR_Tells;

import be.kuleuven.jchr.util.Cloneable;
import be.kuleuven.jchr.util.iterator.*;
import be.kuleuven.jchr.util.AbstractUnmodifiableCollection;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;


/**
<@global.author/>
 */
@JCHR_Constraints({
	<#list constraints?values as constraint>
	@JCHR_Constraint(
		identifier = "${constraint.identifier}",
		arity = ${constraint.variables?size}
		<#if constraint.infix?exists>,
		infix = "${constraint.infix}"
		</#if>		
	)<#if constraint_has_next>, </#if>
	</#list>
})
public class <@global.fullHandlerType/> extends Handler {

public <@global.handlerTypeName/>(<@fullSolverList/>) {
	<#list solvers as solver>
	this.${solver.identifier} = ${solver.identifier};
	</#list>
}

/*
 * SOLVERS
 **************/ 
<#list solvers as solver>
	public final ${solver.interface} ${solver.identifier};
</#list>

/*
 * TELL METHODS
 **************/
<#list constraints?values as constraint>
	@JCHR_Tells("${constraint.identifier}")
	public final void <@global.tellConstraint constraint/>(
		<@global.fullVariableList constraint.variables/>
	) {
		new <@global.constraintType constraint/>(
			<@global.variableList constraint.variables/><@global.comma constraint.variables/>
			this
		).activate();
	}
</#list>

/*
 * CONSTRAINTSTORE
 *********************/
<#list constraints?values as constraint>
	/*
	 * INDICES VOOR ${constraint.identifier?upper_case}-CONSTRAINT
	 ******************************************/
	<#list constraint.lookupCategories as lookupCategory>
		<#switch lookupCategory.indexType>
			<#case "NONE">
				<#assign type><@constraintListType constraint/></#assign>
				<#assign name><@constraintListName constraint/></#assign>
				<@doInitialisation type, name/>
			<#break>
			
			<#case "HASH_MAP">
				<#-- THE HASH_MAP ITSELF -->
				<#assign type><@hashMapType constraint/></#assign>
				<#assign name><@indexName constraint, lookupCategory/></#assign>
				<@doInitialisation type, name, "protected final"/>

				<#-- STORAGE-KEYS -->
				<#assign type><@hashMapConstraintListType constraint/></#assign>
				<@hashMapStorageKey.innerClass constraint, lookupCategory, name, type/>
				
				<#assign type><@hashMapStorageKey.type constraint, lookupCategory/></#assign>
				<#assign name><@hashMapStorageKeyName constraint, lookupCategory/></#assign>
				<@doInitialisation type, name, "private"/>
				
				<#list lookupCategory.lookupTypes as lookupType>
					<#-- LOOKUP-KEYS -->
					<@hashMapLookupKey.innerClass constraint, lookupType/>
					
					<#assign type><@hashMapLookupKey.type constraint, lookupType/></#assign>
					<#assign name><@hashMapLookupKeyName constraint, lookupType/></#assign>
					<@doInitialisation type, name/>				
				</#list>
			<#break>
			
			<#default>
				<#stop "Unsupported index-type: ${lookupCategory.indexType}">
		</#switch>
	</#list>

	/**
	 * Adds the given constraint-object <code>constraint</code> to the
     * constraint store. 
	 *
	 * @param constraint
	 * 	The constraint that has to be added to the constraint store.
	 *
	 * @pre <code>constraint != null</code>
	 * @pre The constraint is newer then all other constraints in the
	 *	store.
	 *
	 * @see runtime.Constraint#isNewerThan(runtime.Constraint)
	 */
	void <@global.storeConstraint constraint/>(
		<@global.constraintType constraint/> constraint
	) {
		<#assign declaredHashMapConstraintList = false/>
		<#list constraint.lookupCategories as lookupCategory>
			<#switch lookupCategory.indexType>
				<#case "NONE">
					<@constraintListName constraint/>.addFirst(constraint);
				<#break>
				
				<#case "HASH_MAP">
					<#assign index><@indexName constraint, lookupCategory/></#assign>
					<#assign key><@hashMapStorageKeyName constraint, lookupCategory/></#assign>
					<#assign listType><@hashMapConstraintListType constraint/></#assign>
					<#assign listName="list"/>
					
					<#if !declaredHashMapConstraintList>
						${listType} ${listName};
						<#assign declaredHashMapConstraintList = true/>
					</#if>
					
					${key}.init(
						<#list lookupCategory.variables as variable>
							constraint.<@global.getVariable variable/>
							<#if variable_has_next>, </#if>
						</#list>
					);
					
					${listName} = ${index}.get(${key});
					
					if (${listName} == null) {
						${listName} = new ${listType}(${key});
						${index}.put(${key}, ${listName});
						<@addHashObserver lookupCategory.variables, key/>
						${key} = new <@hashMapStorageKey.type constraint, lookupCategory/>();
					}
					else {	// ${listName} != null
						<@addHashObserver lookupCategory.variables, "${listName}.getKey()"/>
					}
					
					${listName}.addFirst(constraint);
				<#break>
			
				<#default>
					<#stop "Unsupported index-type: ${lookupCategory.indexType}">
			</#switch>
		</#list>
	}
	
	/*
	 * LOOKUP-METHODS VOOR ${constraint.identifier?upper_case}-CONSTRAINT
	 *******************************************/
	<#list constraint.lookupCategories as lookupCategory>
		<#if lookupCategory.master>
			/**
			 * Returns an iterator over all <code><@global.constraintType constraint/></code>s currently
			 * in the constraint store. The <code>Iterator.remove()</code> method is never supported.
			 * Besides that, we offer very few guarantees about the behavior of these iterators:
			 * <ul>
			 *  <li>
			 *      There are no guarantees concerning the order in which the constraints 
			 *      are returned.
			 *  </li>
			 *  <li>
			 *      The iterators <em>might</em> fail if the constraint store is structurally modified
			 *      at any time after the <code>Iterator</code> is created. In the face of concurrent modification
			 *      it cannot recover from, the <code>Iterator</code> fails quickly and cleanly (throwing a
			 *      <code>ConcurrentModificationException</code>), rather than risking arbitrary, 
			 *      non-deterministic behavior at an undetermined time in the future.
			 *      <br/>
			 *      The <i>fail-fast</i> behavior of the <code>Iterator</code> is not guaranteed,
			 *      even for single-threaded applications (this constraint is inherited from the 
			 *      <a href="http://java.sun.com/j2se/1.5.0/docs/guide/collections/">Java Collections Framework</a>).
			 *      and should only be used to detect bugs. 
			 *      <br/>
			 *      Important is that, while <code>Iterator</code>s returned by collections of the 
			 *      Java Collections Framework generally &quot;fail fast on a best-effort basis&quot;, 
			 *      this is not the case with our <code>Iterator</code>s. On the contrary: our
			 *      iterators try to recover from structural changes &quot;on a best-effort basis&quot;,
			 *      and fail cleanly when this is not possible (or possibly to expensive). So,
			 *      in general you can get away with many updates on the constraint store during
			 *      iterations (there is no way of telling which will fail though...)
			 *  </li>
			 *  <li>
			 *      As a general note: structural changes between calls of hasNext() and next()
			 *      are a bad idea: this easily leads to <code>ConcurrentModificationException</code>s.
			 *  </li>
			 *  <li>
			 *      The failure of the <code>Iterator</code> might only occur some time after
			 *      the structural modification was done: this is again because many parts
			 *      of the constraint store are iterable in the presence of modification.
			 *  </li>
			 *  <li>
			 *      When a constraint is added to the constraint store after the creation of the
			 *      iterator it is possible it appears somewhere later in the iteration, but
			 *      it is equally possible it does not.
			 *  </li>
			 *  <li>
			 *      Removal of constraints on the other hand does mean the iterator will never return
			 *      this constraint.
			 *      Note that it still remains possible that the iterator fails somewhere after
			 *      (and because of) this removal.
			 *  </li>
			 * </ul>
			 * The lack of guarantees is intentional. Some <i>Iterator</i>s might behave perfectly 
			 * in the presence of constraint store updates, whilst others do not. Some might return
			 * constraints in order of their creation (and only iterate over constraints that existed
			 * at the time of their creation), others do not. In fact: it is perfectly possible that 
			 * their behavior changes between two compilations (certainly when moving to a new version
			 * of the compiler). This is the price (and at the same time the bless) of declarative 
			 * programming: it is the compiler that chooses the data structures that seem optimal 
			 * to him at the time!
			 *
			 * @return An iterator over all <code><@global.constraintType constraint/></code>s currently
			 * 	in the constraint store.
			 */
			public <@global.iteratorType constraint/> <@global.masterLookup constraint/>() {
			<#switch lookupCategory.indexType>
				<#case "NONE">
					return <@constraintListName constraint/>.iterator();
				<#break>
			
				<#case "HASH_MAP">
					return new NestedCollectionsIterator<<@global.constraintType constraint/>>(
						<@indexName constraint, lookupCategory/>
					);
				<#break>
			
				<#default>
					<#stop "Unsupported index-type: ${lookupType.indexType}">
			</#switch>
			}
			
			/**
			 * Returns (an unmodifiable view of) the current collection of 
			 * <code><@global.constraintType constraint/></code>s currently in the constraint store. 
			 * Iterators over this collection are the equivalents of those
			 * created by the <code><@global.masterLookup constraint/></code>-method.
			 * We refer to this method for more information on their behavior.
			 * This collection is backed by the constraint store: updates 
			 * to the store will be reflected in the collection.
			 *
			 * @return (An unmodifiable view of) the current collection of 
			 * 	<code><@global.constraintType constraint/></code>s currently 
			 *	in the constraint store. 
			 *
			 * @see #<@global.masterLookup constraint/>
			 */
			public <@collectionType constraint/> <@getCollection constraint/>() {
				<#assign type><@global.constraintType constraint/></#assign>
				<@unmodifiableCollection type>
					<@global.masterLookup constraint/>()
				</@unmodifiableCollection>
			}
		</#if>
		
		<#-- The lookup method for a "NONE-category" is already taken care of above -->	
		<#if lookupCategory.indexType != "NONE">

		<#-- 
			<==== resetting indentation here for clarity 
		-->	

	<#list lookupCategory.lookupTypes as lookupType>
		public <@global.iteratorType constraint/> <@global.lookup constraint, lookupType/>(

		<#switch lookupCategory.indexType>
			<#case "HASH_MAP">
				<@hashMapFullLookupArgumentList lookupType, lookupCategory.variables/>
			) {
				<#assign index><@indexName constraint, lookupCategory/></#assign>
				<#assign lookup><@hashMapLookupKeyName constraint, lookupType/></#assign>
						
				${lookup}.init(<@hashMapLookupArgumentList lookupCategory.variables/>);
						
				<@constraintListType constraint/> temp = ${index}.get(${lookup});
					
				if (temp == null)
					return EmptyIterator.getInstance();
				else
					return temp.iterator();
			<#break>
			
			<#default>
				<#stop "Unsupported index-type: ${lookupType.indexType}">
		</#switch>
		}
	</#list>	<#-- end looping over lookupTypes -->
	</#if>		<#-- end condition on "NONE-category" -->	
	</#list>	<#-- end looping over lookupCategories -->
</#list>		<#-- end looping over constraint -->


<@lookupMethod "lookup"/>

<@lookupMethod "iterator"/>

<#macro lookupMethod name>
	/**
	 * {@inheritDoc}
	 *
	 <#list constraints?values as constraint>
	 * @see #<@global.masterLookup constraint/>()
	 </#list>
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Iterator<Constraint> ${name}() {
		return new ChainingIterator<Constraint>(
			<#list constraints?values as constraint>
				<@global.masterLookup constraint/>()<#if constraint_has_next>,</#if>
			</#list>
		);
	}
</#macro>
    
    // This implementation is still very inefficient: 
	// don't over-use this feature yet!
	@Override
	public int size() {
        return IteratorUtilities.size(iterator());
    }
    
    @Override
    public boolean isEmpty() {
		return !iterator().hasNext();
    }
}

<#-------------------------------------------------------------------->
<#--						MACRODEFINITIES							-->
<#-------------------------------------------------------------------->

<#macro hashMapFullLookupArgumentList lookupType, variables>
	<#list lookupType.guardinfos as info>
		${info.otherType} <@hashMapLookupArgumentName variables[info_index]/><#if info_has_next>, </#if>
	</#list>
</#macro>

<#macro hashMapLookupArgumentList variables>
	<#list variables as variable>
		<@hashMapLookupArgumentName variable/><#if variable_has_next>, </#if>
	</#list>
</#macro>

<#macro hashMapLookupArgumentName variable>
	${variable.identifier}_value<#t>
</#macro>

<#macro constraintListName constraint>
	${constraint.identifier}ConstraintList
</#macro>

<#macro constraintListType constraint>
	ConstraintLinkedList<<@global.constraintType constraint/>>
</#macro>

<#macro hashMapConstraintListType constraint>
	HashMapConstraintLinkedList<<@global.constraintType constraint/>>
</#macro>

<#macro hashMapType constraint>
	HashMap<Key, <@hashMapConstraintListType constraint/>>
</#macro>

<#macro hashMapStorageKeyName constraint, lookupCategory>
	${constraint.identifier}StorageKey<@global.lookupCategorySuffix lookupCategory/>
</#macro>

<#macro hashMapLookupKeyName constraint, lookupType>
	${constraint.identifier}LookupKey<@global.lookupTypeSuffix lookupType/>
</#macro>

<#macro indexName constraint, lookupCategory>
	${constraint.identifier}Index<@global.lookupCategorySuffix lookupCategory/>
</#macro>

<#macro doInitialisation type, name, access="private final">
	${access} ${type} ${name} = new ${type}();
</#macro>

<#macro solverList list=solvers>
	<#list list as solver>
		${solver.identifier}<#if solver_has_next>, </#if>
	</#list>
</#macro>

<#macro fullSolverList list=solvers>
	<#list list as solver>
		${solver.interface} ${solver.identifier}<#if solver_has_next>, </#if>
	</#list>
</#macro>

<#macro addHashObserver variables, observer>
	<#list variables as variable>
		<#if !variable.variableType.fixed>
			constraint.<@global.getVariable variable/>.addHashObserver(${observer});
		</#if>
	</#list>
</#macro>

<#macro getCollection constraint>
	get${constraint.identifier?cap_first}Constraints<#t>
</#macro>

<#macro collectionType constraint>
	Collection<<@global.constraintType constraint/>><#t>
</#macro>

<#macro unmodifiableCollection type>
	// This implementation is still very inefficient: 
	// don't over-use this feature yet!
	return new AbstractUnmodifiableCollection<${type}>() {
        @Override
        public int size() {
            return IteratorUtilities.size(iterator());
        }

        @Override
        public Iterator<${type}> iterator() {
            return <#nested/>;
        }
	};
</#macro>