package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.Argumentable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.IArgumentable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.Constraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.DefaultLookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.LookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.types.DefaultLookupCategories;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.types.ILookupCategories;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.types.LookupCategories;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;
import be.kuleuven.jchr.util.comparing.Comparison;



/**
 * @author Peter Van Weert
 */
public class UserDefinedConstraint extends Constraint<UserDefinedConstraint> {
    
    protected final static int INITIAL_ARITY_CAPACITY = 4;    	
    
    private List<Variable> variables;
    
    private LinkedList<Occurrence> occurrences;
    
    private ILookupCategories lookupTypes;
    
    private String infix;
    
    public UserDefinedConstraint(String id) {
        this(id, null);
    }
    
    public UserDefinedConstraint(String id, String infix) {
        super(id);
        setInfix(infix);
        setArguments(new ArrayList<Variable>(INITIAL_ARITY_CAPACITY));
        setOccurrences(new LinkedList<Occurrence>());
        setLookupCategories(DefaultLookupCategories.getInstance());
    }
    
    void addOccurrence(Occurrence occurrence) {
        final Rule rule = occurrence.getRule();            
        
        for (
            ListIterator<Occurrence> iterator = occurrences.listIterator();        
            iterator.hasNext();
        ) {
            if (iterator.next().getRule().equals(rule)) {   // zoek de eerste occurrence uit dezelfde regel
                if (iterator.hasPrevious()) {               // en voeg HIERVOOR de nieuwe in
                    iterator.previous();
                    iterator.add(occurrence);
                }
                else
                    occurrences.addFirst(occurrence);
                
                return;                                     // resultaat: de occurrences uit een regel staan van achter naar voor!  
            }
        }
        
        occurrences.addLast(occurrence);                      // als er zit nog geen occurrence in zit van dezelfde regel: ACHTERAAN!
        
                                                            // eindresultaat: regels in volgorde, occurrences in omgekeerde volgorde
    }
    
    void removeOccurrence(Occurrence occurrence) {
        getOccurrences().remove(occurrence);
    }
    
    public List<Occurrence> getOccurrences() {
        return occurrences;
    }
    public Occurrence getOccurrenceAt(int index) {
        return getOccurrences().get(index);
    }
    public int getNbOccurrences() {
        return getOccurrences().size();
    }
    public int getNbActiveOccurrences() {
        int result = 0;
        for (Occurrence occurrence : getOccurrences())
            if (! occurrence.isPassive()) result++;
        return result;
    }
    public int getIndexOf(Occurrence occurrence) {
        List<Occurrence> occurrences = getOccurrences();
        
        final int size = occurrences.size();
        for (int i = 0; i < size; i++)             
            if (occurrences.get(i) == occurrence) return i;
         
        throw new RuntimeException();
    }
    public boolean hasAsOccurrence(Occurrence occurrence) {
        for (Occurrence o : getOccurrences())             
            if (occurrence == o) return true;
        return false;
    }
    protected void setOccurrences(LinkedList<Occurrence> occurrences) {
        this.occurrences = occurrences;
    }
    
    public Set<Rule> getRules() {
        final Set<Rule> result = new HashSet<Rule>();
        for (Occurrence occurrence : getOccurrences())
            result.add(occurrence.getRule());
        return result;
    }

    public String getVariableIdAt(int index) {
        return getVariableAt(index).getIdentifier();
    }
    public Variable getVariableAt(int index) {
        return getVariables().get(index);
    }
    public VariableType getVariableTypeAt(int index) {
        return getVariableAt(index).getVariableType();
    }
    public Variable getVariableWith(String id) {
        for (Variable result : getVariables())
            if (result.getIdentifier().equals(id))
                return result;
        return null;
    }
    public boolean hasVariableWith(String id) {
        return getVariableWith(id) != null;
    }   
    public List<Variable> getVariables() {
        return variables;        
    }    
    public void addVariable(Variable variable) 
    	throws DuplicateIdentifierException {
        
        if (hasVariableWith(variable.getIdentifier()))
            throw new DuplicateIdentifierException(variable.getIdentifier());
        getVariables().add(variable);
    }
    
    protected void setArguments(List<Variable> variables) {
        this.variables = variables;
    }
    
