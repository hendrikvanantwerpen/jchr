package be.kuleuven.jchr.compiler.CHRIntermediateForm.members;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.Argumentable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.reflect.Reflection;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.util.IndexOutOfBoundsException;


/**
 * @author Peter Van Weert
 */
public class Field extends Argumentable<Field> {
    
    private GenericType base;
    
    private java.lang.reflect.Field field;
    
    public Field(GenericType base, String name) throws NoSuchFieldException {
        this(base, base.getRawType().getField(name));
    }
    
    public Field(GenericType base, java.lang.reflect.Field field) {        
        setBase(base);
        setField(field);
    }

    @Override
    public String toString() {
        return getBase().toString() + "." + getName();
    }

    public IType getType() {
        return Reflection.reflect(getBase(), getField().getGenericType());
    }
    
    public GenericType getBase() {
        return base;
    }
    protected void setBase(GenericType base) {
        this.base = base;
    }
    
    public java.lang.reflect.Field getField() {
        return field;
    }
    protected void setField(java.lang.reflect.Field field) {
        this.field = field;
    }
    
    public String getName() {
        return getField().getName();
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof Field)
        	&& this.getField().equals(((Field)other).getField());
    }
    
    @Override
    public IType[] getFormalParameters() {
        return new IType[] { getBase() };
    }
    public int getArity() {
        return 1;
    }
    @Override
    public MatchingInfos canHaveAsArguments(IArguments arguments) {
        if ((arguments.getArity() != 1) || !arguments.hasImplicitArgument())
            return MatchingInfos.NO_MATCH;
        else 
            return new MatchingInfos(arguments.getArgumentAt(0).isAssignableTo(getBase()), true);
    }
    public IType getFormalParameterAt(int index) {
        if (index != 0) 
            throw new IndexOutOfBoundsException(index, 0, 0);
        else
            return getBase();
    }
    public FieldAccess getInstance(IArguments arguments) {
        return new FieldAccess(this, arguments);
    }
    
    public boolean haveToIgnoreImplicitArgument() {
        return true;
    }
}
