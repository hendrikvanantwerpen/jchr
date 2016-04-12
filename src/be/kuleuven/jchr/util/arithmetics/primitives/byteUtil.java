package be.kuleuven.jchr.util.arithmetics.primitives;

/**
 * @author Peter Van Weert
 */
public abstract class byteUtil {
    public static byte add(byte x, byte y) {
        return (byte)(x + y);
    }
    
    public static byte sub(byte x, byte y) {
        return (byte)(x - y);
    }
    
    public static byte mult(byte x, byte y) {
        return (byte)(x * y);
    }
    
    public static byte div(byte x, byte y) {
        return (byte)(x / y);
    }
    
    public static byte mod(byte x, byte y) {        
        return (byte)(x % y);
    }
    
    public static boolean modZero(byte x, byte y) {        
        return x % y == 0;
    }
    
    public static byte inc(byte x) {
        return ++x;
    }
    
    public static byte dec(byte x) {
        return --x;
    }
    
    public static byte max(byte x, byte y) {
        return (x > y)? x : y;
    }
    
    public static byte min(byte x, byte y) {
        return (x < y)? x : y;
    }
    
    public static byte neg(byte x) {
        return (byte)-x;
    }
}