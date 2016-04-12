package be.kuleuven.jchr.compiler.CHRIntermediateForm.variables;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ArgumentType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.JavaConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IDeclarator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.CoerceMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Field;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Method;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;


/**
 * @author Peter Van Weert
 */
public class Variable implements IImplicitArgument {
    private static Variable anonymous;
    public static Variable getAnonymousInstance() {
        if (anonymous == null)
            anonymous = new Variable(null, "_");
        return anonymous;
    }
    
    private String identifier;
    
    private VariableType variableType;
    
    public Variable(VariableType type, String id) {
        setIdentifier(id);
        setVariableType(type);
    }
    
    public String getIdentifier() {
        return identifier;
    }
    protected void setIdentifier(String id) {
        this.identifier = id;
    }

    public IType getType() {
        return getVariableType().getType();
    }
    public VariableType getVariableType() {
        return variableType;
    }
    protected void setVariableType(VariableType type) {
        this.variableType = type;
    }
    
    public ArgumentType getArgumentType() {
        return ArgumentType.VARIABLE;
    }
 
    @Override
    public String toString() {
        return getIdentifier().toString();
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof Variable)
            && ((Variable)object).getIdentifier().equals(getIdentifier());
    }
    
    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }
    
    public MatchingInfo isAssignableTo(IType other) {
        return getType().isAssignableTo(other);
    }
    public boolean isDirectlyAssignableTo(IType other) {
        return getType().isDirectlyAssignableTo(other);
    }
    
    public Field getField(String name) throws AmbiguityException, NoSuchFieldException {
        try {
            return getType().getField(name);
        } catch (NoSuchFieldException nsfe) {
            Field result = null, temp;
            for (CoerceMethod method : getType().getCoerceMethods()) {
                try {
                    temp = method.getReturnType().getField(name);
                    if (result != null)
                        result = temp;
                    else
                        throw new AmbiguityException("Ambiguous field: " + name);
                    
                } catch (NoSuchFieldException nsfe2) {
                    // NOP
                }                
            }
            
            throw nsfe;
        }
    }
    
    public Set<Method> getMethods(String id) {
        Set<Method> result = new HashSet<Method>(getType().getMethods(id));
        for (CoerceMethod method : getType().getCoerceMethods())
            result.addAll(method.getReturnType().getMethods(id));
        return result;
    }

    public boolean isFixed() {
        return getVariableType().isFixed();
    }
    public boolean isHashObservable() {
        return getVariableType().isHashObservable();
    }
    public boolean canBeUsedInHashMap() {
        return getVariableType().canBeUsedInHashMap();
    }
    
    private static int counter = 0;
    public final static String createIdentifier() {
        return "$var" + counter++;
    }
    
    public JavaConjunct getDeclaratorInstance() throws AmbiguityException {
        final IDeclarator declarator = getType().getDeclarator();
        if (declarator == null)
            return null;
        else
            return declarator.getInstance(this);
    }
    
    public JavaConjunct getInitialisingDeclaratorInstanceFrom(IType base) throws AmbiguityException {
        final IDeclarator declarator = getType().getInitialisationDeclaratorFrom(base);
        if (declarator == null)
            return null;
        else
            return declarator.getInstance(this);
    }
    
    public boolean isAnonymous() {
        return getIdentifier().charAt(0) == '_';
    }
}