package be.kuleuven.jchr.util.comparing;

/**
 * @author Peter Van Weert
 */
public enum Comparison {
    WORSE, EQUAL, BETTER, AMBIGUOUS;
        
    public static Comparison get(int i) {        
        if (i < 0) return WORSE;
        if (i > 0) return BETTER;
        return EQUAL;
    }
    
    public static Comparison flip(Comparison comparison) {
        switch (comparison) {
            case BETTER: return WORSE;
            case WORSE:  return BETTER;
            default: return comparison;
        }
    }
}
