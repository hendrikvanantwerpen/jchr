package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IBasicArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.IScheduleComponent;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.ScheduleComponentType;

public abstract class Lookup implements IScheduleComponent, IBasicArgumented {

    public ScheduleComponentType getScheduleComponentType() {
        return ScheduleComponentType.LOOKUP;
    }
    
    public abstract Occurrence getOccurrence();
    
    public abstract ILookupType getLookupType();
    
}
