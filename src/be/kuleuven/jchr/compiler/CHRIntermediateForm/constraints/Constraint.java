package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.Argumentable;

/**
 * @author Peter Van Weert
 * 
 */
public abstract class Constraint<T extends IConstraint> implements
        IConstraint<T> {

    private String identifier;

    public Constraint(String id) {
        setIdentifier(id);
    }

    protected Constraint() {
        // NOP
    }

    public String getIdentifier() {
        return identifier;
    }

    protected void setIdentifier(String name) {
        this.identifier = name;
    }

    @Override
    public String toString() {
        return getIdentifier() + Argumentable.toString(this);
    }

}