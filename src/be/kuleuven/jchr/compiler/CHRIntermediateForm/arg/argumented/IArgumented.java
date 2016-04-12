package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented;

import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.IArgumentable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;


/**
 * @author Peter Van Weert
 */
public interface IArgumented<T extends IArgumentable<?>> 
extends IBasicArgumented, IArguments, IArgumentable<T> {
    
    public T getArgumentableType();
    
    public boolean hasAsArgumentableType(T type);
    
    public Set<Variable> getVariablesInArguments();
    
    public int getNbVariableArguments();
    
    public int getArityIgnoringImplicitArgument();
    public IArgument getArgumentIgnoringImplicitAt(int index);
}