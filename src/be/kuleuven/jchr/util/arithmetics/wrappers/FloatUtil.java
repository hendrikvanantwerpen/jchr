package be.kuleuven.jchr.util.arithmetics.wrappers;

/**
 * @author Peter Van Weert
 */
public abstract class FloatUtil {
    public static Float add(Float x, Float y) {
        return Float.valueOf(x.floatValue() + y.floatValue());
    }
    
    public static Float sub(Float x, Float y) {
        return Float.valueOf(x.floatValue() - y.floatValue());
    }
    
    public static Float mult(Float x, Float y) {
        return Float.valueOf(x.floatValue() * y.floatValue());
    }
    
    public static Float div(Float x, Float y) {
        return Float.valueOf(x.floatValue() / y.floatValue());
    }
    
    public static Float mod(Float x, Float y) {        
        return Float.valueOf(x.floatValue() % y.floatValue());
    }
    
    public static boolean modZero(Float x, Float y) {        
        return x.floatValue() % y.floatValue() == 0;
    }
    
    public static Float inc(Float x) {
        return Float.valueOf(x.floatValue() + 1);
    }
    
    public static Float dec(Float x) {
        return Float.valueOf(x.floatValue() - 1);
    } 
    
    public static boolean gt(Float x, Float y) {
        return x.floatValue() > y.floatValue();
    }
    
    public static boolean ge(Float x, Float y) {
        return x.floatValue() >= y.floatValue();
    }
    
    public static Float min(Float x, Float y) {
        return (x.compareTo(y) < 0)? x : y;
    }
    
    public static Float max(Float x, Float y) {
        return (x.compareTo(y) > 0)? x : y;
    }
    
    public static Float neg(Float x) {
        return Float.valueOf(-x.floatValue());
    }
}
