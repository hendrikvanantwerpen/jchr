package be.kuleuven.jchr.compiler.codeGeneration.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Peter Van Weert
 */
public class PartnerConstraintsStore {
    private Map<String, List<String>> store;
    
    public PartnerConstraintsStore() {
        setStore(new HashMap<String, List<String>>());
    }
    
    public void add(String constraintId, String occurrenceId) {        
        List<String> list = getStore().get(constraintId);
        if (list == null) 
            getStore().put(constraintId, list = new ArrayList<String>(4));
        list.add(occurrenceId);
    }
    
    public void reset() {
        for (List list : getStore().values()) list.clear();
    }
    
    public void reset(String constraintId, String occurrenceId) {
        reset();
        add(constraintId, occurrenceId);
    }
    
    public boolean contains(String constraintId, String occurrenceId) {
        final List<String> list = getStore().get(constraintId);
        return ((list != null) && (list.contains(occurrenceId)));
    }
    
    public List getIdentifiers(String constraintId) {
        return getStore().get(constraintId);
    }    

    /**
     * @return Returns the store.
     */
    protected Map<String, List<String>> getStore() {
        return store;
    }

    /**
     * @param store
     *            The store to set.
     */
    protected void setStore(Map<String, List<String>> store) {
        this.store = store;
    }
}
