package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;

/**
 * @author Peter Van Weert
 */
public class ByteArgument extends ConstantArgument<Byte> {    
    public ByteArgument(byte value) {
        this(Byte.valueOf(value));
    }
    public ByteArgument(Byte value) {
        super(ArgumentType.BYTE, value);
    }
    
    @Override
    public IType getType() {
        return PrimitiveType.BYTE_TYPE;
    }
}
