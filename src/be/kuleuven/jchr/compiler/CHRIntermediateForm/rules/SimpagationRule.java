package be.kuleuven.jchr.compiler.CHRIntermediateForm.rules;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.SIMPAGATION;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;


/**
 * @author Peter Van Weert
 */
public final class SimpagationRule extends Rule {
    /**
     * @param id
     * @param nbr
     */
    protected SimpagationRule(String id, int nbr) {
        super(id, nbr);
    }
    
    /* 
     * @see compiler.CHRIntermediateForm.Rule#getType()
     */
    @Override
    public final RuleType getType() {
        return SIMPAGATION;
    }
    
    @Override
    public List<Occurrence> getKeptOccurrences() {
        return getHeadOccurrences(Occurrence.KEPT);
    }
    @Override
    public List<Occurrence> getRemovedOccurrences() {
        return getHeadOccurrences(Occurrence.REMOVED);
    }
    private List<Occurrence> getHeadOccurrences(boolean removed) {
        List<Occurrence> result = new ArrayList<Occurrence>();
        for (Occurrence occurrence : getOccurrences())
            if (occurrence.isRemoved() == removed) result.add(occurrence);
        return result;        
    }
    
    @Override
    public boolean hasKeptOccurrences() {
        return hasHeadOccurrences(Occurrence.KEPT);
    }
    @Override
    public boolean hasRemovedOccurrences() {
        return hasHeadOccurrences(Occurrence.REMOVED);
    }
    private boolean hasHeadOccurrences(boolean removed) {
        for (Occurrence occurrence : getOccurrences())
            if (occurrence.isRemoved() == removed) return true;
        return false;
    }
    
    /* (non-Javadoc)
     * @see compiler.CHRIntermediateForm.Rule#isValid()
     */
    @Override
    public boolean isValid() {        
        return hasKeptOccurrences() && hasRemovedOccurrences();
    }
}