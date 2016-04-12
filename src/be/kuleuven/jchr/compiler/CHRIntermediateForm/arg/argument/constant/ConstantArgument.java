package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.Argument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;

/**
 * @author Peter Van Weert
 */
public abstract class ConstantArgument<Value> extends Argument {

    public ConstantArgument(ArgumentType type, Value value) {
        super(type);
        setValue(value);
    }

    private Value value;

    public Value getValue() {
        return value;
    }

    protected void setValue(Value value) {
        this.value = value;
    }

    public abstract IType getType();

    public boolean isFixed() {
        return true;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}