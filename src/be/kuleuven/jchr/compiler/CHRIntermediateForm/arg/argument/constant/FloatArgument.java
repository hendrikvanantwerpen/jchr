package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;

/**
 * @author Peter Van Weert
 */
public class FloatArgument extends ConstantArgument<Float> {
    public FloatArgument(float value) {
        this(Float.valueOf(value));
    }
    public FloatArgument(Float value) {
        super(ArgumentType.FLOAT, value);
    }
    
    @Override
    public IType getType() {
        return PrimitiveType.FLOAT_TYPE;
    }
}
