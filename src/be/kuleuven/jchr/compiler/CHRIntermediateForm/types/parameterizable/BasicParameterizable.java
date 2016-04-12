package be.kuleuven.jchr.compiler.CHRIntermediateForm.types.parameterizable;

import java.lang.reflect.TypeVariable;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.reflect.Mirror;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.reflect.Reflection;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;



/**
 * @author Peter Van Weert
 */
public abstract class BasicParameterizable extends Parameterizable implements Mirror {
    private Class<?> rawType;
    
    public BasicParameterizable(Class<?> theClass) {
        this(GenericType.getNbParameters(theClass), theClass);
    }
    
    protected BasicParameterizable(int nbParameters, Class<?> theClass) {
        super(nbParameters);
        setRawType(theClass);
    }
    
    public TypeVariable[] getTypeVariables() {
        return getRawType().getTypeParameters();
    }
    
    public TypeVariable getTypeVariableAt(int index) {
        return getTypeVariables()[index];
    }
    
    public int getNbTypeVariables() {
        return getTypeVariables().length;
    }

    @Override
    public boolean hasValidTypeParameterAt(int index) {        
        if (! super.hasValidTypeParameterAt(index))
            return false;

        final IType typeParameter = getTypeParameterAt(index);
        
        for (java.lang.reflect.Type bound : getTypeVariableAt(index).getBounds())            
            if (! typeParameter.isAssignableTo(Reflection.reflect(this, bound)).isNonAmbiguousMatch())
                return false;

        return true;
    }
    
    public Class<?> getRawType() {
        return rawType;
    }
    protected void setRawType(Class<?> rawType) {
        this.rawType = rawType;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getRawType().toString());
        if (hasTypeParameters()) {
            result.append('<');
            for (IType typeParameter : getTypeParameters())
                result.append(typeParameter);
            result.append('>');
        }
        return result.toString();
    }
    
    @Override
    public int hashCode() {        
        return 37 * super.hashCode() + getRawType().hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        return (this == other)
        	|| ((other instanceof BasicParameterizable)
        	&& this.equals((BasicParameterizable)other));         
    }
    
    public boolean equals(BasicParameterizable other) {
        return (this == other)
        	|| (this.getRawType().equals(other.getRawType())
        	&& super.equals(other));
    }
}