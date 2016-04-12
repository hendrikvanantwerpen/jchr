package be.kuleuven.jchr.util;

public class IndexOutOfBoundsException extends java.lang.IndexOutOfBoundsException {

    private static final long serialVersionUID = 1L;
    
    public IndexOutOfBoundsException() {
        super();
    }

    public IndexOutOfBoundsException(String s) {
        super(s);
    }

    public IndexOutOfBoundsException(int index) {
        this(String.valueOf(index));
    }
    
    public IndexOutOfBoundsException(int index, int max) {
        this(index, 0, max);
    } 

    public IndexOutOfBoundsException(int index, int min, int max) {
        this(index + " is not within bounds [" + min + ", " + max + ']');
    }
    
}
