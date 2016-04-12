package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.AbstractMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.BuiltInMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;

public class Equals extends BuiltInMethod<Equals> implements IJavaConstraint<Equals> {

    private Equals() throws NoSuchMethodException {
        super(
            GenericType.getNonParameterizableInstance(Object.class),
            Object.class.getMethod("equals", new Class[] { Object.class } )
        );
    }
    
    private static Equals instance;
    public static Equals getInstance() {
        if (instance == null)
            try {
                instance = new Equals();
            } catch (NoSuchMethodException e) {
                throw new InternalError();
            }
        return instance;
    }
    
    public String getIdentifier() {
        return EQ;
    }
    public boolean isAskConstraint() {
        return true;
    }
    public boolean isEquality() {
        return true;
    }
    public EqualsInvocation getInstance(IArguments arguments) {
        return new EqualsInvocation(this, arguments);
    }
    
    @Override
    public boolean equals(Object other) {
        return this == other;
    }
    
    @Override
    public boolean equals(AbstractMethod other) {
        return this == other;
    }
}