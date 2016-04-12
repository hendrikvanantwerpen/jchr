package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;

/**
 * @author Peter Van Weert
 */
public class ShortArgument extends ConstantArgument<Short> {
    public ShortArgument(short value) {
        this(Short.valueOf(value));
    }
    public ShortArgument(Short value) {
        super(ArgumentType.SHORT, value);
    }
    
    @Override
    public IType getType() {
        return PrimitiveType.SHORT_TYPE;
    }
}
