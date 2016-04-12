package be.kuleuven.jchr.runtime.primitive;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.EQ;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.EQi;
import be.kuleuven.jchr.annotations.*;


/**
 * @author Peter Van Weert
 */
@JCHR_Constraints({
    @JCHR_Constraint(
        identifier = EQ,
        arity = 2,
        infix = EQi
    )
})
public interface BooleanEqualitySolver {
    @JCHR_Tells(EQ)
    public void tellEqual(LogicalBoolean X, boolean val);
    @JCHR_Tells(EQ)
    public void tellEqual(boolean val, LogicalBoolean X);
    @JCHR_Tells(EQ)
    public void tellEqual(LogicalBoolean X, LogicalBoolean Y);
    
    @JCHR_Asks(EQ)
    public boolean askEqual(LogicalBoolean X, boolean val);
    @JCHR_Asks(EQ)
    public boolean askEqual(boolean val, LogicalBoolean X);
    @JCHR_Asks(EQ)
    public boolean askEqual(LogicalBoolean X, LogicalBoolean Y);
}