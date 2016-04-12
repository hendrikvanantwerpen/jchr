package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;

/**
 * @author Peter Van Weert
 */
public class CharArgument extends ConstantArgument<Character> {
    public CharArgument(char value) {
        this(Character.valueOf(value));
    }
    public CharArgument(Character value) {
        super(ArgumentType.CHAR, value);
    }
    
    @Override
    public IType getType() {
        return PrimitiveType.CHAR_TYPE;
    }
    
    @Override
    public String toString() {
        return "'" + getValue() + "'";
    }
}
