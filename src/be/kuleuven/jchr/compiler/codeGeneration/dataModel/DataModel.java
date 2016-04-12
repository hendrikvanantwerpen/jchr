package be.kuleuven.jchr.compiler.codeGeneration.dataModel;

import java.util.List;
import java.util.Map;


/**
 * @author Peter Van Weert
 */
public class DataModel {
    private Map<String, Object> root;
    
    /**
     * @param root
     */
    public DataModel(Map<String, Object> root) {
        setRoot(root);
    }
    
    public Map<String, Object> getRoot() {
        return root;
    }
    protected void setRoot(Map<String, Object> root) {
        this.root = root;
    }
    
    /*
     * SOME USEFUL GETTERS (not typesafe...)
     ****************************************/
    public Map getRule(int ruleNbr) 
    throws ClassCastException, IndexOutOfBoundsException, NullPointerException {
        return (Map)((List)getRoot().get("rules")).get(ruleNbr - 1);
    }
    
    public Map getConjunct(int ruleNbr, int index, String list)
    throws ClassCastException, IndexOutOfBoundsException, NullPointerException {
        return (Map)((List)getRule(ruleNbr).get(list)).get(index);
    }
    
    public Map getOccurrence(int ruleNbr, int index)
    throws ClassCastException, IndexOutOfBoundsException, NullPointerException {        
         return getConjunct(ruleNbr, index, "head");
    }
    
    public Map getGuardConjunct(int ruleNbr, int index)
    throws ClassCastException, IndexOutOfBoundsException, NullPointerException {
        return getConjunct(ruleNbr, index, "guard");
    }
    
    public Map getBodyConjunct(int ruleNbr, int index)
    throws ClassCastException, IndexOutOfBoundsException, NullPointerException {
        return getConjunct(ruleNbr, index, "body");
    }
    
    public Map getVariableType(int index)
    throws ClassCastException, IndexOutOfBoundsException, NullPointerException {
        return (Map)((List)getRoot().get("variableTypes")).get(index);
    }
    
    public Map getConstraint(String constraintId)
    throws ClassCastException, IndexOutOfBoundsException, NullPointerException {        
        return (Map)((Map)getRoot().get("constraints")).get(constraintId);
    }
    
    public Map getLookupCategory(String constraintId, int category)
    throws ClassCastException, IndexOutOfBoundsException, NullPointerException {
        return (Map)((List)getConstraint(constraintId).get("lookupCategories")).get(category);
    }
    
    public Map getLookupType(String constraintId, int category, int type)
    throws ClassCastException, IndexOutOfBoundsException, NullPointerException {
        return (Map)((List)getLookupCategory(constraintId, category).get("lookupTypes")).get(type);
    }
}