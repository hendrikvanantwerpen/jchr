package be.kuleuven.jchr.util.arithmetics.primitives;

/**
 * @author Peter Van Weert
 */
public abstract class doubleUtil {
    public static double add(double x, double y) {
        return x + y;
    }
    
    public static double sub(double x, double y) {
        return x - y;
    }
    
    public static double mult(double x, double y) {
        return x * y;
    }
    
    public static double div(double x, double y) {
        return x / y;
    }
    
    public static double mod(double x, double y) {        
        return x % y;
    }
    
    public static boolean modZero(double x, double y) {        
        return x % y == 0;
    }
    
    public static double inc(double x) {
        return ++x;
    }
    
    public static double dec(double x) {
        return --x;
    }
    
    public static double max(double x, double y) {
        return (x > y)? x : y;
    }
    
    public static double min(double x, double y) {
        return (x < y)? x : y;
    }
    
    public static double neg(double x) {
        return -x;
    }
}