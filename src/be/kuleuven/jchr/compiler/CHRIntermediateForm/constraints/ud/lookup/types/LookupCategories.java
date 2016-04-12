package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.LookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType;


public class LookupCategories implements ILookupCategories {
    private List<ILookupCategory> lookupCategories = new ArrayList<ILookupCategory>();
    
    public Iterator<ILookupCategory> iterator() {
        return getLookupCategories().iterator();
    }

    public List<ILookupCategory> getLookupCategories() {
        return lookupCategories;
    }
    protected void setLookupCategories(List<ILookupCategory> lookupCategories) {
        this.lookupCategories = lookupCategories;
    }
    
    public int getNbLookupCategories() {
        return getLookupCategories().size();
    }
    
    public int getIndexOf(ILookupCategory lookupCategory) {
        return getLookupCategories().indexOf(lookupCategory);
    }
    
    public boolean contains(ILookupCategory category) {
        return getLookupCategories().contains(category);
    }
    public boolean contains(IndexType indexType, int[] variableIndices) {        
        return contains(LookupLookupCategory.getInstance(indexType, variableIndices));
    }
    
    public void addLookupCategory(ILookupCategory category) {
        getLookupCategories().add(category);
    }
    
    public ILookupCategory getLookupCategory(IndexType indexType,int[] variableIndices) {
        final int index = getIndexOf(LookupLookupCategory.getInstance(indexType, variableIndices));        
        return index >= 0
            ? getLookupCategories().get(index)
            : null;
    }
    
    protected final static class LookupLookupCategory extends LookupCategory {
        private LookupLookupCategory() {/* SINGLETON */}
        private static LookupLookupCategory instance;
        public static LookupLookupCategory getInstance(IndexType indexType, int[] variableIndices) {
            if (instance == null)
                instance = new LookupLookupCategory();
            instance.init(indexType, variableIndices);
            return instance;
        }
    }
    
    public ILookupCategory[] toArray() {
        return getLookupCategories().toArray(new ILookupCategory[getNbLookupCategories()]);
    }
}
