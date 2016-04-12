package be.kuleuven.jchr.compiler.CHRIntermediateForm.init;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;

public interface IInitialisatorInvocation<T extends IInitialisator> 
extends IArgumented<T>, IArgument {
    // no new methods
}
