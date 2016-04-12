package be.kuleuven.jchr.compiler.CHRIntermediateForm.init;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.AbstractMethodInvocation;

public class InitialisatorMethodInvocation 
extends AbstractMethodInvocation<InitialisatorMethod> 
implements IInitialisatorInvocation<InitialisatorMethod> {

    public InitialisatorMethodInvocation(InitialisatorMethod type, IArguments arguments) {
        super(type, arguments);
    }
}
