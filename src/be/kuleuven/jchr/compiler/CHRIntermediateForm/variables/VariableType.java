package be.kuleuven.jchr.compiler.CHRIntermediateForm.variables;

import java.util.HashMap;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IBasicArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalVariableTypeException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.util.Cloneable;
import be.kuleuven.jchr.util.builder.Current;



public class VariableType implements Cloneable<VariableType> {
    private IType type;
    
    private boolean fixed;
    
    private Current<IBasicArgumented> eq = new Current<IBasicArgumented>();
    
    protected VariableType() {
        // NOP
    }

    protected VariableType(IType type, boolean fixed) throws IllegalVariableTypeException {
        init(type, fixed);
        if (! isValid())
            throw new IllegalVariableTypeException(this);
    }
    
    protected void init(IType type, boolean fixed) {
        setType(type);
        setFixed(fixed);
    }
    
    private final static VariableType LOOKUP = new VariableType();
    private final static HashMap<VariableType, VariableType> table 
        = new HashMap<VariableType, VariableType>();
    
    public static VariableType getInstance(IType type, boolean fixed) throws IllegalVariableTypeException {
        LOOKUP.init(type, fixed);
        VariableType result = table.get(LOOKUP);
        if (result == null) {
            result = new VariableType(type, fixed);
            table.put(result, result);
        }
        return result;
    }
    
    public boolean isFixed() {
        return fixed || getType().isFixed();
    }
    protected void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
    
    public boolean canBeUsedInHashMap() {
        return isFixed() || isHashObservable();
    }
    
    public boolean isObservable() {
        return getType().isObservable();
    }
    
    public boolean isHashObservable() {
        return getType().isHashObservable();
    }

    public String getTypeString() {
        return getType().toTypeString();
    }
    public IType getType() {
        return type;
    }
    protected void setType(IType type) {
        this.type = type;
    }
    
    public Variable getInstance(String identifier) {
        return new Variable(this, identifier);
    }
    
    public boolean hasInitializedBuiltInConjuncts() {
        return hasInitializedEq();
    }

    public IBasicArgumented getEq() throws IllegalStateException {
        return eq.get();
    }
    public boolean hasInitializedEq() {
        return eq.isSet();
    }
    public void initEq(IBasicArgumented eq) throws IllegalStateException {
        this.eq.set(eq);
    }
    
    @Override
    public boolean equals(Object other) {
        return (this == other) 
            || (other instanceof VariableType) && this.equals((VariableType)other);
    }
    public boolean equals(VariableType other) {
        return (this.isFixed() == other.isFixed())
            && this.getType().equals(other.getType());
    }
    
    @Override
    public int hashCode() {
        return 37 * (37 * (23) + (fixed? 1 : 0)) + type.hashCode(); 
    }
    
    @Override
    public VariableType clone() {
        try {
            return (VariableType)super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new InternalError();
        }
    }
    
    public boolean isValid() {
        return isFixed() || isObservable();
    }
    
    @Override
    public String toString() {
        return (isFixed()? "fixed " : "")
             + (isObservable()? "observable " : "")
             + (isHashObservable()? "hashObservable " : "")
             + "variable "
             + getTypeString();
    }
}
