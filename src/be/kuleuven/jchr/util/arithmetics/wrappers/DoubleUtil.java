package be.kuleuven.jchr.util.arithmetics.wrappers;

/**
 * @author Peter Van Weert
 */
public abstract class DoubleUtil {
    public static Double add(Double x, Double y) {
        return Double.valueOf(x.doubleValue() + y.doubleValue());
    }
    
    public static Double sub(Double x, Double y) {
        return Double.valueOf(x.doubleValue() - y.doubleValue());
    }
    
    public static Double mult(Double x, Double y) {
        return Double.valueOf(x.doubleValue() * y.doubleValue());
    }
    
    public static Double div(Double x, Double y) {
        return Double.valueOf(x.doubleValue() / y.doubleValue());
    }
    
    public static Double mod(Double x, Double y) {        
        return Double.valueOf(x.doubleValue() % y.doubleValue());
    }
    
    public static boolean modZero(Double x, Double y) {        
        return x.doubleValue() % y.doubleValue() == 0;
    }
    
    public static Double inc(Double x) {
        return Double.valueOf(x.doubleValue() + 1);
    }
    
    public static Double dec(Double x) {
        return Double.valueOf(x.doubleValue() - 1);
    } 
    
    public static boolean gt(Double x, Double y) {
        return x.doubleValue() > y.doubleValue();
    }
    
    public static boolean ge(Double x, Double y) {
        return x.doubleValue() >= y.doubleValue();
    }
    
    public static Double min(Double x, Double y) {
        return (x.compareTo(y) < 0)? x : y;
    }
    
    public static Double max(Double x, Double y) {
        return (x.compareTo(y) > 0)? x : y;
    }
    
    public static Double neg(Double x) {
        return Double.valueOf(-x.doubleValue());
    }
}
