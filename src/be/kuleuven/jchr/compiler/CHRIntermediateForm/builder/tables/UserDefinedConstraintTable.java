package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;


/**
 * @author Peter Van Weert
 */
public class UserDefinedConstraintTable extends ConstraintTable<UserDefinedConstraint> {
    
    public UserDefinedConstraint declareConstraint(String id) 
    throws DuplicateIdentifierException {        
        return declare(id, new UserDefinedConstraint(id), false);
    }
    
    public void declareInfixFor(UserDefinedConstraint ud)
    throws DuplicateIdentifierException {
        declare(ud.getInfix(), ud, true);
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(100);
        result.append(getClass().getName());
        result.append('@');
        result.append(Integer.toHexString(hashCode()));
        result.append(" contains ");
        result.append(getSize());
        result.append(" symbols:\n");
        
        for (String id : getTable().keySet()) {
            result.append('\t');
            result.append(id);
            result.append(" ==> ");
            result.append(get(id).toString());
            result.append('\n');
        }
        
        return result.toString();
    }
}