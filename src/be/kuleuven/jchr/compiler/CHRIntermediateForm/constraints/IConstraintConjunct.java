package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints;


public interface IConstraintConjunct<T extends IConstraint> extends IConjunct {

    public String getIdentifier();

}