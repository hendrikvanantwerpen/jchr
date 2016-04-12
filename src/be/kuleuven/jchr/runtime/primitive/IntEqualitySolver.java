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
public interface IntEqualitySolver {
    @JCHR_Tells(EQ)
    public void tellEqual(LogicalInt X, int val);
    @JCHR_Tells(EQ)
    public void tellEqual(int val, LogicalInt X);
    @JCHR_Tells(EQ)
    public void tellEqual(LogicalInt X, LogicalInt Y);
    
    @JCHR_Asks(EQ)
    public boolean askEqual(LogicalInt X, int val);
    @JCHR_Asks(EQ)
    public boolean askEqual(int val, LogicalInt X);
    @JCHR_Asks(EQ)
    public boolean askEqual(LogicalInt X, LogicalInt Y);
}