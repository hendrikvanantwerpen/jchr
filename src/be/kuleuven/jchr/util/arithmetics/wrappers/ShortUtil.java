package be.kuleuven.jchr.util.arithmetics.wrappers;

/**
 * @author Peter Van Weert
 */
public abstract class ShortUtil {
    public static Short add(Short x, Short y) {
        return Short.valueOf((short)(x.shortValue() + y.shortValue()));
    }
    
    public static Short sub(Short x, Short y) {
        return Short.valueOf((short)(x.shortValue() - y.shortValue()));
    }
    
    public static Short mult(Short x, Short y) {
        return Short.valueOf((short)(x.shortValue() * y.shortValue()));
    }
    
    public static Short div(Short x, Short y) {
        return Short.valueOf((short)(x.shortValue() / y.shortValue()));
    }
    
    public static Short mod(Short x, Short y) {        
        return Short.valueOf((short)(x.shortValue() % y.shortValue()));
    }
    
    public static boolean modZero(Short x, Short y) {        
        return x.shortValue() % y.shortValue() == 0;
    }
    
    public static Short inc(Short x) {
        return Short.valueOf((short)(x.shortValue() + 1));
    }
    
    public static Short dec(Short x) {
        return Short.valueOf((short)(x.shortValue() - 1));
    } 
    
    public static boolean gt(Short x, Short y) {
        return x.shortValue() > y.shortValue();
    }
    
    public static boolean ge(Short x, Short y) {
        return x.shortValue() >= y.shortValue();
    }
    
    public static Short min(Short x, Short y) {
        return (x.compareTo(y) < 0)? x : y;
    }
    
    public static Short max(Short x, Short y) {
        return (x.compareTo(y) > 0)? x : y;
    }
    
    public static Short neg(Short x) {
        return Short.valueOf((short)-x.shortValue());
    }
}
