package be.kuleuven.jchr.util;

import java.util.AbstractSet;
import java.util.Iterator;

import be.kuleuven.jchr.util.iterator.SingletonIterator;


/*
 * version 1.0.3    (Peter Van Weert)
 *   reimplemented
 */
public class SingletonSet<T> extends AbstractSet<T> {
    private T value;
    
    public SingletonSet(T value) {
        setValue(value);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Iterator<T> iterator() {
        return new SingletonIterator<T>(getValue());
    }

    public T getValue() {
        return value;
    }

    protected void setValue(T singletonValue) {
        this.value = singletonValue;
    }
}