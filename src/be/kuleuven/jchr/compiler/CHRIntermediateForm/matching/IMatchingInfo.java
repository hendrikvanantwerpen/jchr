package be.kuleuven.jchr.compiler.CHRIntermediateForm.matching;

import be.kuleuven.jchr.util.comparing.Comparable;



/**
 * @author Peter Van Weert
 */
public interface IMatchingInfo<T extends IMatchingInfo> extends Comparable<T> {

    public boolean isMatch();
    
    public boolean isNonAmbiguousMatch();
    
    public boolean isAmbiguous();
    
    public boolean isExactMatch();
    
    public boolean isNonExactMatch();
       
    public boolean isCoerceMatch();
    
    public boolean isInitMatch();
    
    public boolean isNonInitMatch();
}