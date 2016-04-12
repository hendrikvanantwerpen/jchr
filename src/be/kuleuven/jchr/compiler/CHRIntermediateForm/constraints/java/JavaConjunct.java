package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ConstraintConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.ScheduleComponentType;

public class JavaConjunct 
extends ConstraintConjunct<JavaConstraint> 
implements IJavaConjunct<JavaConstraint> {
    
    public JavaConjunct(JavaConstraint constraint, IArguments arguments) {
        super(constraint, arguments);
    }
    
    /**
     * Als een <code>JavaConjunct</code> als deel van een Schedule
     * wordt gebruikt, zal het als een onderdeel van een guard 
     * zijn.
     */
    public ScheduleComponentType getScheduleComponentType() {
        return ScheduleComponentType.GUARD;
    }
    
    public boolean canBeAskConjunct() {
        return getArgumentableType().isAskConstraint();
    }
}