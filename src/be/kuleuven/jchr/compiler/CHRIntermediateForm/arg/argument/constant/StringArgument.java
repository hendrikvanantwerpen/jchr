package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;

/**
 * @author Peter Van Weert
 */
public class StringArgument extends ConstantArgument<String> {
    public StringArgument(String value) {
        super(ArgumentType.STRING, value);
    }
    
    @Override
    public IType getType() {
        return GenericType.getNonParameterizableInstance(String.class);
    }
    
    @Override
    public String toString() {
        return '"' + getValue() + '"';
    }
}
