package be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;

public class IllegalVariableTypeException extends Exception {    
    private static final long serialVersionUID = 1L;

    public IllegalVariableTypeException(VariableType type) {
        super(type.toString());
    }
    
    public IllegalVariableTypeException(String message) {
        super(message);
    }
}