    public int getArity() {
        return getVariables().size();
    }
    public IType getFormalParameterAt(int index)
            throws ArrayIndexOutOfBoundsException {
        return getVariableAt(index).getType();
    }    
    public IType[] getFormalParameters() {
        IType[] result = new IType[getArity()];
        for (int i = 0; i < getArity(); i++)
            result[i] = getFormalParameterAt(i);
        return result;
    }
    
    public UserDefindedConjunct getInstance(IArguments arguments) {
        return new UserDefindedConjunct(this, arguments);
    }
    
    public IArgumented<UserDefinedConstraint> getInstance(IArguments arguments, MatchingInfos infos) {
        arguments.incorporate(infos, false);
        return getInstance(arguments);
    }
    
    public void newInstance(Rule rule, IArguments args, boolean removed) {
        // de dubbele bindingen worden door deze constructor geregeld:
        new Occurrence(rule, this, args, removed);
    }
    
    public boolean isAskConstraint() {
        return false;
    }
    public boolean isEquality() {
        return false;
    }
    
    public boolean haveToIgnoreImplicitArgument() {
        return false;   // heeft er geen!
    }

    public MatchingInfos canHaveAsArguments(IArguments arguments) {
        return Argumentable.canHaveAsArguments(this, arguments);
    }

    public MatchingInfo canHaveAsArgumentAt(int index, IArgument argument) {
        return argument.getType().isAssignableTo(getVariableAt(index).getType());
    }

    public Comparison compareTo(IArgumentable other) {
        return Argumentable.compare(this, other);
    }
    
    public ILookupCategories getLookupCategories() {
        return lookupTypes;
    }
    public int getNbLookupCategories() {
        return getLookupCategories().getNbLookupCategories();
    }
    protected void setLookupCategories(ILookupCategories lookupTypes) {
        this.lookupTypes = lookupTypes;
    }
    public int getIndexOf(ILookupCategory lookupCategory) {
        return getLookupCategories().getIndexOf(lookupCategory);
    }
    public void ensureDefaultLookupCategory() {
        ensureNonDefaultLookupCategories();
        final ILookupCategories lookupCategories = getLookupCategories();
        final ILookupCategory defaultLookupCategory = DefaultLookupCategory.getInstance();
        if (! lookupCategories.contains(defaultLookupCategory))
            lookupCategories.addLookupCategory(defaultLookupCategory);
    }
    /**
     * Makes sure a lookup category with given type (not NONE) and
     * variable indices is present in the current collection of
     * lookup categories. This method conveniently also returns this
     * lookup category.
     * This of course means that first the collection of lookup-
     * categories has to be an optimised one (and not the default
     * collection).
     * 
     * @pre indexType != IndexType.NONE
     */
    public ILookupCategory ensureLookupCategory(IndexType indexType, int[] variableIndices) {
        assert indexType != IndexType.NONE;

        ILookupCategory result = getLookupCategory(indexType, variableIndices);
        
        if (result == null) {
            result = new LookupCategory(indexType, variableIndices);
            addLookupCategory(result);
        }
        
        return result;
    }
    /**
     * Adds a given lookup category to the current collection
     * of lookup categories. 
     * Also makes sure that the current lookup-category is no longer
     * the default one, but an optimised (possibly empty) 
     * collection of lookup categories.
     */
    protected void addLookupCategory(ILookupCategory lookupCategory) {
        ensureNonDefaultLookupCategories();
        getLookupCategories().addLookupCategory(lookupCategory);
    }
    public ILookupCategory getLookupCategory(IndexType indexType, int[] variableIndices) {
        return getLookupCategories().getLookupCategory(indexType, variableIndices);
    }
    public boolean hasMasterLookupCategory() {
        for (ILookupCategory category : getLookupCategories())
            if (category.isMasterCategory()) return true;
        return false;
    }
    
    protected void ensureNonDefaultLookupCategories() {
        if (getLookupCategories() == DefaultLookupCategories.getInstance())
            setLookupCategories(new LookupCategories());
    }

    /**
     * Returns the infix identifier of this user defined constraint
     * (<code>null</code> if undefined).
     * 
     * @return The infix identifier of this user defined constraint
     *  (<code>null</code> if undefined).
     */
    public String getInfix() {
        return infix;
    }

    /**
     * Sets the infix identifier of this user defined constraint
     * (<code>null</code> if undefined).
     * 
     * @param infix 
     *  The infix identifier of this user defined constraint
     *  (<code>null</code> if undefined).
     */
    public void setInfix(String infix) {
        this.infix = infix;
    }
}