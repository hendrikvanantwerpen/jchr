package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.IConstraint;

/**
 * @author Peter Van Weert
 */
public interface IBuiltInConstraint<T extends IBuiltInConstraint> 
    extends IConstraint<T> {
    
    public final static String BUILTIN_MARK = "$builtin";
    
    public final static String 
        EQ  = "eq",   EQi = "=",  EQi2  = "==",
        LEQ = "leq", LEQi = "<=", LEQi2 = "=<",
        GEQ = "geq", GEQi = ">=",
        LT  = "lt",   LTi = "<",
        GT  = "gt",   GTi = ">",
        NEQ = "neq", NEQi = "!=";    
    
}