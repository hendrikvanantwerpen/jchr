package be.kuleuven.jchr.compiler.codeGeneration;

/**
 * @author Peter Van Weert
 */
public class GenerationException extends Exception {
    
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public GenerationException() {
        super();
    }
    
    /**
     * @param message
     */
    public GenerationException(String message) {
        super(message);
    }
    
    /**
     * @param message
     * @param cause
     */
    public GenerationException(String message, Throwable cause) {
        super(message, cause);
    
    }
    
    /**
     * @param cause
     */
    public GenerationException(Throwable cause) {
        super(cause);
    }
}
