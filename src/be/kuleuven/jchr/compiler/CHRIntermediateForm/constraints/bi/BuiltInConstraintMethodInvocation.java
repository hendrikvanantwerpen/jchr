package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.MethodInvocation;

public class BuiltInConstraintMethodInvocation extends MethodInvocation<BuiltInConstraint> 
implements IBuiltInConjunct<BuiltInConstraint>{

    public BuiltInConstraintMethodInvocation(BuiltInConstraint constraint, IArguments arguments) {
        super(constraint, arguments);
    }
    
    public String getIdentifier() {
        return getArgumentableType().getIdentifier();
    }
    
    public boolean isEquality() {
        return getArgumentableType().isEquality();
    }
}
