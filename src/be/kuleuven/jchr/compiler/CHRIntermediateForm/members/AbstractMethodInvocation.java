package be.kuleuven.jchr.compiler.CHRIntermediateForm.members;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.AbstractMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.Argumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.IConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;

/**
 * @author Peter Van Weert
 */
public abstract class AbstractMethodInvocation<T extends AbstractMethod<?>> 
extends Argumented<T>
implements IArgument, IConjunct {
    
    /**
     * @param type
     * @param arguments
     * @param assignmentInfos
     */
    public AbstractMethodInvocation(T type, IArguments arguments) {
        super(type, arguments);
    }
    
    public String getMethodName() {
        return getArgumentableType().getName();
    }
    
    public ArgumentType getArgumentType() {
        return ArgumentType.METHOD_INVOCATION;
    }
    
    public IType getType() {
        return getArgumentableType().getReturnType();
    }
    
    public boolean canBeArgument() {
        return getArgumentableType().isValidArgument(); 
    }
    
    public boolean canBeAskConjunct() {
        return getArgumentableType().returnsBoolean();
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
    
    @Override
    public String toString() {
        return '[' + super.toString() + " ReturnType: " + getType() + ']';
    }
}