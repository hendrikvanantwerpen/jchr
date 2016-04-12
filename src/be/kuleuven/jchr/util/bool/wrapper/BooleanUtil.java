package be.kuleuven.jchr.util.bool.wrapper;

public abstract class BooleanUtil {

    public static Boolean and(Boolean x, Boolean y) {
        return (x.booleanValue() && y.booleanValue())? Boolean.TRUE : Boolean.FALSE;
    }
    
    public static Boolean or(Boolean x, Boolean y) {
        return (x.booleanValue() || y.booleanValue())? Boolean.TRUE : Boolean.FALSE;
    }
    
    public static Boolean xor(Boolean x, Boolean y) {
        return (x.booleanValue() ^ y.booleanValue())? Boolean.TRUE : Boolean.FALSE;
    }
    
    public static Boolean imp(Boolean x, Boolean y) {
        return (!x.booleanValue() || y.booleanValue())? Boolean.TRUE : Boolean.FALSE;
    }
    
    public static Boolean not(Boolean x) {
        return x.booleanValue()? Boolean.FALSE : Boolean.TRUE;
    }
}
 