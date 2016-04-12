package be.kuleuven.jchr.annotations;

import java.lang.annotation.*;

/**
 * An annotation indicating the annotated method tells the constraint
 * with given identifier. This identifier has to be the same
 * as the identifier of one of the <code>JCHR_Constraint</code>s
 * in the <code>JCHR_Constraints</code> annotation of the methods
 * class or interface. Furthermore the methods arity has to 
 * be the same as the one declared in the same <code>JCHR_Constraint</code>.
 * <br/>
 * For example:
 * <pre>
 * @JCHR_Tells("eq")
 *  public void tellEqual(LogicalVariable<T> X, LogicalVariable<T> Y);
 * </pre>
 * 
 * @author Peter Van Weert
 * @see be.kuleuven.jchr.annotations.JCHR_Asks
 * @see be.kuleuven.jchr.annotations.JCHR_Constraint
 * @see be.kuleuven.jchr.annotations.JCHR_Constraints
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JCHR_Tells {
    String value();
}
