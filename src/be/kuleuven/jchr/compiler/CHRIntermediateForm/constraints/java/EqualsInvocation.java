package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.ScheduleComponentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.MethodInvocation;

public class EqualsInvocation 
    extends MethodInvocation<Equals>
    implements IJavaConjunct<Equals> {

    public EqualsInvocation(Equals constraint, IArguments arguments) {
        super(constraint, arguments);
    }

    @Override
    public boolean canBeAskConjunct() {
        return true;
    }

    @Override
    public ScheduleComponentType getScheduleComponentType() {
        return ScheduleComponentType.GUARD;
    }
    
    public String getIdentifier() {
        return IBuiltInConstraint.EQ;
    }
}