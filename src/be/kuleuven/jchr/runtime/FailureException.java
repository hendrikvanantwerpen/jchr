package be.kuleuven.jchr.runtime;

public class FailureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FailureException() {
        super();
    }

    public FailureException(String message) {
        super(message);
    }
}
