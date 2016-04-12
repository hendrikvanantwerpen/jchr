package be.kuleuven.jchr.compiler.CHRIntermediateForm.rules;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.SIMPLIFICATION;

import java.util.Collections;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;


/**
 * @author Peter Van Weert
 */
public final class SimplificationRule extends Rule {
    /**
     * @param id
     * @param nbr
     */
    protected SimplificationRule(String id, int nbr) {
        super(id, nbr);
    }
    
    /*
     * @see compiler.CHRIntermediateForm.Rule#getType()
     */
    @Override
    public final RuleType getType() {
        return SIMPLIFICATION;
    }
       
    @Override
    public void addOccurrence(Occurrence occurrence) {
        if (occurrence.isKept())
            throw new UnsupportedOperationException("Cannot ad a kept constraint to a simplification rule");
        else
            super.addOccurrence(occurrence);
    }
    @Override
    public final List<Occurrence> getRemovedOccurrences() {
        return getOccurrences();
    }
    @Override
    public List<Occurrence> getKeptOccurrences() {
        return Collections.emptyList();
    }
    
    /* (non-Javadoc)
     * @see compiler.CHRIntermediateForm.Rule#isValid()
     */
    @Override
    public boolean isValid() {
        return !hasKeptOccurrences() && hasRemovedOccurrences();
    }
}