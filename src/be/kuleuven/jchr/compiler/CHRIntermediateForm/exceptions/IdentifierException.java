package be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions;


/**
 * @author Peter Van Weert
 */
public class IdentifierException extends Exception {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public IdentifierException() {
        super();
    }
    
    /**
     * @param message
     */
    public IdentifierException(String message) {
        super(message);
    }
}
