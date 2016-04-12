package be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions;

import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.IArgumentable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;


/**
 * @author Peter Van Weert
 *
 */
public class IllegalArgumentsException extends ArgumentsException {
    private static final long serialVersionUID = 1L;
    
    public IllegalArgumentsException(IArgumentable argumentable, IArguments arguments) {
        super(argumentable + " cannot have as arguments " + arguments);
    }
    
    public <T extends IArgumentable<?>> IllegalArgumentsException(Set<T> argumentables, IArguments arguments) {
        super("None of " + argumentables + " can have " + arguments + " as arguments");
    }
}