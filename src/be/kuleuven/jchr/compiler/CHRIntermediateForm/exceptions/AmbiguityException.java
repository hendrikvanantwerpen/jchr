package be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.BuiltInConstraint;

/**
 * @author Peter Van Weert
 *
 */
public class AmbiguityException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public AmbiguityException() {
        super();
    }
    
    public AmbiguityException(String message) {
        super(message);
    }

    public AmbiguityException(BuiltInConstraint constraint) {
        super("Ambiguous constraint: "  
                + constraint.toString()
                + ", declare explicit solver instead");
    }
}
