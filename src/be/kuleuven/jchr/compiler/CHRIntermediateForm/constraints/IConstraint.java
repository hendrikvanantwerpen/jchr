package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.IArgumentable;

/**
 * @author Peter Van Weert
 */
public interface IConstraint<T extends IConstraint> extends IArgumentable<T> {
    public String getIdentifier();

    public boolean isAskConstraint();
    
    public boolean isEquality();
        
}