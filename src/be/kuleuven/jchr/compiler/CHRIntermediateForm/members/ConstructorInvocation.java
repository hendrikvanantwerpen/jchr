package be.kuleuven.jchr.compiler.CHRIntermediateForm.members;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.Argumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IInitialisatorInvocation;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;

public class ConstructorInvocation 
    extends Argumented<Constructor> 
    implements IInitialisatorInvocation<Constructor> {
    
    public ConstructorInvocation(Constructor type, IArguments arguments) {
        super(type, arguments);
    }

    public String getTypeString() {
        return getArgumentableType().getTypeString();
    }
    
    public ArgumentType getArgumentType() {
        return ArgumentType.CONSTRUCTOR_INVOCATION;
    }

    public IType getType() {
        return getArgumentableType().getType();
    }

    public MatchingInfo isAssignableTo(IType type) {
        return getType().isAssignableTo(type);
    }

    public boolean isDirectlyAssignableTo(IType type) {
        return getType().isDirectlyAssignableTo(type);
    }
    
    public boolean isFixed() {
        return getType().isFixed();
    }
}
