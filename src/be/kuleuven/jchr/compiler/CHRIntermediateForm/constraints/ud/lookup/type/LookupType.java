package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type;

import java.util.Arrays;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;


public abstract class LookupType implements ILookupType {
    
    private ILookupCategory lookupCategory;

    public LookupType(ILookupCategory lookupCategory) {
        setLookupCategory(lookupCategory);
    }
    
    public ILookupCategory getCategory() {
        return lookupCategory;
    }
    protected void setLookupCategory(ILookupCategory lookupCategory) {
        this.lookupCategory = lookupCategory;
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof LookupType)
            && this.equals((LookupType)other);
    }
    
    public boolean equals(LookupType other) {
        return this.getCategory().equals(other.getCategory());
    }
    
    @Override
    public String toString() {
        return getCategory().getIndexType().toString()
            + ':'
            + Arrays.toString(getCategory().getVariableIndices());
    }
}
