package be.kuleuven.jchr.compiler.CHRIntermediateForm.types.parameterizable;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;


/**
 * @author Peter Van Weert
 */
public interface IParameterizable {
    public IType getTypeParameterAt(int index);

    public IType[] getTypeParameters();

    public int getNbTypeParameters();

    public void addTypeParameter(IType typeParameter)
    	throws IllegalArgumentException, IndexOutOfBoundsException, AmbiguityException;
    
    public boolean hasTypeParameters();
    
    public boolean hasValidTypeParameterAt(int index)
    	throws IndexOutOfBoundsException, AmbiguityException;

    public void putTypeParameterAt(IType typeParameter, int index)
    throws IllegalArgumentException, IndexOutOfBoundsException, AmbiguityException;
    
    public boolean isValid();
}