package be.kuleuven.jchr.util.builder;

/**
 * @author Peter Van Weert
 */
public class Current<T> {
    private T current;
    
    public void reset() {
        current = null;
    }
    public T get() throws IllegalStateException {
        if (! isSet())
            throw new IllegalStateException("No current set!");
        return current;
    }
    public void set(T current) throws IllegalStateException {
        if (isSet())
            throw new IllegalStateException("Current already set!");
        this.current = current;
    }
    public boolean isSet() {
        return (current != null);
    }
    
    @Override
    public String toString() {
        return "Current<" + current + '>';
    }
}