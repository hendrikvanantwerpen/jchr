package be.kuleuven.jchr.util.arithmetics.wrappers;

/**
 * @author Peter Van Weert
 */
public abstract class LongUtil {
    public static Long add(Long x, Long y) {
        return Long.valueOf(x.longValue() + y.longValue());
    }
    
    public static Long sub(Long x, Long y) {
        return Long.valueOf(x.longValue() - y.longValue());
    }
    
    public static Long mult(Long x, Long y) {
        return Long.valueOf(x.longValue() * y.longValue());
    }
    
    public static Long div(Long x, Long y) {
        return Long.valueOf(x.longValue() / y.longValue());
    }
    
    public static Long mod(Long x, Long y) {        
        return Long.valueOf(x.longValue() % y.longValue());
    }
    
    public static boolean modZero(Long x, Long y) {        
        return x.longValue() % y.longValue() == 0;
    }
    
    public static Long inc(Long x) {
        return Long.valueOf(x.longValue() + 1);
    }
    
    public static Long dec(Long x) {
        return Long.valueOf(x.longValue() - 1);
    } 
    
    public static boolean gt(Long x, Long y) {
        return x.longValue() > y.longValue();
    }
    
    public static boolean ge(Long x, Long y) {
        return x.longValue() >= y.longValue();
    }
    
    public static Long min(Long x, Long y) {
        return (x.compareTo(y) < 0)? x : y;
    }
    
    public static Long max(Long x, Long y) {
        return (x.compareTo(y) > 0)? x : y;
    }
    
    public static Long neg(Long x) {
        return Long.valueOf(-x.longValue());
    }
}
