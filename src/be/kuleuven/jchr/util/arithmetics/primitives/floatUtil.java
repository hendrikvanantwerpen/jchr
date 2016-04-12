package be.kuleuven.jchr.util.arithmetics.primitives;

/**
 * @author Peter Van Weert
 */
public abstract class floatUtil {
    public static float add(float x, float y) {
        return x + y;
    }
    
    public static float sub(float x, float y) {
        return x - y;
    }
    
    public static float mult(float x, float y) {
        return x * y;
    }
    
    public static float div(float x, float y) {
        return x / y;
    }
    
    public static float mod(float x, float y) {        
        return x % y;
    }
    
    public static boolean modZero(float x, float y) {        
        return x % y == 0;
    }
    
    public static float inc(float x) {
        return ++x;
    }
    
    public static float dec(float x) {
        return --x;
    }
    
    public static float max(float x, float y) {
        return (x > y)? x : y;
    }
    
    public static float min(float x, float y) {
        return (x < y)? x : y;
    }
    
    public static float neg(float x) {
        return -x;
    }
}