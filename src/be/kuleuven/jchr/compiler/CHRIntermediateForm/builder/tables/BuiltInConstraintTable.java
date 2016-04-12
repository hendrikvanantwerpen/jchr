package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.EQ;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.EQi;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.EQi2;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.GEQ;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.GEQi;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.GT;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.GTi;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.LEQ;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.LEQi;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.LEQi2;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.LT;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.LTi;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.NEQ;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.NEQi;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.ISolver.JAVA_SOLVER;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.ISolver.JAVA_SOLVER_IDENTIFIER;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType.BOOLEAN_TYPE;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType.BYTE_TYPE;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType.CHAR_TYPE;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType.DOUBLE_TYPE;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType.FLOAT_TYPE;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType.INT_TYPE;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType.LONG_TYPE;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType.SHORT_TYPE;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import be.kuleuven.jchr.annotations.JCHR_Asks;
import be.kuleuven.jchr.annotations.JCHR_Constraint;
import be.kuleuven.jchr.annotations.JCHR_Constraints;
import be.kuleuven.jchr.annotations.JCHR_Tells;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.Identifier;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.BuiltInConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.Assignment;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.Comparison;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.Equals;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.IJavaConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.JavaConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.CoerceMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.ISolver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;



/*
 * version 1.0.3
 *  - Assignments have become a special case, no longer the
 *      responsibility of the BuiltInConstraintTable. 
 */
/**
 * @author Peter Van Weert
 */
public class BuiltInConstraintTable extends ConstraintTable<Set<IBuiltInConstraint<?>>> {
    public BuiltInConstraintTable() {
        super();
        setIndex(new HashMap<String, BuiltInConstraintTable>());
        reset();
    }
    protected BuiltInConstraintTable(Object arg) {
        super();
    }
    
    private Map<String, BuiltInConstraintTable> index;
    
    public void registerSolver(Solver solver) throws IdentifierException {
        final BuiltInConstraintTable other = indexSolver(solver);
        registerComparings(solver, other);

        final JCHR_Constraints constraints = solver.getCHRconstraints();
        if (constraints != null) {
            final Map<String, JCHR_Constraint> map
            	= new HashMap<String, JCHR_Constraint>(constraints.value().length);
            
            for (JCHR_Constraint constraint : constraints.value())
                if (map.put(constraint.identifier(), constraint) != null)
                    throw new DuplicateIdentifierException(constraint.identifier());
    
            boolean asks;
            String name;
            JCHR_Constraint constraint;
            for (Method method : solver.getMethods()) {
                if (method.isAnnotationPresent(JCHR_Asks.class)) {
                    asks = true;
                    name = method.getAnnotation(JCHR_Asks.class).value();
                }
                else if (method.isAnnotationPresent(JCHR_Tells.class)) {
                    asks = false;
                    name = method.getAnnotation(JCHR_Tells.class).value();
                }
                else continue;
                
                constraint = map.get(name);
                if (constraint == null)
                    throw new IdentifierException(name + " is an unknown constraint");
                
                final IBuiltInConstraint biConstraint = 
                    new BuiltInConstraint(solver, method, name, asks);
                
                registerBiConstraintAlfa(name, constraint.infix(), constraint.arity(), asks, biConstraint, other);
            }
        }
        
        if (other.getSize() == 0) 
            throw new IllegalArgumentException(solver + " is not a valid solver!");
    }
    
    public void registerVariableType(VariableType type) {
        registerComparings(type.getType());
        
        // TODO ook hier werken met annotaties!!!
    }
    
    protected void registerComparings(Solver solver, BuiltInConstraintTable other) {
        final MatchingInfo info = 
            solver.getType().isAssignableTo(GenericType.getNonParameterizableInstance(Comparator.class));
        if (info.isExactMatch()) {
            try {
                for (Comparison comparing : Comparison.getInstances(solver))
                    registerBiConstraintAlfa(comparing.getPrefixIdentifier(), comparing.getIdentifier(), 2, true, comparing, other);
                
            } catch (IdentifierException e) {
                throw new RuntimeException();
            }
        }
    }
    
    protected void registerComparings(IType type) {
        final MatchingInfo info 
            = type.isAssignableTo(GenericType.getNonParameterizableInstance(Comparable.class));
        if (info.isExactMatch())
            for (Comparison comparing : Comparison.getInstances(type))
                registerJavaBiConstraint(comparing.getPrefixIdentifier(), comparing.getIdentifier(), true, comparing);
        else if (info.isNonInitMatch())
            for (CoerceMethod method : info.getCoerceMethods())
                registerComparings(method.getReturnType());
    }
    
    protected BuiltInConstraintTable indexSolver(ISolver solver) {
        final BuiltInConstraintTable result = new BuiltInConstraintTable(null);
        if (getIndex().put(solver.getIdentifier(), result) != null)
            throw new IllegalArgumentException(solver.getIdentifier() + " is already declared");
        return result;
    }
    
