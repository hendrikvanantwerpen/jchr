package be.kuleuven.jchr.util.bool.primitive;

public abstract class booleanUtil {

    public static boolean and(boolean x, boolean y) {
        return x && y;
    }
    
    public static boolean or(boolean x, boolean y) {
        return x || y;
    }
    
    public static boolean xor(boolean x, boolean y) {
        return x ^ y;
    }
    
    public static boolean imp(boolean x, boolean y) {
        return !x || y;
    }
    
    public static boolean not(boolean x) {
        return !x;
    }
}
 