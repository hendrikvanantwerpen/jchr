package be.kuleuven.jchr.annotations;

/**
 * An annotation intended to be listed in a <code>JCHR_Constraints</code>
 * annotation of a JCHR built-in constraint solver class or interface.
 * Each constraint indicates its (unique) identifier, its arity,
 * an optional infix identifier and an optional prefix.
 * 
 * @todo does the prefix thingy work? 
 * 
 * @author Peter Van Weert
 * 
 * @see be.kuleuven.jchr.annotations.JCHR_Constraints
 * @see be.kuleuven.jchr.annotations.JCHR_Asks
 * @see be.kuleuven.jchr.annotations.JCHR_Tells
 */
public @interface JCHR_Constraint {
    /**
     * The (unique!) identifier of this constraint. This has to be a
     * String containing a valid identifier for a constraint. Please
     * refer to the manual for constrains about constraint identifiers.
     * This identifier can than be used in <code>JCHR_Asks</code> and
     * <code>JCHR_Tells</code> annotations.
     *  
     * @return The (unique!) identifier of this constraint.
     */
    String identifier();
    
    /**
     * The arity of this constraint (this is its number of 
     * arguments).
     *  
     * @return The arity of this constraint.
     */
    int arity();
    
    /**
     * The (unique!) infix-identifier of this constraint. This has to be a
     * String containing a valid identifier for a constraint. Please
     * refer to the manual for constrains about constraint identifiers.
     *  
     * @return The (unique!) infix identifier of this constraint.
     */
    String infix() default "";
    
    /*
     * TODO check that this works?
     */
    String prefix() default "";
}