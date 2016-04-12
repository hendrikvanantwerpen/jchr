package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;

/**
 * @author Peter Van Weert
 */
public class VariableTable extends SymbolTable<Variable> {    
    
    public Variable declareVariable(VariableType type, String identifier) 
    throws DuplicateIdentifierException, IllegalIdentifierException {
        return declare(identifier, new Variable(type, identifier));
    }
}
