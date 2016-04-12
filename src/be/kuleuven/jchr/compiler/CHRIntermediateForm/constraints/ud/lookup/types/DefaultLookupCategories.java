package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.types;

import java.util.Iterator;
import java.util.NoSuchElementException;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.DefaultLookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType;
import be.kuleuven.jchr.util.iterator.SingletonIterator;



public class DefaultLookupCategories implements ILookupCategories {
    
    private DefaultLookupCategories() {/* SINGLETON */}
    private static DefaultLookupCategories instance;
    public static DefaultLookupCategories getInstance() {
        if (instance == null)
            instance = new DefaultLookupCategories();

        return instance;
    }
    
    public Iterator<ILookupCategory> iterator() {
        return new SingletonIterator<ILookupCategory>(
            DefaultLookupCategory.getInstance()
        );
    }
    
    public int getNbLookupCategories() {
        return 1;
    }

    public boolean contains(ILookupCategory category) {
        return category == DefaultLookupCategory.getInstance();
    }
    public boolean contains(IndexType indexType, int[] variableIndices) {
        return indexType == IndexType.NONE 
            && variableIndices != null 
            && variableIndices.length == 0;
    }
    
    public void addLookupCategory(ILookupCategory category) {
        throw new UnsupportedOperationException();
    }
    
    public ILookupCategory getLookupCategory(IndexType indexType,int[] variableIndices) {
        return contains(indexType, variableIndices)
            ? DefaultLookupCategory.getInstance()
            : null;
    }
    
    public int getIndexOf(ILookupCategory lookupCategory) throws NoSuchElementException {
        if (lookupCategory == DefaultLookupCategory.getInstance())
            return 0;
        else
            throw new NoSuchElementException();
    }
    
    public ILookupCategory[] toArray() {
        return new ILookupCategory[] { DefaultLookupCategory.getInstance() };
    }
}
