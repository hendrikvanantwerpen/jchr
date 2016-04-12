package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category;

import java.util.Iterator;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.DefaultLookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType;
import be.kuleuven.jchr.util.IndexOutOfBoundsException;
import be.kuleuven.jchr.util.iterator.SingletonIterator;



public class DefaultLookupCategory implements ILookupCategory {
    
    private DefaultLookupCategory() {/* SINGLETON */}
    private static DefaultLookupCategory instance;
    public static DefaultLookupCategory getInstance() {
        if (instance == null)
            instance = new DefaultLookupCategory();

        return instance;
    }
    
    public boolean isMasterCategory() {
        return true;
    }
    public void setMasterCategory() {
        // NOP
    }
    
    public IndexType getIndexType() {
        return IndexType.NONE;
    }
    
    public Iterator<ILookupType> iterator() {
        return new SingletonIterator<ILookupType>(
            DefaultLookupType.getInstance()
        );
    }
    
    public int getNbLookupTypes() {
        return 1;
    }
    
    public void addLookupType(ILookupType lookupType) {
        throw new UnsupportedOperationException();
    }
    
    public int getNbVariables() {
        return 0;
    }
    
    public int[] getVariableIndices() {
        return new int[0];
    }
    
    public int getIndexOf(ILookupType lookupType) {
        return (lookupType == DefaultLookupType.getInstance())? 0 : -1;
    }
    public boolean contains(ILookupType lookupType) {
        return (lookupType == DefaultLookupType.getInstance());
    }
    public ILookupType getLookupTypeAt(int index) {
        if (index == 0)
            return DefaultLookupType.getInstance();
        else
            throw new IndexOutOfBoundsException(index, 0);
    }
}
