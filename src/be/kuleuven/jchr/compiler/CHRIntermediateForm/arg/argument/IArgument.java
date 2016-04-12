package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.IAssignable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;

/**
 * @author Peter Van Weert
 */
public interface IArgument extends IAssignable {
    public ArgumentType getArgumentType();
    
    public IType getType();
    
    public boolean isFixed();
}