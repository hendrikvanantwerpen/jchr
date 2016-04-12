package be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions;


/**
 * @author Peter Van Weert
 */
public class DuplicateIdentifierException extends IdentifierException {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public DuplicateIdentifierException() {
        super();
    }
    
    /**
     * @param message
     */
    public DuplicateIdentifierException(String message) {
        super(message);
    }

}
