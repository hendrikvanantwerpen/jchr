package be.kuleuven.jchr.util;


/**
 * @author Peter Van Weert
 */
public final class Arrays {
    private Arrays() {/* non-instantiatable utility class */}
    
    public final static <T> int firstIndexOf(T[] array, T elem) 
    throws ArrayIndexOutOfBoundsException {
        for (int i = 0;; i++) if (array[i].equals(elem)) return i;
    }
    
    public final static <T> int identityFirstIndexOf(T[] array, T elem)
    throws ArrayIndexOutOfBoundsException {
        for (int i = 0;; i++) if (array[i] == elem) return i;
    }
    
    public final static <T> int indexOf(T[] array, T elem) {
        for (int i = 0; i < array.length; i++) 
            if (array[i].equals(elem)) return i;
        return -1;
    }
    
    public final static <T> int identityIndexOf(T[] array, T elem) {
        for (int i = 0; i < array.length; i++)
            if (array[i] == elem) return i;
        return -1;
    }
    
    public final static <T> boolean contains(T[] array, T elem) {
        for (T t : array) if (t.equals(elem)) return true;
        return false;
    }
    
    public final static <T> boolean identityContains(T[] array, T elem) {
        for (T t : array) if (t == elem) return true;
        return false;
    }
}
