package be.kuleuven.jchr.util;

import java.util.AbstractCollection;
import java.util.Collection;

/**
 * This class provides a skeletal implementation of the <tt>Collection</tt>
 * interface, to minimize the effort required to implement this interface.
 * <p>
 * 
 * To implement an unmodifiable collection, the programmer needs only to extend
 * this class and provide implementations for the <tt>iterator</tt> and
 * <tt>size</tt> methods. (The iterator returned by the <tt>iterator</tt>
 * method must implement <tt>hasNext</tt> and <tt>next</tt>.)
 * <p>
 * 
 * The programmer should generally provide a void (no argument) and
 * <tt>Collection</tt> constructor, as per the recommendation in the
 * <tt>Collection</tt> interface specification.
 * <p>
 * 
 * The documentation for each non-abstract methods in this class describes its
 * implementation in detail. Each of these methods may be overridden if the
 * collection being implemented admits a more efficient implementation.
 * <p>
 * 
 * @author Peter Van Weert
 * @see java.util.AbstractCollection
 */
public abstract class AbstractUnmodifiableCollection<T> extends
        AbstractCollection<T> {

    @Override
    /**
     * This method is not supported (unmodifiable collection).
     * 
     * @throws UnsupportedOperationException
     */
    public final boolean add(T o) {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * This method is not supported (unmodifiable collection).
     * 
     * @throws UnsupportedOperationException
     */
    public final boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * This method is not supported (unmodifiable collection).
     * 
     * @throws UnsupportedOperationException
     */
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * This method is not supported (unmodifiable collection).
     * 
     * @throws UnsupportedOperationException
     */
    public final boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * This method is not supported (unmodifiable collection).
     * 
     * @throws UnsupportedOperationException
     */
    public final boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * This method is not supported (unmodifiable collection).
     * 
     * @throws UnsupportedOperationException
     */
    public final boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}