package be.kuleuven.jchr.util.arithmetics.primitives;

/**
 * @author Peter Van Weert
 */
public abstract class longUtil {
    public static long add(long x, long y) {
        return x + y;
    }
    
    public static long sub(long x, long y) {
        return x - y;
    }
    
    public static long mult(long x, long y) {
        return x * y;
    }
    
    public static long div(long x, long y) {
        return x / y;
    }
    
    public static long mod(long x, long y) {        
        return x % y;
    }
    
    public static boolean modZero(long x, long y) {        
        return x % y == 0;
    }
    
    public static long inc(long x) {
        return ++x;
    }
    
    public static long dec(long x) {
        return --x;
    }
    
    public static long max(long x, long y) {
        return (x > y)? x : y;
    }
    
    public static long min(long x, long y) {
        return (x < y)? x : y;
    }
    
    public static long neg(long x) {
        return -x;
    }
}