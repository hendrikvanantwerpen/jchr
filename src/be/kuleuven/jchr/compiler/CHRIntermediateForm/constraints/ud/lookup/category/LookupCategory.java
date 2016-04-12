package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType;
import be.kuleuven.jchr.util.Cloneable;



public class LookupCategory implements ILookupCategory, Cloneable<LookupCategory> {
    
    private List<ILookupType> lookupTypes;
    
    private int[] variableIndices;
    
    private IndexType indexType;
    
    private boolean forcedMasterCategory;
    
    protected LookupCategory() {
        // NOP (just indicating this constructor has to be protected)
    }
    
    public boolean isMasterCategory() {
        return (indexType == IndexType.NONE)
            || isForcedMasterCategory();
    }
    public void setMasterCategory() {
        setForcedMasterCategory(true);
    }
    
    /**
     * @param lookupTypes
     * @param variableIndices
     * @param indexType
     */
    public LookupCategory(IndexType indexType, int[] variableIndices) {
        init(indexType, variableIndices);
    }

    protected void init(IndexType indexType, int[] variableIndices) 
    throws UnsupportedOperationException {
        setLookupTypes(new ArrayList<ILookupType>());
        setIndexType(indexType);
        setVariableIndices(variableIndices);
    }
    
    public Iterator<ILookupType> iterator() {
        return getLookupTypes().iterator();
    }

    public List<ILookupType> getLookupTypes() {
        return lookupTypes;
    }
    public void addLookupType(ILookupType lookupType) {
        getLookupTypes().add(lookupType);
    }
    protected void setLookupTypes(List<ILookupType> lookupTypes) {
        this.lookupTypes = lookupTypes;
    }
    
    public int getNbLookupTypes() {
        return getLookupTypes().size();
    }
    
    public int getIndexOf(ILookupType lookupType) {
        return getLookupTypes().indexOf(lookupType);
    }
    public boolean contains(ILookupType lookupType) {
        return getLookupTypes().contains(lookupType);
    }
    public ILookupType getLookupTypeAt(int index) {
        return getLookupTypes().get(index);
    }

    public int[] getVariableIndices() {
        return variableIndices;
    }
    protected void setVariableIndices(int[] variableIndices) {
        this.variableIndices = variableIndices;
    }
    public int getNbVariables() {
        return getVariableIndices().length;
    }
    
    public IndexType getIndexType() {
        return indexType;
    }
    protected void setIndexType(IndexType indexType) {
        this.indexType = indexType;
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof ILookupCategory)
            && Arrays.equals(this.getVariableIndices(), ((ILookupCategory)other).getVariableIndices());
    }
    
    @Override
    public LookupCategory clone() {
        try {
            return (LookupCategory)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public boolean isForcedMasterCategory() {
        return forcedMasterCategory;
    }
    public void setForcedMasterCategory(boolean forcedMasterCategory) {
        this.forcedMasterCategory = forcedMasterCategory;
    }
}