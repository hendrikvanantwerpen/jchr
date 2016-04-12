package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;

/**
 * @author Peter Van Weert
 */
public class IntArgument extends ConstantArgument<Integer> {
    public final static IntArgument ZERO = new IntArgument(0);
    
    public IntArgument(int value) {
        this(Integer.valueOf(value));
    }
    public IntArgument(Integer value) {
        super(ArgumentType.INT, value);
    }
    
    @Override
    public IType getType() {
        return PrimitiveType.INT_TYPE;
    }
}