    public void registerAssignment(IType type) {
        registerJavaBiConstraint(null, "=", false, new Assignment(type));
    }
    
    /**
     * Registers the built-in constraint <code>biConstraint</code> with given
     * needed parameters (<code>identifier</code>, <code>infixIdentifier</code>, 
     * <code>arity</code> and <code>asks</code>) in <code>this</code>
     * (as in the implicit argument) built-in constraint table. 
     * 
     * @pre identifier != null || infixIdentifier != null
     * 
     * @param identifier
     *  The (prefix) identifier to be used for the built-in constraint. 
     *  This may be a null-reference indicating that the constraint cannot
     *  be written prefix.
     * @param infixIdentifier
     *  The infix identifier to be used for the built-in constraint. 
     *  This may be a null-reference indicating that the constraint cannot
     *  be written infix.
     * @param arity
     *  The arity of the constraint.
     * @param asks
     *  Indicates whether the registered constraint is a ask constraint
     *  or not (i.e. if false, this is a tell constraint).
     * @param biConstraint
     *  The built-in constraint to be registered.
     * @throws IdentifierException
     *  <code>identifier == null && infixIdentifier == null</code>
     */
    public void registerBiConstraint(String identifier, String infixIdentifier, int arity, boolean asks, IBuiltInConstraint biConstraint) 
    throws IdentifierException {
        registerBiConstraint(identifier, infixIdentifier, arity, asks, biConstraint, this);
    }
    
    /**
     * Registers the built-in constraint <code>biConstraint</code> with given
     * needed parameters (<code>identifier</code>, <code>infixIdentifier</code>, 
     * <code>arity</code> and <code>asks</code>) in both <code>this</code>
     * (as in the implicit argument) built-in constraint table and in the
     * <code>other</code> one (the final argument). 
     * 
     * @pre identifier != null || infixIdentifier != null
     * 
     * @param identifier
     *  The (prefix) identifier to be used for the built-in constraint. 
     *  This may be a null-reference indicating that the constraint cannot
     *  be written prefix.
     * @param infixIdentifier
     *  The infix identifier to be used for the built-in constraint. 
     *  This may be a null-reference indicating that the constraint cannot
     *  be written infix.
     * @param arity
     *  The arity of the constraint.
     * @param asks
     *  Indicates whether the registered constraint is a ask constraint
     *  or not (i.e. if false, this is a tell constraint).
     * @param biConstraint
     *  The built-in constraint to be registered.
     * @param other
     *  The built-in constraint table <code>biConstraint</code> has
     *  to be registered to, asides from <code>this</code> one 
     *  (the implicit argument). 
     * @throws IdentifierException
     *  <code>identifier == null && infixIdentifier == null</code>
     */
    protected void registerBiConstraintAlfa(String identifier, String infixIdentifier, int arity, boolean asks, IBuiltInConstraint<?> biConstraint, BuiltInConstraintTable other) 
    throws IdentifierException{
        registerBiConstraint(identifier, infixIdentifier, arity, asks, biConstraint, this);
        registerBiConstraint(identifier, infixIdentifier, arity, asks, biConstraint, other);
    }
    
    /**
     * Registers the built-in constraint <code>biConstraint</code> with given
     * needed parameters (<code>identifier</code>, <code>infixIdentifier</code>, 
     * <code>arity</code> and <code>asks</code>) in the given
     * built-in constraint table <code>table</code>. 
     * 
     * @pre identifier != null || infixIdentifier != null
     * 
     * @param identifier
     *  The (prefix) identifier to be used for the built-in constraint. 
     *  This may be a null-reference indicating that the constraint cannot
     *  be written prefix.
     * @param infixIdentifier
     *  The infix identifier to be used for the built-in constraint. 
     *  This may be a null-reference indicating that the constraint cannot
     *  be written infix.
     * @param arity
     *  The arity of the constraint.
     * @param asks
     *  Indicates whether the registered constraint is a ask constraint
     *  or not (i.e. if false, this is a tell constraint).
     * @param biConstraint
     *  The built-in constraint to be registered.
     * @param table
     *  The built-in constraint table <code>biConstraint</code> has
     *  to be registered to. 
     * @throws IdentifierException
     *  <code>identifier == null && infixIdentifier == null</code>
     */
    protected void registerBiConstraint(String identifier, String infixIdentifier, int arity, boolean asks, IBuiltInConstraint<?> biConstraint, BuiltInConstraintTable table) 
    throws IdentifierException {
        final boolean 
            identifierOK = Identifier.isValidUdSimpleIdentifier(identifier),
            infixOK = (infixIdentifier != null && infixIdentifier.length() > 0 && infixIdentifier.indexOf('?') < 0);

        if (infixOK && arity != 2)
            throw new IllegalArgumentException(biConstraint.getIdentifier()
                + " is not a binary constraint => cannot have infix notation"
            );
        
        if (identifierOK)
            table.declare(identifier, asks, arity, biConstraint, false);
        if (infixOK)
            table.declare(infixIdentifier, asks, arity, biConstraint, true);
        
        if (!identifierOK && !infixOK)
            throw new IdentifierException("No valid identifier was presented for this built-in constraint");
    }
    
