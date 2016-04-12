package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.IConstraintConjunct;

public interface IBuiltInConjunct<T extends IBuiltInConstraint<?>>
    extends IGuardConjunct, IConstraintConjunct<T> {

    // no new methods
}