package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java;

import java.lang.reflect.TypeVariable;
import java.util.Comparator;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.IntArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.Arguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Method;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.reflect.Reflection;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.TypeParameter;


/*
 * Version 1.3.1    (Peter Van Weert)
 *  - Since solver "cannot be coerced" in the private getInstances method
 *      if (type.isAssignableTo(comparator).isMatch())
 *    was changed to
 *      if (type.isDirectlyAssignableTo(comparator))
 *  - Related: the following test was superfluous since solvers cannot be
 *    immutable Java wrappers!
 *      if (comparator.isImmutableJavaWrapper()) return new Comparison[0];
 *  - String, Boolean, BigInteger and BigDecimal are to be compared
 *      using the Comparable interface!
 *  - equals-methodes zijn er!
 */
public abstract class Comparison extends JavaConstraint {
    
    private String prefixIdentifier;
    
    private final static Comparison[] NO_COMPARISONS = new Comparison[0];

    protected Comparison(IType argType, String infix, String prefix) {
        super(argType, infix, true);
        setPrefixIdentifier(prefix);
    }
    
    /**
     * @pre ``solver.getType() extends java.util.Comparator''
     */
    public static Comparison[] getInstances(Solver solver) {
        return getInstances(solver, solver.getType());
    }
    /*
     * Allow recursion in case the solver is a TypeParameter
     * (we still need the original solver object) 
     */
    private static Comparison[] getInstances(Solver solver, IType comp) {
        if (comp instanceof TypeParameter) {
            final GenericType comparator = GenericType.getInstance(Comparator.class);
            for (IType type : ((TypeParameter)comp).getUpperBounds())
                if (type.isDirectlyAssignableTo(comparator))
                    return getInstances(solver, type);
            throw new IllegalArgumentException("This solver isn't a comparator!");
        }
        else {  // comp instanceof GenericType
            final GenericType comparator = (GenericType)comp;
            
            final Comparison[] result = new Comparison[5];
            final IType argType = Reflection.reflect(comparator, ComparatorComparison.TYPE_VARIABLE);
            result[0] = new ComparatorComparison(argType, LTi,  LT,  solver);
            result[1] = new ComparatorComparison(argType, GTi,  GT,  solver);
            result[2] = new ComparatorComparison(argType, LEQi, LEQ, solver);
            result[3] = new ComparatorComparison(argType, LEQi2,LEQ, solver);
            result[4] = new ComparatorComparison(argType, GEQi, GEQ, solver);
            return result;
        }
    }
    
    /**
     * @pre ``comp extends java.lang.Comparable''
     */
    public static Comparison[] getInstances(IType comp) {
        if (comp instanceof TypeParameter) {
            for (IType type : ((TypeParameter)comp).getUpperBounds())
                if (type.isAssignableTo(GenericType.getInstance(Comparable.class)).isMatch())
                    return getInstances(type);
            throw new IllegalArgumentException("This type isn't comparable!");
        }
        else {  // comparable instanceof GenericType
            final GenericType comparable = (GenericType)comp;
            
            if (comparable.isComparablePrimitiveWrapper()) 
                return NO_COMPARISONS;
            
            final Comparison[] result = new Comparison[5];
            final IType argType = Reflection.reflect(comparable, ComparableComparison.TYPE_VARIABLE);
            result[0] = new ComparableComparison(argType, LTi, LT);
            result[1] = new ComparableComparison(argType, GTi, GT);
            result[2] = new ComparableComparison(argType, LEQi, LEQ);
            result[3] = new ComparableComparison(argType, LEQi2, LEQ);
            result[4] = new ComparableComparison(argType, GEQi, GEQ);
            return result;
        }
    }

    /**
     * Returns the prefix identifier of this Comparing, whereas
     * the <code>getIdentifier()</code> method returns the
     * infix identifier.
     * 
     * @return The prefix identifier of this Comparing, whereas
     *  the <code>getIdentifier()</code> method returns the
     *  infix identifier.
     * 
     * @see JavaConstraint#getIdentifier()
     */
    public String getPrefixIdentifier() {
        return prefixIdentifier;
    }
    protected void setPrefixIdentifier(String prefix) {
        this.prefixIdentifier = prefix;
    }
    
    private static class ComparableComparison extends Comparison {
        protected final static TypeVariable TYPE_VARIABLE = Comparable.class.getTypeParameters()[0];
        protected final static Method COMPARE_TO;
        
        static {
            Method result = null;
            for (java.lang.reflect.Method m : Comparable.class.getMethods())
                if (m.getName().equals("compareTo")) {
                    result = new Method(m);
                    break;
                }
            if (result == null) throw new InternalError();
            COMPARE_TO = result;
        }
        
        private ComparableComparison(IType argType, String infix, String prefix) {
            super(argType, infix, prefix);
        }
        
        @Override
        public JavaConjunct getInstance(IArguments arguments) {
            return super.getInstance(
                new Arguments(COMPARE_TO.getInstance(arguments), IntArgument.ZERO)
            );
        }
        
        @Override
        public boolean equals(JavaConstraint other) {
            return (other instanceof ComparableComparison) 
                && super.equals(other);
        }
    }
    
    private static class ComparatorComparison extends Comparison {
        protected final static TypeVariable TYPE_VARIABLE = Comparator.class.getTypeParameters()[0];
        protected final static Method COMPARE_TO;
        
        static {
            Method result = null;
            for (java.lang.reflect.Method m : Comparator.class.getMethods())
                if (m.getName().equals("compare")) {
                    result = new Method(m);
                    break;
                }
            if (result == null) throw new InternalError();
            COMPARE_TO = result;
        }
        
        private Solver implicitArgument;
        
        private ComparatorComparison(IType argType, String infix, String prefix, Solver solver) {
            super(argType, infix, prefix);
            setImplicitArgument(solver);
        }
        
        @Override
        public JavaConjunct getInstance(IArguments arguments) {
            arguments.addImplicitArgument(getImplicitArgument());
            return super.getInstance(
                new Arguments(COMPARE_TO.getInstance(arguments), IntArgument.ZERO)
            );
        }

        protected Solver getImplicitArgument() {
            return implicitArgument;
        }
        protected void setImplicitArgument(Solver implicitArgument) {
            this.implicitArgument = implicitArgument;
        }
        
        @Override
        public boolean equals(JavaConstraint other) {
            return (other instanceof ComparatorComparison)
                && this.equals(other);
        }
        
        public boolean equals(ComparatorComparison other) {
            return super.equals(other)
                && this.getImplicitArgument().equals(other.getImplicitArgument());
        }
    }
}