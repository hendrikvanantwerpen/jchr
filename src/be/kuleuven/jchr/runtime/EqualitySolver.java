package be.kuleuven.jchr.runtime;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.EQ;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.EQi;
import be.kuleuven.jchr.annotations.*;


/**
 * @author Peter Van Weert
 */
//@WakeConditions( {"touched", "bound"} )
@JCHR_Constraints({
    @JCHR_Constraint(
        identifier = EQ,
        arity = 2,
        infix = EQi
    )
})
public interface EqualitySolver<Type> {
    @JCHR_Tells(EQ)
    public void tellEqual(Logical<Type> X, Type val);
    @JCHR_Tells(EQ)
    public void tellEqual(Type val, Logical<Type> X);
    @JCHR_Tells(EQ)
    public void tellEqual(Logical<Type> X, Logical<Type> Y);
    
//    @WakesList( "bound" )
    @JCHR_Asks(EQ)
    public boolean askEqual(Logical<Type> X, Type val);
//  @WakesList( "bound" )
    @JCHR_Asks(EQ)
    public boolean askEqual(Type val, Logical<Type> X);
    
//    @WakesList( { "bound", "touched" } )
    @JCHR_Asks(EQ)
    public boolean askEqual(Logical<Type> X, Logical<Type> Y);
}