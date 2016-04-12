package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument;

import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Field;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Method;


/**
 * @author Peter Van Weert
 */
public interface IImplicitArgument extends IArgument {

    public Set<Method> getMethods(String id);
    
    public Field getField(String name) 
    	throws AmbiguityException, NoSuchFieldException;
}
