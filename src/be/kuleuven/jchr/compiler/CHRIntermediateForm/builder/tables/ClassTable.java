package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.Identifier;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguousIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalIdentifierException;
import be.kuleuven.jchr.util.builder.BuilderException;


/**
 * @author Peter Van Weert
 */
public class ClassTable extends SymbolTable<Class<?>> {
    public ClassTable() {
        setImportedPackages(new HashSet<String>());
        setDefaultPackages(new ArrayList<String>());
        /**
         * Order is important. First one wins.
         */
        addDefaultPackage(JAVA_LANG);
        addDefaultPackage(TOOL_NAMESPACE);        
        addDefaultPackage(TOOL_NAMESPACE + ".runtime");        
        addDefaultPackage(TOOL_NAMESPACE + ".util");        
    }
    
    private Set<String> importedPackages;
    private ArrayList<String> defaultPackages;
    
    private final static String JAVA_LANG = "java.lang";
    private final static String TOOL_NAMESPACE = "be.kuleuven.jchr";
    
    protected Class<?> forName(String id) throws ClassNotFoundException {
        return Class.forName(id, false, getClass().getClassLoader());
    }

    protected Class<?> forNameOrNull(String id){
    	try{
    		return forName(id);
    	}catch( ClassNotFoundException e ){
    		return null;
    	}
    }

    protected Class<?> forDefNameOrNull(String id){
    	Class<?> result = null;
    	for( String pkg: getDefaultPackages() ){
    		result = forNameOrNull(pkg + id);
    		if( result != null ) return result;
    	}
    	return null;
    }
    
    public Class<?> getClass(String id) 
    throws ClassNotFoundException, AmbiguousIdentifierException {
        if (isDeclaredId(id)) return super.get(id);
        
        Class<?> result = getClassOrNull(id);
        if (result == null) throw new ClassNotFoundException(id);
        return result;
    }
    
    protected Class<?> getClassOrNull(String id) throws AmbiguousIdentifierException {
        Class<?> result = getClassOrNull2(id);
        if( result != null ) return result;
        
        result = getDefClassOrNull2(id);
        if (result != null) return result;
        
        Class<?> temp;        
        for (String importedPackage_dot : getImportedPackages()) {
            temp = getClassOrNull2(importedPackage_dot + id);
            if (temp != null) {
                if (result != null)
                    throw new AmbiguousIdentifierException(id);
                else return temp;
            }
        }        
        return null;
    }
    
    /*
     * @pre composedId is een composed identifier
     */
    protected Class<?> getClassOrNull2(String composedId) {
        try {
            return ensureDeclared(composedId, forName(composedId));
        } catch (ClassNotFoundException cnfe) {
            return null;
        }
    }
    
    /*
     * @pre composedId is een composed identifier
     */
    protected Class<?> getDefClassOrNull2(String composedId) {
    	Class<?> result = null;
    	for( String pkg : getDefaultPackages() ){
    		result = getClassOrNull2(pkg + composedId);
    		if( result != null ) return result;
    	}
    	return null;
    }

    public void importPackage(String id) {
        if (! id.equals(JAVA_LANG))
            getImportedPackages().add(id + '.');
    }

    public void addDefaultPackage(String id) {
        getDefaultPackages().add(id + '.');
    }

    public Class<?> importClass(String id) 
    throws ClassNotFoundException, NoClassDefFoundError, 
        IllegalIdentifierException, DuplicateIdentifierException {
        
        if (Identifier.isSimple(id))
            throw new IllegalIdentifierException(
                "Cannot import classes from the nameless package: " + id
            );
        
        if (isDeclaredId(id)) return get(id);
        
        Class<?> theClass = forNameOrNull(id);
        if( theClass == null ){
        	theClass = forDefNameOrNull(id);
        	if( theClass == null ){
        		throw new ClassNotFoundException(id);
        	}
        }
        
        declareSafe(id, theClass);
        declare(Identifier.getTail(id), theClass);

        return theClass;
    }

    public boolean isImported(String id) {
        return isDeclaredId(Identifier.getTail(id));
    }

    public Set<String> getImportedPackages() {
        return importedPackages;
    }

    protected void setImportedPackages(Set<String> importedPackages) {
        this.importedPackages = importedPackages;
    }

    public ArrayList<String> getDefaultPackages() {
        return defaultPackages;
    }

    protected void setDefaultPackages(ArrayList<String> defaultPackages) {
        this.defaultPackages = defaultPackages;
    }
}