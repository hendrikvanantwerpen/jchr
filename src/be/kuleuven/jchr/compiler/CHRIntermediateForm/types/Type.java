package be.kuleuven.jchr.compiler.CHRIntermediateForm.types;

import static be.kuleuven.jchr.util.comparing.Comparison.AMBIGUOUS;
import static be.kuleuven.jchr.util.comparing.Comparison.BETTER;
import static be.kuleuven.jchr.util.comparing.Comparison.EQUAL;
import static be.kuleuven.jchr.util.comparing.Comparison.WORSE;
import be.kuleuven.jchr.util.comparing.Comparison;

/**
 * @author Peter Van Weert
 */
public abstract class Type implements IType {

    public Comparison compareTo(IType other) {
        return compare(this, other);
    }
    
    public String toTypeString() {
        return toString();
    }
    
    public static Comparison compare(IType x, IType y) {
        boolean 
            x2y = x.isDirectlyAssignableTo(y),
            y2x = y.isDirectlyAssignableTo(x);
        
        if (x2y && !y2x) return WORSE;  // y is algemener dan x
        if (!x2y && y2x) return BETTER; // x is algemener dan y
        if (x2y && y2x)  return EQUAL;
        
        /* !x2y && !y2x */
        
        // speciaal geval: in een van de richtingen is coercing mogelijk
        x2y = x.isAssignableTo(y).isNonInitMatch();
        y2x = y.isAssignableTo(x).isNonInitMatch();
        
        if (x2y && !y2x) return BETTER; // x is algemener dan y
        if (y2x && !x2y) return WORSE;  // y is algemener dan x
        
        return AMBIGUOUS;
    }
}