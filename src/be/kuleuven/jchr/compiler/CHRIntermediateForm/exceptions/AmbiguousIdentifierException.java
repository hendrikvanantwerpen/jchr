package be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions;


/**
 * @author Peter Van Weert
 */
public class AmbiguousIdentifierException extends IdentifierException {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public AmbiguousIdentifierException() {
        super();
    }
    
    /**
     * @param message
     */
    public AmbiguousIdentifierException(String message) {
        super(message);
    }

}
