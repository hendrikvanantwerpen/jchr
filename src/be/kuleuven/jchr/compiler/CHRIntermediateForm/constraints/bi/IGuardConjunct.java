package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.IConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.IScheduleComponent;

public interface IGuardConjunct
extends IConjunct, IScheduleComponent {

    public boolean canBeAskConjunct();
}
