package be.kuleuven.jchr.util.iterator;

import java.util.Iterator;


public interface ResettableIterator<T> extends Iterator<T> {
    
    /**
     * Resets the iterator in its initial state (the state it was
     * in when created).
     */
    void reset();

}
