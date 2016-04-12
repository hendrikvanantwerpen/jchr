package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.Argument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;

/**
 * @author Peter Van Weert
 */
public class NullArgument extends Argument {
    private NullArgument() {
        super(ArgumentType.NULL);
    }
    
    private static NullArgument instance;
    public static NullArgument getInstance() {
        if (instance == null)
            instance = new NullArgument();
        return instance;
    }

    @Override
    public String toString() {
        return "null";
    }
    
    public IType getType() {
        throw new NullPointerException("Unknown argument type: null");
    }
    
    public boolean isFixed() {
        return true;
    }
    
    
    @Override
    public MatchingInfo isAssignableTo(IType other) {
        return GenericType.getNonParameterizableInstance(Object.class).isAssignableTo(other);
    }
}
