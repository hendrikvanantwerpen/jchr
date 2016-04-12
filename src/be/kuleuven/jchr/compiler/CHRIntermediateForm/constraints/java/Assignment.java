package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;

public class Assignment extends JavaConstraint {
    final public static String ID  = "="; 
    
    public Assignment(IType argumentType) {
        super(argumentType, ID, false);
    }
    
    @Override
    public boolean isEquality() {
        return true;
    }
    
    /*
     * version 1.0.3
     *  - In case of an assignment the type of the first argument
     *      should be equal to the formal type, and not some
     *      supertype.
     */
    @Override    
    public MatchingInfo canHaveAsArgumentAt(int index, IArgument argument) {
        if (index == 0) 
            return MatchingInfo.valueOf(
                (argument instanceof Variable) &&
                // argument.isDirectlyAssignableTo(getFormalParameterAt(0))
                argument.getType().equals(getFormalParameterAt(0))
            );
        else // index == 1
            return super.canHaveAsArgumentAt(index, argument);
    }
    
    @Override
    public AssignmentConjunct getInstance(IArguments arguments) {
        return new AssignmentConjunct(this, arguments);
    }
        
    @Override
    public boolean equals(JavaConstraint other) {
        return (other instanceof Assignment)
            && super.equals(other);
    }
}
