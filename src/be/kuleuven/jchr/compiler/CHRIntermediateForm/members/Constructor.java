package be.kuleuven.jchr.compiler.CHRIntermediateForm.members;

import java.util.Collections;

import be.kuleuven.jchr.annotations.JCHR_Declare;
import be.kuleuven.jchr.annotations.JCHR_Init;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.StringArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.Arguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.EmptyArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IInitialisator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IInitialisatorInvocation;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.Initialisator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.reflect.Reflection;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.util.comparing.Comparison;


import static be.kuleuven.jchr.util.comparing.Comparison.*;


public class Constructor extends Initialisator<Constructor> {
    
    private java.lang.reflect.Constructor<?> constructor;
    
    private GenericType base;
    
    private int identifierIndex;
    
    public Constructor(GenericType base, java.lang.reflect.Constructor constructor) {
        setBase(base);
        setConstructor(constructor);
        initIdentifierIndex();
    }
    
    public boolean isConstructor() {
        return true;
    }

    public IType getType() {
        return getBase();
    }
    protected GenericType getBase() {
        return base;
    }
    protected void setBase(GenericType base) {
        this.base = base;
    }

    protected boolean isInitialisator() {
        return getConstructor().isAnnotationPresent(JCHR_Init.class);
    }
    protected boolean isDeclarator() {
        return getConstructor().isAnnotationPresent(JCHR_Declare.class);
    }
    
    protected void initIdentifierIndex() {
        if (isInitialisator())
            identifierIndex = getConstructor().getAnnotation(JCHR_Init.class).identifier();
        else if (isDeclarator())
            identifierIndex = (getArity() == 1)? 0 : -1;
        else 
            identifierIndex = -1;
    }
    public int getIdentifierIndex() {
        if (!isInitialisator() && !isDeclarator())
            throw new UnsupportedOperationException();
        else
            return identifierIndex;
    }
    protected void setIdentifierIndex(int identifierIndex) {
        this.identifierIndex = identifierIndex;
    }
    @Override
    public boolean usesIdentifier() {
        if (!isInitialisator() && !isDeclarator())
            throw new UnsupportedOperationException();
        else
            return getIdentifierIndex() >= 0;
    }

    public ConstructorInvocation getInstance(IArgument argument) {
        return getInstance(argument, null);
    }
    public ConstructorInvocation getInstance(IArgument argument, String identifier) {
        IArguments arguments = new Arguments(usesIdentifier()? 2 : 1);
        arguments.addArgument(argument);
        
        if (usesIdentifier()) {
            if (identifier == null) 
                identifier = Variable.createIdentifier();
            arguments.addArgumentAt(getIdentifierIndex(), new StringArgument(identifier));
        }
        
        return getInstance(arguments);
    }

    public ConstructorInvocation getInstance(IArguments arguments) {
        return new ConstructorInvocation(this, arguments);
    }

    public IType getFormalParameterAt(int index) {
        return Reflection.reflect(getBase(), getConstructor().getGenericParameterTypes()[index]);
    }
    
    public int getArity() {
        return getConstructor().getParameterTypes().length;
    }

    public java.lang.reflect.Constructor<?> getConstructor() {
        return constructor;
    }
    protected void setConstructor(java.lang.reflect.Constructor<?> constructor) {
        this.constructor = constructor;
    }
    public String getTypeString() {
        return getType().toTypeString();
    } 
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Constructor) 
            && equals((Constructor)obj);  
    }
    
    public boolean equals(Constructor other) {
        return (this == other) || ( other != null
            && this.getConstructor().equals(other.getConstructor())
        );
    }
    
    public static Constructor getDeclaratorInitialisator(GenericType base) 
    throws AmbiguityException {
        Constructor result = null;
        
        for (Constructor constructor : base.getConstructors()) {
            if (constructor.isValidDeclarationInitialisator()) {                    
                if (result == null)
                    result = constructor;
                else {
                    if (result.usesIdentifier() && !constructor.usesIdentifier())
                        continue;
                    if (!result.usesIdentifier() && constructor.usesIdentifier())
                        result = constructor;
                    else
                        throw new AmbiguityException();
                }
            }
        }
    
        return result;
    }
    
    public static Constructor getInitialisatorFrom(GenericType base, IType type)
    throws AmbiguityException {
        Constructor best = null;
        boolean ambiguous = false;
        
        for (Constructor constructor : base.getConstructors()) {
            if (constructor.isInitialisator()) {
                if (constructor.isValidInitialisatorFrom(type)) {                    
                    if (best == null)
                        best = constructor;
                    else {                    
                        switch (constructor.compareTo( (IInitialisator)best)) {
                            case BETTER:
                                ambiguous = false;
                                best = constructor;
                            break;
                            
                            case EQUAL:
                            case AMBIGUOUS:
                                ambiguous = true;
                            break;
                        }
                    }
                }
            }
        }
        
        if (ambiguous) throw new AmbiguityException();
        
        return best;
    }
    
    @Override
    public String toString() {
        return "new " + getType().toTypeString() + super.toString();
    }
    
    public boolean isValidInitialisator() {
        return isInitialisator() 
            && Initialisator.hasValidIdentifierParameter(this)
            && getArity() == (usesIdentifier()? 2 : 1);
    }
    
    public boolean isValidDeclarationInitialisator() {
        return isDeclarator()
            && Initialisator.hasValidIdentifierParameter(this)
            && getArity() == (usesIdentifier()? 1 : 0);
    }
    
    public boolean haveToIgnoreImplicitArgument() {
        return false; // heeft er eenvoudigweg geen
    }
    
    public IInitialisatorInvocation<Constructor> getInstance() {
        return getInstance((String)null);
    }
    public IInitialisatorInvocation<Constructor> getInstance(String identifier) {
        if (usesIdentifier()) {            
            if (identifier == null) identifier = Variable.createIdentifier();
            return getInstance(new Arguments(Collections.singletonList((IArgument)new StringArgument(identifier))));
        }
        else return getInstance(EmptyArguments.getInstance());
    }
}
