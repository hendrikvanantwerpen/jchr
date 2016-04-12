package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;

/**
 * @author Peter Van Weert
 */
public class BooleanArgument extends ConstantArgument<Boolean> {

    private BooleanArgument(Boolean value) {
        super(ArgumentType.BOOLEAN, value);
    }

    private static BooleanArgument trueInstance;
    private static BooleanArgument falseInstance;
    public static BooleanArgument getInstance(boolean value) {
        if (value) 
            return getTrueInstance();
        else
            return getFalseInstance();
    }
    public static BooleanArgument getTrueInstance() {
        if (trueInstance == null)
            trueInstance = new BooleanArgument(Boolean.TRUE);
        return trueInstance;
    }
    public static BooleanArgument getFalseInstance() {
        if (falseInstance == null)
            falseInstance = new BooleanArgument(Boolean.FALSE);
        return falseInstance;
    }
    
    @Override
    public IType getType() {
        return PrimitiveType.BOOLEAN_TYPE;
    }   
}
