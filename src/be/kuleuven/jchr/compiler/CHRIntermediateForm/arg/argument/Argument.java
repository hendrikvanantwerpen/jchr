package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;

/**
 * @author Peter Van Weert
 */
public abstract class Argument implements IArgument {

    private final ArgumentType type;

    public Argument(ArgumentType type) {
        this.type = type;
    }

    @Override
    public abstract String toString();
    
    public ArgumentType getArgumentType() {
        return type;
    }
    
    public MatchingInfo isAssignableTo(IType other) {
        return getType().isAssignableTo(other);
    }
    public boolean isDirectlyAssignableTo(IType other) {
        return getType().isDirectlyAssignableTo(other);
    }
}