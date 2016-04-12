package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.DefaultLookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;

public class DefaultLookupType implements ILookupType {

    private DefaultLookupType() {/* SINGLETON */}    
    private static DefaultLookupType instance;
    public static DefaultLookupType getInstance() {
        if (instance == null)
            instance = new DefaultLookupType();
        return instance;
    }
    
    public ILookupCategory getCategory() {
        return DefaultLookupCategory.getInstance();
    }
    
    @Override
    public String toString() {
        return "Default";
    }
}