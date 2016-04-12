package be.kuleuven.jchr.util.arithmetics.primitives;

/**
 * @author Peter Van Weert
 */
public abstract class intUtil {
    public static int add(int x, int y) {
        return x + y;
    }
    
    public static int sub(int x, int y) {
        return x - y;
    }
    
    public static int mult(int x, int y) {
        return x * y;
    }
    
    public static int div(int x, int y) {
        return x / y;
    }
    
    public static int mod(int x, int y) {        
        return x % y;
    }
    
    public static boolean modZero(int x, int y) {        
        return x % y == 0;
    }
    
    public static int inc(int x) {
        return ++x;
    }
    
    public static int dec(int x) {
        return --x;
    }
    
    public static int max(int x, int y) {
        return (x > y)? x : y;
    }
    
    public static int min(int x, int y) {
        return (x < y)? x : y;
    }
    
    public static int neg(int x) {
        return -x;
    }
}