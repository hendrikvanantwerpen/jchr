package be.kuleuven.jchr.compiler.CHRIntermediateForm.init;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.JavaConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;


public interface IDeclarator<T extends IDeclarator> {

    public IType getType();
    
    public boolean usesIdentifier();
    
    public JavaConjunct getInstance(Variable variable);
    
    public JavaConjunct getInstance(Variable variable, String identifier);

}