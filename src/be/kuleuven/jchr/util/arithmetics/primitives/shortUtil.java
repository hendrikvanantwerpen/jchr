package be.kuleuven.jchr.util.arithmetics.primitives;

/**
 * @author Peter Van Weert
 */
public abstract class shortUtil {
    public static short add(short x, short y) {
        return (short)(x + y);
    }
    
    public static short sub(short x, short y) {
        return (short)(x - y);
    }
    
    public static short mult(short x, short y) {
        return (short)(x * y);
    }
    
    public static short div(short x, short y) {
        return (short)(x / y);
    }
    
    public static short mod(short x, short y) {        
        return (short)(x % y);
    }
    
    public static boolean modZero(short x, short y) {        
        return x % y == 0;
    }
    
    public static short inc(short x) {
        return ++x;
    }
    
    public static short dec(short x) {
        return --x;
    }
    
    public static short max(short x, short y) {
        return (x > y)? x : y;
    }
    
    public static short min(short x, short y) {
        return (x < y)? x : y;
    }
    
    public static short neg(short x) {
        return (short)-x;
    }
}