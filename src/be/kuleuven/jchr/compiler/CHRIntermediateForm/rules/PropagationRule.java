package be.kuleuven.jchr.compiler.CHRIntermediateForm.rules;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.PROPAGATION;

import java.util.Collections;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;


/**
 * @author Peter Van Weert
 */
public final class PropagationRule extends Rule {
    protected PropagationRule(String id, int nbr) {
        super(id, nbr);
    }

    /* 
     * @see compiler.CHRIntermediateForm.Rule#getType()
     */
    @Override
    public final RuleType getType() {
        return PROPAGATION;
    }
    
    @Override
    public void addOccurrence(Occurrence occurrence) {
        if (occurrence.isRemoved())
            throw new UnsupportedOperationException("Cannot ad a removed constraint to a simplification rule");
        else
            super.addOccurrence(occurrence);
    }
    @Override
    public List<Occurrence> getKeptOccurrences() {
        return getOccurrences();
    }
    @Override
    public List<Occurrence> getRemovedOccurrences() {
        return Collections.emptyList();
    }
    
    /*
     * @see compiler.CHRIntermediateForm.Rule#isValid()
     */
    @Override
    public boolean isValid() {
        return !hasRemovedOccurrences() 
        	&& hasKeptOccurrences()        	
        	&& hasBodyConjuncts();
    }
}