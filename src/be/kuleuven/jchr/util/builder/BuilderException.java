package be.kuleuven.jchr.util.builder;

/**
 * @author Peter Van Weert
 */
public class BuilderException extends Exception {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public BuilderException() {
        super();
    }

    /**
     * @param message
     */
    public BuilderException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public BuilderException(Throwable cause) {
        super(cause);        
    }
}
