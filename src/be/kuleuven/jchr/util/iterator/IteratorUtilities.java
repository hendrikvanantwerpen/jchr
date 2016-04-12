package be.kuleuven.jchr.util.iterator;

import java.util.Iterator;

public final class IteratorUtilities {

    private IteratorUtilities() {/* non-instantiatable utility class */}
    
    public static <T> String toString(Iterable<T> iterable) {
        return toString(iterable.iterator());
    }
    
    public static <T> String toString(Iterator<T> iterator) {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        if (iterator.hasNext()) 
            builder.append(iterator.next());
        while (iterator.hasNext()) {
            builder.append(',');
            builder.append(iterator.next());
        }
        builder.append(']');
        return builder.toString();
    }
    
    public static <T> int size(Iterable<T> iterable) {
        return size(iterable.iterator());
    }
    
    public static <T> int size(Iterator<T> iterator) {
        int result = 0;
        while (iterator.hasNext()) {
            iterator.next();
            result++; 
        }
        return result;
    }
    
    public static <T> boolean isEmpty(Iterable<T> iterable) {
        return isEmpty(iterable.iterator());
    }
    
    public static <T> boolean isEmpty(Iterator<T> iterator) {
        return iterator.hasNext();
    }
}