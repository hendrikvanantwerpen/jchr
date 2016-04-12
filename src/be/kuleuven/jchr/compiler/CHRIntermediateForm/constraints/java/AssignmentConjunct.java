package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;

public class AssignmentConjunct extends JavaConjunct {
    private boolean declaration;
    
    public AssignmentConjunct(Assignment constraint, IArguments arguments) {
        super(constraint, arguments);
    }

    public boolean isDeclaration() {
        return declaration;
    }
    public void setDeclarator() {
        declaration = true;
    }
    public IType getType() {
        return getArgumentAt(0).getType();
    }
    public String getTypeString() {
        return getType().toTypeString();
    }
}