    /*
     * version 1.0.3
     *  - Assignments have become a special case, no longer the
     *      responsibility of the BuiltInConstraintTable. 
     */
    public void registerJavaBiConstraints() {
        final String[][] askPairs = new String[][] {
            { GEQ , GEQi },
            { GT  , GTi  },
            { LT  , LTi  },
            { NEQ , NEQi }
        };
        final String[][] askTriplets = new String[][] {
            { EQ  , EQi2 , EQi}, 
            { LEQ , LEQi , LEQi2 }
        };
        
        final PrimitiveType[] comparableTypes = new PrimitiveType[] {
            BYTE_TYPE,
            CHAR_TYPE,
            DOUBLE_TYPE,
            FLOAT_TYPE,
            INT_TYPE,
            CHAR_TYPE,
            LONG_TYPE,
            SHORT_TYPE
        };
        
        for (PrimitiveType type : comparableTypes) {
            //registerAssignment(type);
            for (String[] triplet : askTriplets)
                registerJavaBiConstraintTriplet(triplet, type);
            for (String[] pair : askPairs)
                registerJavaBiConstraintPair(pair, type, true);
        }
        
        //registerAssignment(BOOLEAN_TYPE);        
        registerJavaBiConstraintTriplet(askTriplets[0], BOOLEAN_TYPE);
        registerJavaBiConstraintPair(askPairs[3], BOOLEAN_TYPE, true);
        
        registerJavaBiConstraint(EQ, EQi, true, Equals.getInstance());
    }
    
    private void registerJavaBiConstraintTriplet(String[] triplet, PrimitiveType type) {
        final JavaConstraint constraint = new JavaConstraint(type, triplet[1], true); 
        registerJavaBiConstraint(triplet[0], triplet[1], true, constraint);
        registerJavaBiConstraint(triplet[0], triplet[2], true, constraint);
    }
    
    private void registerJavaBiConstraintPair(String[] pair, PrimitiveType type, boolean asks) {
        registerJavaBiConstraint(pair[0], pair[1], asks, new JavaConstraint(type, pair[1], asks));
    }
    
    protected void registerJavaBiConstraint(String identifier, String infixIdentifier, boolean asks, IJavaConstraint constraint) {
        try {
            registerBiConstraint(identifier, infixIdentifier, 2, asks, constraint, getJavaBiConstraintTable());
        } catch (IdentifierException e) {
            throw new InternalError();
        }
    }
    
    protected BuiltInConstraintTable getJavaBiConstraintTable() {
        return getIndex().get(JAVA_SOLVER_IDENTIFIER);
    }
    
    protected void declare(String name, boolean asks, int arity, IBuiltInConstraint<?> constraint, boolean infix) {
        declare(createBiConstraintId(name, asks, arity), constraint, infix);
    }
    
    protected void declare(String id, IBuiltInConstraint<?> constraint, boolean infix) {
        try {
            Set<IBuiltInConstraint<?>> list = get(id, infix);
            if (list == null) 
                list = declare(id, new HashSet<IBuiltInConstraint<?>>(), infix);
            list.add(constraint);
            
        } catch (DuplicateIdentifierException die) {
            throw new RuntimeException();
        }
    }
    
    public Set<IBuiltInConstraint<?>> get(String id, boolean asks, int arity, boolean infix) {
        return get(createBiConstraintId(id, asks, arity), infix);
    }
    
    public Set<IBuiltInConstraint<?>> getJavaConstraints(String id, boolean asks, boolean infix) {
        return get(JAVA_SOLVER, id, asks, 2, infix);
    }
    
    public Set<IBuiltInConstraint<?>> get(ISolver solver, String id, boolean asks, int arity, boolean infix) {
        return getIndex().get(solver.getIdentifier()).get(id, asks, arity, infix);
    }
    
    public final static String createBiConstraintId(String constraintId, boolean asks, int arity) {
        return (asks? "ask" : "tell") + constraintId + '/' + arity;
    }

    protected Map<String, BuiltInConstraintTable> getIndex() {
        return index;
    }
    protected void setIndex(Map<String, BuiltInConstraintTable> index) {
        this.index = index;
    }
    
    @Override
    public void reset() {
        super.reset();
        getIndex().clear();
        indexSolver(JAVA_SOLVER);
        registerJavaBiConstraints();
    }
}