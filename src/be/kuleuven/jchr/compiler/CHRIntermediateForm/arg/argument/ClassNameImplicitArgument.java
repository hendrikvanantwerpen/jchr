package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument;

import java.lang.reflect.Modifier;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Field;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Method;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;


/**
 * @author Peter Van Weert
 */
public class ClassNameImplicitArgument extends Argument implements IImplicitArgument {
    
    private Class<?> theClass;
    
    public ClassNameImplicitArgument(Class<?> theClass) {
        super(ArgumentType.CLASS_NAME);
        setTheClass(theClass);
    }

    public Set<Method> getMethods(String id) {        
        return Method.getStaticMethods(getTheClass(), id);
    }

    public Field getField(String name) throws AmbiguityException, NoSuchFieldException {
        final java.lang.reflect.Field field = 
            getTheClass().getField(name);
        
        if (! Modifier.isStatic(field.getModifiers()))
            throw new NoSuchFieldException("Field " + name + " is not static");
        else
            return new Field(getType(), field);
    }

    public GenericType getType() {
        return GenericType.getClassInstance(getTheClass());
    }
    
    public boolean isFixed() {
        return true;
    }

    public Class<?> getTheClass() {
        return theClass;
    }
    protected void setTheClass(Class< ? > theClass) {
        this.theClass = theClass;
    }
    
    public String getName() {
        return getTheClass().getName();
    }
    
    @Override
    public String toString() {
        return getName();
    }    
}