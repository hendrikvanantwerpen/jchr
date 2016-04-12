package be.kuleuven.jchr.util.iterator;

import java.util.NoSuchElementException;

/*
 * version 1.0.3    (Peter Van Weert)
 *  - extended towards ListIterator (just for the fun of it)
 *  - made it a concrete class
 * version 1.3.3
 *  - extended towards ResettableListIterator (more fun)
 */
public class SingletonIterator<T> implements ResettableListIterator<T> {
    private T value;
    
    private boolean next = true;
        
    public SingletonIterator(T value) {
        setValue(value);
    }

    public boolean hasNext() {
        return next;
    }

    public T next() {
        if (!hasNext()) 
            throw new NoSuchElementException();
        else {
            setNext(false);
            return getValue();
        }
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
    public void add(T o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean hasPrevious() {
        return ! hasNext();
    }

    public int nextIndex() {
        return hasNext()? 0 : 1;
    }

    public T previous() {
        if (!hasPrevious())
            throw new NoSuchElementException();
        else {
            setNext(true);
            return getValue();
        }
    }

    public int previousIndex() {
        return hasPrevious()? 0 : -1;
    }

    /**
     * This operation is not supported by this iterator.
     * 
     * @throws UnsupportedOperationException
     */
    public void set(T o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    protected T getValue() {
        return value;
    }

    protected void setValue(T value) {
        this.value = value;
    }
    
    public void reset() {
        setNext(true);
    }
}