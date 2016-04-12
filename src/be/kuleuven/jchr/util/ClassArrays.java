package be.kuleuven.jchr.util;

/**
 * @author Peter Van Weert
 * 
 * @deprecated Houdt geen rekening met generic types!
 */
public final class ClassArrays {

    private ClassArrays() {/* non-instantiatable utility class */}
    
    public static boolean isAssignableFrom(Class<?>[] formal, Class<?>[] actual) {
        if (formal.length != actual.length) return false;
        
        for (int i = 0; i < formal.length; i++)
            if (! formal[i].isAssignableFrom(actual[i]) )                 
                return false;
        
        return true;
    }
}
