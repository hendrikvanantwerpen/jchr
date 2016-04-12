package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.types;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType;

public interface ILookupCategories extends Iterable<ILookupCategory> {
    
    public ILookupCategory getLookupCategory(IndexType indexType, int[] variableIndices);
    
    /**
     * @pre !contains(category)
     * @throws UnsupportedOperationException
     */
    public void addLookupCategory(ILookupCategory category);
   
    public boolean contains(ILookupCategory category);    
    public boolean contains(IndexType indexType, int[] variableIndices);
    
    public int getIndexOf(ILookupCategory lookupCategory);
    
    public int getNbLookupCategories();
    
    public ILookupCategory[] toArray();
}
