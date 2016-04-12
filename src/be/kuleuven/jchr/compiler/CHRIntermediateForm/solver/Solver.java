package be.kuleuven.jchr.compiler.CHRIntermediateForm.solver;

import java.lang.reflect.Method;
import java.util.Comparator;

import be.kuleuven.jchr.annotations.JCHR_Constraints;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;



/**
 * @author Peter Van Weert
 */
public class Solver extends GenericType implements IImplicitArgument, ISolver {
    
    public Solver(Class<?> solverClass) {
        super(solverClass);        
    }
    
    private String identifier;
    
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public boolean hasIdentifier() {
        return getIdentifier() != null;
    }
    
    public JCHR_Constraints getCHRconstraints() {
        return getRawType().getAnnotation(JCHR_Constraints.class);
    }
    
    public Method[] getMethods() {
        return getRawType().getMethods();
    }
    
    @Override
    protected void setRawType(Class<?> solverClass) {
        if (! isValidSolverClass(solverClass))
            throw new IllegalArgumentException(solverClass + 
                    " is not a valid JCHR built-in solver class");
        
        super.setRawType(solverClass);
    }
    
    public final static boolean isValidSolverClass(Class<?> theClass) {
        return (theClass != null) 
            && (theClass.isAnnotationPresent(JCHR_Constraints.class)
                || Comparator.class.isAssignableFrom(theClass)
            );
    }
    
    public IType getType() {
        return this;
    }
    
    public ArgumentType getArgumentType() {
        return ArgumentType.SOLVER;
    }
    
    @Override
    public boolean isFixed() {
        return true;
    }
    
    @Override
    public String toString() {
        return getIdentifier();
    }
}
