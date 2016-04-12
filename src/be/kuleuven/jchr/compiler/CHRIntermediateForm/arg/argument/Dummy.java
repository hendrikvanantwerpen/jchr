package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;

public abstract class Dummy extends Argument {

    protected Dummy(ArgumentType type) {
        super(type);
    }
    
    public final static Dummy getOneInstance(final VariableType variableType) {
        return new Dummy(ArgumentType.ONE_DUMMY) {
            public IType getType() {
                return variableType.getType();
            }
        };
    }
    
    public final static Dummy getOtherInstance(final IType type) {
        return new Dummy(ArgumentType.OTHER_DUMMY) {
            public IType getType() {
                return type;
            }
        };
    }
    
    public boolean isFixed() {
        // Niet de bedoeling dat een dummy hiervoor wordt gebruikt!
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return getArgumentType().toString() + " (" + getType().toTypeString() + ')';
    }
}