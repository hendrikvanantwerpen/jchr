package be.kuleuven.jchr.util.comparing;


/**
 * @author Peter Van Weert
 */
public interface Comparable<T> {    
    public Comparison compareTo(T other);
}
