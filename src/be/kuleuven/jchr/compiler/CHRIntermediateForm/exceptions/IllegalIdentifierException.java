package be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions;


/**
 * @author Peter Van Weert
 */
public class IllegalIdentifierException extends IdentifierException {    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public IllegalIdentifierException() {
        super();
    }
    
    /**
     * @param message
     */
    public IllegalIdentifierException(String message) {
        super(message);
    }
}
