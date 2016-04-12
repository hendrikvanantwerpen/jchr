package be.kuleuven.jchr.util.iterator;

import java.util.NoSuchElementException;

public final class EmptyIterator<T> implements ResettableListIterator<T> {
    
    private EmptyIterator() {/* SINGLETON */}
    
    // Since this is a singleton that is used *a lot* in many cases, we
    // instantiate it eagerly.
    private static EmptyIterator instance = new EmptyIterator();
    
    /*
     * Since this is a singleton, it cannot be type safe!
     */
    @SuppressWarnings("unchecked")
    public final static <T> EmptyIterator<T> getInstance() {
        return instance;
    }
    
    public boolean hasNext() {
        return false;
    }
    
    
    public T next() {
        throw new NoSuchElementException();
    }
    
    /**
     * This operation is not supported by this iterator.
     * 
     * @throws UnsupportedOperationException
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    /**
     * This operation is not supported by this iterator.
     * 
     * @throws UnsupportedOperationException
     */
    public void add(T t) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    public boolean hasPrevious() {
        return false;
    }
    
    public int nextIndex() {
        return 0;
    }
    
    public T previous() {
        throw new NoSuchElementException();
    }
    
    public int previousIndex() {
        return -1;
    }
    
    /**
     * This operation is not supported by this iterator.
     * 
     * @throws UnsupportedOperationException
     */
    public void set(T t) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    public boolean isTerminated() {
        return false;
    }
    
    public void terminate() {
        // NOP
    }
    
    public void reset() {
        // NOP
    }
}
