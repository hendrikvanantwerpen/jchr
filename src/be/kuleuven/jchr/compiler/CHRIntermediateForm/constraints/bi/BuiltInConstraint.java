package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.BuiltInMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;

/**
 * @author Peter Van Weert
 */
public class BuiltInConstraint extends BuiltInMethod<BuiltInConstraint> implements IBuiltInConstraint<BuiltInConstraint> {

    private String identifier;
    
    private boolean isAskConstraint;
    
    /*
     * base is 
     * 	- solver ==> hasDefaultImplicitArgument !
     * 	- GenericType van de variabele 	==> ! hasDefaultImplicitArgument
     *  - of null als statisch ==> hasDefaultImplicitArgument !
     */

    public BuiltInConstraint(java.lang.reflect.Method method, String identifier, boolean isAskConstraint) {
        super(method);
        init(identifier, isAskConstraint);
    }
    
    public BuiltInConstraint(Solver solver, java.lang.reflect.Method method, String identifier, boolean isAskConstraint) {
	    super(solver, method);
        init(identifier, isAskConstraint);
    }
  
    public BuiltInConstraint(GenericType base, java.lang.reflect.Method method, String identifier, boolean isAskConstraint) {
	    super(base, method);
        init(identifier, isAskConstraint);
    }
    
    protected void init(String identifier, boolean isAskConstraint) {
        setIdentifier(identifier);
        setAskConstraint(isAskConstraint);
        
        if (isAskConstraint() && ! returnsBoolean())
            throw new IllegalArgumentException("Illegal ask constraint: method cannot be used for an ask constraint");
    }
    
    
    @Override
    public boolean equals(Object obj) {
        return (obj == this) ||
        	((obj instanceof BuiltInConstraint)
        		&& ((BuiltInConstraint)obj).getIdentifier().equals(getIdentifier())
        		&& super.equals((BuiltInConstraint)obj));
    }
    
    public String getIdentifier() {
        return identifier;
    }
    protected void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public boolean isAskConstraint() {
        return isAskConstraint;
    }
    protected void setAskConstraint(boolean isAskConstraint) {
        this.isAskConstraint = isAskConstraint;
    }
    
    public BuiltInConstraintMethodInvocation getInstance(IArguments arguments) {
        return new BuiltInConstraintMethodInvocation(this, arguments);
    }
    
    public boolean isEquality() {
        // vergeet de haakjes rond isAskConstraint etc nooit meer!!!
        return getIdentifier().equals(EQ)
            || (isAskConstraint()
                ? getIdentifier().equals("==") 
                : getIdentifier().equals("=")); 
    }
}
