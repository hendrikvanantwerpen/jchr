package be.kuleuven.jchr.compiler.CHRIntermediateForm.init;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;
import be.kuleuven.jchr.util.IndexOutOfBoundsException;


public class WrapperInitialisator<T extends IInitialisator<?>> extends Initialisator<T> {
    private IInitialisator<T> initialisator;
    
    private GenericType wrapperType;
    
    private PrimitiveType primitiveType;
    
    public WrapperInitialisator(
        IInitialisator<T> initialisator, 
        GenericType wrapperType, 
        PrimitiveType primitiveType
    ) {
        setInitialisator(initialisator);
        setWrapperType(wrapperType);
        setPrimitiveType(primitiveType);
    }

    public boolean isConstructor() {
        return getInitialisator().isConstructor();
    }

    public IType getType() {
        return getInitialisator().getType();
    }

    public int getIdentifierIndex() {
        return getInitialisator().getIdentifierIndex();
    }

    @Override
    public boolean usesIdentifier() {
        return getInitialisator().usesIdentifier();
    }

    public int getArity() {
        return getInitialisator().getArity();
    }

    protected IInitialisator<T> getInitialisator() {
        return initialisator;
    }
    protected void setInitialisator(IInitialisator<T> initialisator) {
        this.initialisator = initialisator;
    }

    public IInitialisatorInvocation<T> getInstance(IArgument argument, String identifier) {
        return getInitialisator().getInstance(wrapArgument(argument), identifier);
    }    
    public IInitialisatorInvocation<T> getInstance(IArgument argument) {
        return getInitialisator().getInstance(wrapArgument(argument));
    }

    @Override
    public IType[] getFormalParameters() {
        IType[] result = new IType[getArity()];
        result[getArgumentIndex()] = getPrimitiveType();
        if (usesIdentifier())
            result[getIdentifierIndex()] = GenericType.getNonParameterizableInstance(String.class);
        return result;
    }
    
    public IType getFormalParameterAt(int index) {
        if (index == getIdentifierIndex()) return GenericType.getNonParameterizableInstance(String.class);
        if (index == getArgumentIndex()) return getPrimitiveType();
        throw new IndexOutOfBoundsException(index);
    }
    
    protected IArgument wrapArgument(IArgument argument) {
        try {
            return getWrapperType().getInitialisatorFrom(getPrimitiveType()).getInstance(argument);
            
        } catch (AmbiguityException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    
    public IArgumented<T> getInstance(IArguments arguments) {
        int index = getArgumentIndex();
        arguments.replaceArgumentAt(index, 
            wrapArgument(arguments.getArgumentAt(index))
        );
        return getInitialisator().getInstance(arguments);
    }

    protected GenericType getWrapperType() {
        return wrapperType;
    }
    protected void setWrapperType(GenericType wrapperType) {
        this.wrapperType = wrapperType;
    }

    protected PrimitiveType getPrimitiveType() {
        return primitiveType;
    }
    protected void setPrimitiveType(PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }
    
    @Override
    public String toString() {
        return "Wrapper: " + getInitialisator().toString() + " wraps " + getPrimitiveType();
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof WrapperInitialisator)
            && this.equals((WrapperInitialisator)obj);
    }
    
    public boolean equals(WrapperInitialisator other) {
        return (this == other) || ( other != null
            && this.getInitialisator().equals(other.getInitialisator())
            && this.getPrimitiveType().equals(other.getPrimitiveType())
            && this.getWrapperType().equals(other.getWrapperType())
        );
    }
    
    public boolean haveToIgnoreImplicitArgument() {
        return getInitialisator().haveToIgnoreImplicitArgument();
    }

    public IInitialisatorInvocation<T> getInstance() {
        throw new UnsupportedOperationException();
    }

    public IInitialisatorInvocation<T> getInstance(String identifier) {
        throw new UnsupportedOperationException();
    }

    public boolean isValidInitialisator() {
        return getInitialisator().isValidInitialisator();
    }

    public boolean isValidDeclarationInitialisator() {
        return false;
    }
    
}
