package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;

/**
 * @author Peter Van Weert
 */
public class LongArgument extends ConstantArgument<Long> {
    public LongArgument(long value) {
        this(Long.valueOf(value));
    }
    public LongArgument(Long value) {
        super(ArgumentType.LONG, value);
    }
    
    @Override
    public IType getType() {
        return PrimitiveType.LONG_TYPE;
    }
}
