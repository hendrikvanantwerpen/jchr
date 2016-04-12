package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;

/**
 * Deze klasse veegt zowat zijn voeten aan alle overervings-principes door
 * een aantal methodes onbruikbaar te maken, en extra infix-informatie te
 * vereisen! Dit kan uiteraard beter door de hiërarchie iets aan te passen,
 * wie weet doe ik dat nog wel eens.
 * 
 * @author Peter Van Weert
 */
public abstract class ConstraintTable<T> extends SymbolTable<T> {
    
    @Override
    final protected T declare(String id,T t) throws DuplicateIdentifierException {
        throw new UnsupportedOperationException("Use the version with infix-information instead");
    }
    
    protected T declare(String id,T t, boolean infix) throws DuplicateIdentifierException {
        return infix
            ? super.declare(createInfixIdentifier(id), t)
            : super.declare(id, t);
    }

    @Override
    final public boolean isDeclaredId(String id) {
        throw new UnsupportedOperationException("Use the version with infix-information instead");
    }
    
    public boolean isDeclaredId(String id, boolean infix) {
        return infix
            ? super.isDeclaredId(createInfixIdentifier(id))
            : super.isDeclaredId(id);
    }
    
    @Override
    final public T get(String id) {
        throw new UnsupportedOperationException("Use the version with infix-information instead");
    }
    
    public T get(String id, boolean infix) {
        return infix
            ? super.get(createInfixIdentifier(id))
            : super.get(id);
    }
    
    protected static String createInfixIdentifier(String id) {
        return "$infix_" + id;
    }
    
    @Override
    public String toString() {
        final StringBuffer result = new StringBuffer();
        result.append(getClass().getName());
        result.append('@');
        result.append(Integer.toHexString(hashCode()));
        result.append(" contains ");
        result.append(getSize());
        result.append(" constraint(s):\n");
        
        for (String id : getTable().keySet()) {
            result.append('\t');
            result.append(id);
            result.append(" ==> ");
            result.append(getTable().get(id));
            result.append('\n');
        }
        
        return result.toString();
    }
}