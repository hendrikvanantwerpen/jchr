package be.kuleuven.jchr.compiler.CHRIntermediateForm.types;

import java.util.List;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IDeclarator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IInitialisator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.CoerceMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.IAssignable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Field;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Method;
import be.kuleuven.jchr.util.comparing.Comparable;



/**
 * @author Peter Van Weert
 */
public interface IType extends IAssignable, Comparable<IType> {
    public boolean isInterface();
    
    public List<CoerceMethod> getCoerceMethods();
    
    public IInitialisator getInitialisatorFrom(IType type)
        throws AmbiguityException;
    
    public IDeclarator getDeclarator()
        throws AmbiguityException;
    
    public IDeclarator getInitialisationDeclaratorFrom(IType type)
        throws AmbiguityException;
    
    public Field getField(String name) 
    	throws NoSuchFieldException, AmbiguityException;
    
    public Set<Method> getMethods(String id);
    
    public String toTypeString();
    
    public boolean isFixed();
    
    public boolean isHashObservable();
    public boolean isObservable();
}
