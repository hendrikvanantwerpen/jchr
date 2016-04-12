package be.kuleuven.jchr.compiler.CHRIntermediateForm.members;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.AbstractMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.ScheduleComponentType;

public class MethodInvocation<T extends AbstractMethod<?>> 
extends AbstractMethodInvocation<T> 
implements IGuardConjunct {
    public MethodInvocation(T type, IArguments arguments) {
        super(type, arguments);
    }
    
    public ScheduleComponentType getScheduleComponentType() {
        return ScheduleComponentType.GUARD;
    }
}