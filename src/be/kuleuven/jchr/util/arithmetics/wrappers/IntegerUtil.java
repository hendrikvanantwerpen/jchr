package be.kuleuven.jchr.util.arithmetics.wrappers;

/**
 * @author Peter Van Weert
 */
public abstract class IntegerUtil {
    public static Integer add(Integer x, Integer y) {
        return Integer.valueOf(x.intValue() + y.intValue());
    }
    
    public static Integer sub(Integer x, Integer y) {
        return Integer.valueOf(x.intValue() - y.intValue());
    }
    
    public static Integer mult(Integer x, Integer y) {
        return Integer.valueOf(x.intValue() * y.intValue());
    }
    
    public static Integer div(Integer x, Integer y) {
        return Integer.valueOf(x.intValue() / y.intValue());
    }
    
    public static Integer mod(Integer x, Integer y) {        
        return Integer.valueOf(x.intValue() % y.intValue());
    }
    
    public static boolean modZero(Integer x, Integer y) {        
        return x.intValue() % y.intValue() == 0;
    }
    
    public static Integer inc(Integer x) {
        return Integer.valueOf(x.intValue() + 1);
    }
    
    public static Integer dec(Integer x) {
        return Integer.valueOf(x.intValue() - 1);
    } 
    
    public static boolean gt(Integer x, Integer y) {
        return x.intValue() > y.intValue();
    }
    
    public static boolean ge(Integer x, Integer y) {
        return x.intValue() >= y.intValue();
    }
    
    public static Integer min(Integer x, Integer y) {
        return (x.compareTo(y) < 0)? x : y;
    }
    
    public static Integer max(Integer x, Integer y) {
        return (x.compareTo(y) > 0)? x : y;
    }
    
    public static Integer neg(Integer x) {
        return Integer.valueOf(-x.intValue());
    }
}
