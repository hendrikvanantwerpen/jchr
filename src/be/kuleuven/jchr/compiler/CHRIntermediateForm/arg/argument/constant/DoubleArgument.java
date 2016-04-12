package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;

/**
 * @author Peter Van Weert
 */
public class DoubleArgument extends ConstantArgument<Double> {
    public DoubleArgument(double value) {
        this(Double.valueOf(value));
    }
    public DoubleArgument(Double value) {
        super(ArgumentType.DOUBLE, value);
    }
    
    @Override
    public IType getType() {
        return PrimitiveType.DOUBLE_TYPE;
    }
}
