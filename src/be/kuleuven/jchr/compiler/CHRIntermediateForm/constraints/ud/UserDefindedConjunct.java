package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ConstraintConjunct;

/**
 * @author Peter Van Weert
 */
public class UserDefindedConjunct extends ConstraintConjunct<UserDefinedConstraint> {

    public UserDefindedConjunct(UserDefinedConstraint constraint, IArguments arguments) {
        super(constraint, arguments);
    }
}