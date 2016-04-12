package be.kuleuven.jchr.compiler.CHRIntermediateForm.types.parameterizable;

import java.util.Arrays;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;



/**
 * @author Peter Van Weert
 */
public class Parameterizable implements IParameterizable {
    
    public Parameterizable(int nbParameters) {
        setTypeParameters(new IType[nbParameters]);
    }
    
    private IType[] typeParameters;
    
    public IType getTypeParameterAt(int index) {
        return typeParameters[index];
    }
    public IType[] getTypeParameters() {
        return typeParameters;
    }
    protected void setTypeParameters(IType[] typeParameters) {
        this.typeParameters = typeParameters;
    }
    public int getNbTypeParameters() {
        int result;
        for (result = 0; result < getTypeParameters().length; result++)
            if (getTypeParameterAt(result) == null) return result;
        return result;
    }
    public boolean hasTypeParameters() {
        return getNbTypeParameters() > 0;
    }
    
    public void addTypeParameter(IType typeParameter) 
    throws IllegalArgumentException, IndexOutOfBoundsException {
        putTypeParameterAt(typeParameter, getNbTypeParameters());
    }
    public void putTypeParameterAt(IType typeParameter, int index)    
    throws IllegalArgumentException, IndexOutOfBoundsException {
        getTypeParameters()[index] = typeParameter;
        
        if (! hasValidTypeParameterAt(index))
            throw new IllegalArgumentException(
                typeParameter.toString() + " @ index " + index
            );
    }
    public boolean hasValidTypeParameterAt(int index) 
    throws IndexOutOfBoundsException {
        
        return isValidTypeParameter(getTypeParameterAt(index));
    }
    
    public boolean isValid() {
        return getNbTypeParameters() 
    		== getTypeParameters().length;
    }
    
    public boolean isValidTypeParameter(IType typeParameter) {
        return (typeParameter != null);
    }
    
    @Override
    public int hashCode() {        
        int result = 23;
        for (IType typeParameter : getTypeParameters())            
            result = 37 * result + 
            	((typeParameter == null)? 0 : typeParameter.hashCode());
            
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {        
        return (this == obj) 
        	|| ((obj instanceof Parameterizable)
        	&& equals((Parameterizable)obj));
    }
        
    public boolean equals(Parameterizable other) {
        return Arrays.deepEquals(
                this.getTypeParameters(),
                other.getTypeParameters()
            );
    }
}
