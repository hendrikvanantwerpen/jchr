package be.kuleuven.jchr.compiler.CHRIntermediateForm.reflect;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;

/**
 * Dit omvat de essentie van een geparameteriseerd type, en is al
 * wat nodig is voor de klasse Reflection om zijn werk te doen.
 * 
 * @author Peter Van Weert
 */
public interface Mirror {
    public Class<?> getRawType();
    
    public IType getTypeParameterAt(int index);
}
