package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType;

public interface ILookupCategory extends Iterable<ILookupType> {
    
    public IndexType getIndexType();
    
    public boolean isMasterCategory();
    public void setMasterCategory();
    
    public boolean contains(ILookupType lookupType);
    public int getIndexOf(ILookupType lookupType);
    public ILookupType getLookupTypeAt(int index);
    
    /**
     * @pre ! contains(lookupType)
     * @throws UnsupportedOperationException
     */
    public void addLookupType(ILookupType lookupType);
    
    public int getNbLookupTypes();
        
    public int[] getVariableIndices();
    
    public int getNbVariables();
}
