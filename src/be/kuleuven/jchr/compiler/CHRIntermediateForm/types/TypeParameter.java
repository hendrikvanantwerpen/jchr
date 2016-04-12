package be.kuleuven.jchr.compiler.CHRIntermediateForm.types;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo.EXACT_MATCH;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo.NO_MATCH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.Identifier;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IDeclarator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IInitialisator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.CoerceMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Field;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Method;


/**
 * @author Peter Van Weert
 *
 */
public class TypeParameter extends Type {

    private String identifier;
    
    private List<IType> upperBounds;
    
    private List<CoerceMethod> coerceMethods;
    
    public TypeParameter(String identifier) throws IllegalIdentifierException {
        Identifier.testSimpleIdentifier(identifier);
        setIdentifier(identifier);
        setUpperBounds(new ArrayList<IType>());
    }    
    
    public List<IType> getUpperBounds() {
        return upperBounds;
    }
    public IType getUpperBound(int index) {
        return getUpperBounds().get(index);
    }
    protected void setUpperBounds(List<IType> upperBounds) {
        this.upperBounds = upperBounds;
    }
    public void addUpperBound(IType upperBound) {
        if (hasUpperBounds() && !upperBound.isInterface())
            throw new IllegalArgumentException("The additional bound " + upperBound + " is not an interface type");
        if (getNbUpperBounds() == 1 && getUpperBound(0) instanceof TypeParameter)
            throw new IllegalArgumentException("A type parameter may not be followed by other bounds");
            
        getUpperBounds().add(upperBound);
    }
    public int getNbUpperBounds() {
        return getUpperBounds().size();
    }
    public boolean hasUpperBounds() {
        return getNbUpperBounds() > 0;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    protected void setIdentifier(String name) {
        this.identifier = name;
    }
    
    public boolean isInterface() {
        return hasUpperBounds() && getUpperBounds().get(0).isInterface();
    }
    
    public MatchingInfo isAssignableTo(IType other) {
        if (other == this) return EXACT_MATCH;
        if (! hasUpperBounds())
            return TypeFactory.getInstance(Object.class).isAssignableTo(other);

        MatchingInfo best = NO_MATCH, temp;
        
        for (IType bound : getUpperBounds()) {
            if (best.isExactMatch()) return EXACT_MATCH;
            temp = bound.isAssignableTo(other);
            
            switch (temp.compareTo(best)) {
                case BETTER:
                    best = temp;
                break;
                
                case EQUAL:
                case AMBIGUOUS:
                    best.setAmbiguous();
                break;                
            }
        }
                        
        return best;
    }
    
    public boolean isDirectlyAssignableTo(IType other) {
        if (this.equals(other)) return true;
        
        if (! hasUpperBounds())
            return TypeFactory.getInstance(Object.class).isDirectlyAssignableTo(other);
        
        for (IType bound : getUpperBounds())
            if (bound.isDirectlyAssignableTo(other))
                return true;
            
        return false;
    }
    
    public boolean isHashObservable() {
        for (IType bound : getUpperBounds())
            if (bound.isHashObservable())
                return true;
        return false;
    }
    public boolean isObservable() {
        for (IType bound : getUpperBounds())
            if (bound.isObservable())
                return true;
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getIdentifier());
        if (hasUpperBounds()) {
            result.append(" extends ");
	        for (IType bound : upperBounds) {
	            result.append(bound.toTypeString());
	            result.append(" & ");
	        }	        
	        result.delete(result.length() - 3, result.length());
        }
        return result.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        return (this == other) ||
            ((other instanceof TypeParameter) && this.equals((TypeParameter)other));
    }

    @Override
    public int hashCode() {
        // aanname dat uniekheid van naam extern wordt gegarandeerd
        return 37 * 23 + getIdentifier().hashCode();
    }
    
    public boolean equals(TypeParameter other) {
        // aanname dat uniekheid van naam extern wordt gegarandeerd
        return this.getIdentifier().equals(other.getIdentifier());
    }
    
    public List<CoerceMethod> getCoerceMethods() {
        // eens het als variabletype wordt gebruikt is het niet
        // onredelijk te verwachten dat deze info nog wel eens
        // zal worden opgevraagd ==> cachen?
        if (coerceMethods == null) initCoerceMethods();
        return coerceMethods;
    }
    
    public IInitialisator<?> getInitialisatorFrom(IType type) throws AmbiguityException {
        IInitialisator<?> result = null, temp;
        boolean ambiguous = false;
        
        for (IType bound : getUpperBounds()) {
            temp = bound.getInitialisatorFrom(type);
            if (result == null)
                result = temp;
            else switch (temp.compareTo(result)) {
                case BETTER:
                    ambiguous = false;
                    result = temp;
                break;
                
                case EQUAL:
                case AMBIGUOUS:
                    ambiguous = true;
                break;
            }
        }
               
        if (ambiguous) throw new AmbiguityException();
        
        return result;
    }
    
    public IDeclarator getDeclarator() throws AmbiguityException {
        IDeclarator result = null, temp;
        
        for (IType bound : getUpperBounds()) {
            temp = bound.getDeclarator();
            if (temp != null) {
                if (result != null)
                    throw new AmbiguityException();
                else
                    result = temp;
            }
        }
        
        return result;
    }
    
    public IDeclarator getInitialisationDeclaratorFrom(IType type) throws AmbiguityException {
        IDeclarator result = null, temp;
        
        for (IType bound : getUpperBounds()) {
            temp = bound.getInitialisationDeclaratorFrom(type);
            if (temp != null) {
                if (result != null)
                    throw new AmbiguityException();
                else
                    result = temp;
            }
        }
        
        return result;
    }
    
    public void initCoerceMethods() {
        final List<CoerceMethod> list;
        
        if (!hasUpperBounds())
            list = Collections.emptyList();
        else {
            list = new ArrayList<CoerceMethod>();
        
            for (IType bound : getUpperBounds())
                list.addAll(bound.getCoerceMethods());
        }
        
        setCoerceMethods(list);
    }
    
    protected void setCoerceMethods(List<CoerceMethod> coerceMethods) {
        this.coerceMethods = coerceMethods;
    }
    
    public boolean isFixed() {
        return hasUpperBounds() && getUpperBound(0).isFixed();
    }
    
    public Field getField(String name) 
    throws NoSuchFieldException, AmbiguityException {
        
        Field result = null, temp;
        for (IType bound : getUpperBounds()) 
	        try {
	            temp = bound.getField(name);
	            if (result != null && ! result.equals(temp))
	                throw new AmbiguityException(this + "." + name);
	            else
	                result = temp;
	            
	        } catch (NoSuchFieldException snfe) {
	            // NOP
	        }

        if (result == null)
            throw new NoSuchFieldException(name);
        else
            return result;
    }
    
    public Set<Method> getMethods(String id) {
        Set<Method> result = new HashSet<Method>();
        for (IType bound : getUpperBounds())
            result.addAll(bound.getMethods(id));
        return result;
    }
    
    @Override
    public String toTypeString() {
        return getIdentifier();
    }
}