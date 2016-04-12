package be.kuleuven.jchr.compiler.analysis.scheduling;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType.HASH_MAP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.EmptyArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.BinaryGuardedLookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.DefaultLookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.util.builder.BasicBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;



public class HashMapLookupBuilder 
    extends BasicBuilder<be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.Lookup> 
    implements ILookupBuilder {
    
    private List<IArgumented<?>> guardConjuncts;
    
    public HashMapLookupBuilder() {
        setGuardConjuncts(new ArrayList<IArgumented<?>>());
    }

    @Override
    public void init() throws BuilderException {
        getGuardConjuncts().clear();
        setResult(new Lookup());
    }
    
    /**
     * @inheritDoc
     */
    public void setCurrentPartner(Occurrence occurrence) throws BuilderException {
        if (hasPartnerOccurrence())
            throw new BuilderException("Partner occurrence allready set");
        else
            setPartnerOccurrence(occurrence);
    }
    
    /**
     * @inheritDoc
     */
    public void addGuard(IGuardConjunct guard) throws BuilderException {
        getGuardConjuncts().add((IArgumented<?>)guard);
    }
    
    /**
     * @inheritDoc
     */
    public boolean canAddGuard(IGuardConjunct guard) throws BuilderException {     
        if (! (guard instanceof IBuiltInConjunct)) 
            return false;   // very reasonable assumption
        if (! (guard instanceof IArgumented))
            return false;   // idem (guard should've been sheduled first!)
        
        IArgumented<? extends IBuiltInConstraint<?>> argumented = 
            (IArgumented<? extends IBuiltInConstraint<?>>)guard;
        
        if (!argumented.getArgumentableType().isEquality()) 
            return false;   // only equality guards in a hash index (for now??) 
        
        // Equality guards are (currently) always binary guards 
        if (argumented.getArityIgnoringImplicitArgument() != 2)
            return false;
        
        Set<Variable> variables = getCurrentVariables();
        
        IArgument
            arg0 = argumented.getArgumentIgnoringImplicitAt(0),
            arg1 = argumented.getArgumentIgnoringImplicitAt(1);        
        final boolean 
            var0 = variables.contains(arg0),
            var1 = variables.contains(arg1),
            ok0 = var0 && ((Variable)arg0).canBeUsedInHashMap(),
            ok1 = var1 && ((Variable)arg1).canBeUsedInHashMap();
        
        // If one of both arguments is a variable in the current 
        // partner occurrence that can be used in a hash map
        // *and* the other argument is *not* also a variable
        // in this occurrence, then it is OK
        // TODO:: What about coerced variables?
        return (ok0 && !var1) || (ok1 && !var0);
    }
    
    @Override
    public void abort() throws BuilderException {
        getGuardConjuncts().clear();
    }

    @Override
    public void finish() throws BuilderException {
        if (! hasGuardConjuncts())
            finishDefaultLookup();
        else
            finishHashMapLookup();
    }
    
    protected void finishDefaultLookup() throws BuilderException {
        getUserDefinedConstraint().ensureDefaultLookupCategory();
        setLookupType(DefaultLookupType.getInstance());
        setArguments(EmptyArguments.getInstance());
    }
    
    protected void finishHashMapLookup() throws BuilderException {
        addAllBinaryGuardsToLookupType(
            getUserDefinedConstraint().ensureLookupCategory(
                HASH_MAP, getHashMapVariableIndices()
            )
        );
    }
        
    /*
     * Deze methode zal alle guards toevoegen aan een lookupType,
     * dat wordt ingesteld als het type van de huidige lookup...
     * 
     * @pre alle guards zijn binary...
     */
    protected void addAllBinaryGuardsToLookupType(ILookupCategory lookupCategory) throws BuilderException {
        final int[] variableIndices = lookupCategory.getVariableIndices();
        final int size = variableIndices.length;
        
        BinaryGuardedLookupType lookupType 
            = new BinaryGuardedLookupType(lookupCategory, size);
        getLookup().setNbArguments(size);
        
        IArgumented<?> guard;
        for (int i = 0; i < size; i++) {
            guard = getGuardConjunctAt(i);
            
            if (guard.getArityIgnoringImplicitArgument() != 2
                ||(!addBinaryGuardToLookupType(lookupType, guard, variableIndices[i], 0)
                && !addBinaryGuardToLookupType(lookupType, guard, variableIndices[i], 1))
               )
                throw new BuilderException();
        }
        
        clearGuardConjuncts();
        setLookupType(lookupType.getUniqueInstance());
    }
    
    protected boolean addBinaryGuardToLookupType(BinaryGuardedLookupType lookupType, IArgumented<?> guard, int variableIndex, int argumentIndex) {
        if (guard.getArgumentIgnoringImplicitAt(argumentIndex) == getVariableAt(variableIndex)) {
            final int x = (argumentIndex + 1) % 2; 
            lookupType.addGuard(guard, x);
            getLookup().addArgument(guard.getArgumentIgnoringImplicitAt(x));
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @return Voor iedere guard een index van de variabele van de beschouwde 
     *  occurrence waarop de guard test. Deze zullen dan gebruikt worden
     *  om de variabelelijst voor een lookupCategory vast te leggen. De
     *  argumenten worden van links naar rechts gecontroleerd, maar er is
     *  wel een voorkeur voor fixed variabelen...
     * @throws BuilderException
     *  Als er een guard is die niet test op een variabele uit de occurrence,
     *  wat uiteraard niet zo mogen kunnen gebeuren.
     */
    protected int[] getHashMapVariableIndices() throws BuilderException {
        final int size = getNbGuardConjuncts();
        final int[] result = new int[size];
        final int INITIAL_VALUE = -1;
        Arrays.fill(result, INITIAL_VALUE);
        final List<IArgument> variables = getCurrentArguments();
        
        IArgumented<?> guard;
        
        int index;
outer:  for (int i = 0; i < size; i++) {
            guard = getGuardConjunctAt(i);
            
            for (IArgument argument : guard) {
                index = variables.indexOf(argument);
                if (index >= 0) {
                    if (argument.isFixed()) {
                        result[i] = index;
                        continue outer;
                    }
                    else if (result[i] == INITIAL_VALUE 
                            && getVariableAt(index).isHashObservable())
                        result[i] = index;
                }
            }
            
            if (result[i] == INITIAL_VALUE)
                throw new BuilderException("Illegal guard added to lookup: " + guard);
        }
        
        return result;
    }
    
    protected Lookup getLookup() {
        try {
            return (Lookup)super.getResult();
        } catch (BuilderException be) {
            throw new RuntimeException();
        }
    }
    protected void setLookupType(ILookupType lookupType) {
        getLookup().setLookupType(lookupType);        
    }
    protected void setArguments(IArguments arguments) {
        getLookup().setArguments(arguments);
    }
    protected void setPartnerOccurrence(Occurrence occurrence) {
        getLookup().setOccurrence(occurrence);
    }
    protected Occurrence getPartnerOccurrence() {
        return getLookup().getOccurrence();
    }
    protected UserDefinedConstraint getUserDefinedConstraint() {
        return getPartnerOccurrence().getArgumentableType();
    }
    protected boolean hasPartnerOccurrence() {
        return getPartnerOccurrence() != null;
    }
    protected Set<Variable> getCurrentVariables() {
        return getPartnerOccurrence().getVariablesInArguments();
    }
    /*
     * Note that this will in fact return the same collection of
     * variables as <code>getCurrentVariables</code>
     * 
     * @see #getCurrentVariables()
     */
    protected List<IArgument> getCurrentArguments() {
        return getPartnerOccurrence().getArgumentList();
    }
    protected Variable getVariableAt(int index) {
        return (Variable)getCurrentArguments().get(index);
    }
    
    public Collection<IGuardConjunct> getRemainingGuards() throws BuilderException {
        if (hasGuardConjuncts())
            throw new BuilderException("Not all guards got included in the lookup!");
        
        return Collections.emptyList();
    }

    protected List<IArgumented<?>> getGuardConjuncts() {
        return guardConjuncts;
    }
    protected void clearGuardConjuncts() {
        getGuardConjuncts().clear();        
    }
    protected IArgumented<?> getGuardConjunctAt(int index) {
        return getGuardConjuncts().get(index);
    }
    protected void setGuardConjuncts(List<IArgumented<?>> guardConjuncts) {
        this.guardConjuncts = guardConjuncts;
    }
    protected boolean hasGuardConjuncts() {
        return !getGuardConjuncts().isEmpty();
    }
    protected int getNbGuardConjuncts() {
        return getGuardConjuncts().size();
    }
}