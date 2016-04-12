package be.kuleuven.jchr.compiler.CHRIntermediateForm.init;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.Arguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.Assignment;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.AssignmentConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;

public class AssignmentInitialisator extends Assignment 
implements IDeclarator<AssignmentInitialisator> {
    
    private IInitialisator<?> initialisator;
    
    public AssignmentInitialisator(IInitialisator<?> initialisator) {
        super(initialisator.getType());
        if (! initialisator.isValidDeclarationInitialisator())
            throw new IllegalArgumentException();
        else
            setInitialisator(initialisator);
    }
    
    public IType getType() {
        return getArgumentType();
    }

    public boolean usesIdentifier() {
        return getInitialisator().usesIdentifier();
    }

    public AssignmentConjunct getInstance(Variable variable) {
        return getInstance(new Arguments(variable, getInitialisator().getInstance()));    
    }

    public AssignmentConjunct getInstance(Variable variable, String identifier) {
        return getInstance(new Arguments(variable, getInitialisator().getInstance(identifier)));
    }
    
    @Override
    public AssignmentConjunct getInstance(IArguments arguments) {
        final AssignmentConjunct result = super.getInstance(arguments);
        result.setDeclarator();
        return result;
    }
    
    protected IInitialisator<?> getInitialisator() {
        return initialisator;
    }
    protected void setInitialisator(IInitialisator<?> initialisator) {
        this.initialisator = initialisator;
    }
}
