package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.Argumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;

public abstract class ConstraintConjunct<T extends IConstraint<?>> 
    extends Argumented<T> 
    implements IConstraintConjunct<T> {

    public ConstraintConjunct(T constraint, IArguments arguments) {
        super(constraint, arguments);
    }
    
    public String getIdentifier() {
        return getArgumentableType().getIdentifier();
    }
}
