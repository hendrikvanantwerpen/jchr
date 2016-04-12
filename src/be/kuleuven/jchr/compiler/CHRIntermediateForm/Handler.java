package be.kuleuven.jchr.compiler.CHRIntermediateForm;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.TypeParameter;



/**
 * @author Peter Van Weert
 *
 */
public class Handler {
    private String identifier;
    
    private List<TypeParameter> typeParameters; 
    
    public Handler(String identifier) throws IllegalIdentifierException {
        Identifier.testUdSimpleIdentifier(identifier);
        setIdentifier(identifier);
        setTypeParameters(new ArrayList<TypeParameter>());
    }
    
    public String getIdentifier() {
        return identifier;
    }
    protected void setIdentifier(String name) {
        this.identifier = name;
    }
    
    public void addTypeParameter(TypeParameter typeParameter) 
    throws DuplicateIdentifierException {        
        if (hasAsTypeParameter(typeParameter.getIdentifier()))
            throw new DuplicateIdentifierException(typeParameter.getIdentifier());
        typeParameters.add(typeParameter);
    }
    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }    
    public int getNbTypeParameters() {
        return getTypeParameters().size();
    }
    public boolean hasTypeParameters() {
        return getNbTypeParameters() > 0;
    }
    public TypeParameter getTypeParameter(String name) {
        for (TypeParameter typeParameter : getTypeParameters())
            if (typeParameter.getIdentifier().equals(name))
                return typeParameter;
        return null;
    }
    public boolean hasAsTypeParameter(String name) {
        return (getTypeParameter(name) != null);
    }
    protected void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }
}
