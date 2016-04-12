package be.kuleuven.jchr.compiler.analysis;

/**
 * A generic exception indicating something has gone wrong
 * during an analysis.
 * 
 * @author Peter Van Weert
 * @see Analysor#analyse(CHRIntermediateForm, Options)
 */
public class AnalysisException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * @inheritDoc
     */
    public AnalysisException() {
        super();
    }

    /**
     * @inheritDoc
     */
    public AnalysisException(String message) {
        super(message);
    }

    /**
     * @inheritDoc
     */
    public AnalysisException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @inheritDoc
     */
    public AnalysisException(Throwable cause) {
        super(cause);        
    }
}
