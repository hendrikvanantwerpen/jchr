

package be.kuleuven.jchr.util.arithmetics.wrappers;
/**
 * @author Peter Van Weert
 */
public abstract class ByteUtil {
    public static Byte add(Byte x, Byte y) {
        return Byte.valueOf((byte)(x.byteValue() + y.byteValue()));
    }
    
    public static Byte sub(Byte x, Byte y) {
        return Byte.valueOf((byte)(x.byteValue() - y.byteValue()));
    }
    
    public static Byte mult(Byte x, Byte y) {
        return Byte.valueOf((byte)(x.byteValue() * y.byteValue()));
    }
    
    public static Byte div(Byte x, Byte y) {
        return Byte.valueOf((byte)(x.byteValue() / y.byteValue()));
    }
    
    public static Byte mod(Byte x, Byte y) {        
        return Byte.valueOf((byte)(x.byteValue() % y.byteValue()));
    }
    
    public static boolean modZero(Byte x, Byte y) {        
        return x.byteValue() % y.byteValue() == 0;
    }
    
    public static Byte inc(Byte x) {
        return Byte.valueOf((byte)(x.byteValue() + 1));
    }
    
    public static Byte dec(Byte x) {
        return Byte.valueOf((byte)(x.byteValue() - 1));
    } 
    
    public static boolean gt(Byte x, Byte y) {
        return x.byteValue() > y.byteValue();
    }
    
    public static boolean ge(Byte x, Byte y) {
        return x.byteValue() >= y.byteValue();
    }
    
    public static Byte min(Byte x, Byte y) {
        return (x.compareTo(y) < 0)? x : y;
    }
    
    public static Byte max(Byte x, Byte y) {
        return (x.compareTo(y) > 0)? x : y;
    }
    
    public static Byte neg(Byte x) {
        return Byte.valueOf((byte)-x.byteValue());
    }
}
