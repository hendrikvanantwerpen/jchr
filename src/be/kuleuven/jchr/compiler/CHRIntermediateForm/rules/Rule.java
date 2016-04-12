package be.kuleuven.jchr.compiler.CHRIntermediateForm.rules;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.PROPAGATION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.IConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.Failure;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;


/**
 * @author Peter Van Weert
 */
public abstract class Rule implements Comparable<Rule> {
    private String identifier;
    
    private int nbr;
            
    private List<IGuardConjunct> guardConjuncts;
    private List<IConjunct> bodyConjuncts;
    private LinkedList<Occurrence> occurrences;
        
    /**
     * @param id
     * @param nbr
     * 
     */
    protected Rule(String id, int nbr) {
        setIdentifier(id);
        setNbr(nbr);
        
        setGuardConjuncts(new ArrayList<IGuardConjunct>());
        setBodyConjuncts(new ArrayList<IConjunct>());
        setOccurrences(new LinkedList<Occurrence>());
    }
    
    /**
     * @pre Rule.isValidType(type)
     */
    public static Rule create(String id, int nbr, RuleType type) {
        switch (type) {
        	case PROPAGATION:	return new PropagationRule(id, nbr);
        	case SIMPAGATION:	return new SimpagationRule(id, nbr);
        	case SIMPLIFICATION:return new SimplificationRule(id, nbr);
        	default: throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
    
    public void addOccurrence(Occurrence occurrence) {
        getOccurrences().add(occurrence);
    }
    
    public void addGuardConstraint(IGuardConjunct conjunct) throws IllegalArgumentException {
        if (! conjunct.canBeAskConjunct())
            throw new IllegalArgumentException("Illegal ask conjunct: " + conjunct);
        getGuardConjuncts().add(conjunct);
    }
    
    public boolean endsWithFailure() {
        return hasBodyConjuncts() 
            && getFinalBodyConjunct() == Failure.getInstance();        
    }
    
    public void addBodyConjunct(IConjunct conjunct) {
        if (endsWithFailure())
            throw new IllegalStateException("Cannot add a conjunct after a failure");
            
        getBodyConjunctsRef().add(conjunct);
    }
    
    public String getIdentifier() {
        return identifier;
    }
    protected void setIdentifier(String id) {
        this.identifier = id;
    }
    
    public abstract RuleType getType();
    
    public List<IConjunct> getBodyConjuncts() {
        return Collections.unmodifiableList(bodyConjuncts);
    }
    protected List<IConjunct> getBodyConjunctsRef() {
        return bodyConjuncts;
    }
    protected void setBodyConjuncts(List<IConjunct> bodyConstraints) {
        this.bodyConjuncts = bodyConstraints;
    }
    public boolean hasBodyConjuncts() {
        return getNbBodyConjuncts() > 0;
    }
    public int getNbBodyConjuncts() {
        return getBodyConjuncts().size();
    }
    public IConjunct getBodyConjunctAt(int index) {
        return getBodyConjuncts().get(index);
    }
    public IConjunct getFinalBodyConjunct() {
        return getBodyConjuncts().get(getNbBodyConjuncts() - 1);
    }
    
    public List<IGuardConjunct> getGuardConjuncts() {
        return guardConjuncts;
    }
    protected void setGuardConjuncts(List<IGuardConjunct> guardConstraints) {
        this.guardConjuncts = guardConstraints;
    }
    public boolean hasGuardConjuncts() {
        return getNbGuardConjuncts() > 0;
    }
    public int getNbGuardConjuncts() {
        return getGuardConjuncts().size();
    }
    public IGuardConjunct getGuardConjunctAt(int index) {
        return getGuardConjuncts().get(index);
    }
    
    public abstract List<Occurrence> getKeptOccurrences();
    
    public boolean hasKeptOccurrences() {
        return getNbKeptOccurrences() > 0;
    }
    public int getNbKeptOccurrences() {
        return getKeptOccurrences().size();
    }
    public Occurrence getKeptOccurrenceAt(int index) {
        return getKeptOccurrences().get(index);
    }
    
    public abstract List<Occurrence> getRemovedOccurrences();
    
    public boolean hasRemovedOccurrences() {
        return getNbRemovedOccurrences() > 0;
    }
    public int getNbRemovedOccurrences() {
        return getRemovedOccurrences().size();
    }
    public Occurrence getRemovedOccurrenceAt(int index) {
        return getRemovedOccurrences().get(index);
    }
    
    public int getNbOccurrences() {
        return getOccurrences().size();
    }
    public int getNbActiveOccurrences() {
        int result = 0;
        for (Occurrence occurrence : getOccurrences())
            if (! occurrence.isPassive()) result++;
        return result;
    }
    public Occurrence getOccurrenceAt(int index) {
        return getOccurrences().get(index);
    }
    public Occurrence getLastOccurence() {
        return getOccurrenceAt(getNbOccurrences() - 1);
    }
    public int getOccurrenceIndex(Occurrence occurrence) {
        final int nbOccurrences = getNbOccurrences();
        for (int i = 0; i < nbOccurrences; i++)
            if (getOccurrenceAt(i) == occurrence)
                return i;
        
        throw new RuntimeException();
    }
    public LinkedList<Occurrence> getOccurrences() {
        return occurrences;
    }
    protected void setOccurrences(LinkedList<Occurrence> headConstraints) {
        this.occurrences = headConstraints;
    }
    
    public abstract boolean isValid();
    
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        
        result.append(getNbr());
        result.append(": ");
        result.append(getIdentifier());
        result.append(" @ ");
        
        if (hasKeptOccurrences()) {
            result.append(getKeptOccurrences());
            if (hasRemovedOccurrences()) result.append(" \\ ");
        }
        if (hasRemovedOccurrences()) result.append(getRemovedOccurrences());
        
        switch (getType()) {
        	case PROPAGATION: 
        	    result.append("==>");
        	break;  
        	case SIMPAGATION:
        	case SIMPLIFICATION:
        	    result.append("<=>");
        	break;
        }
        
        if (hasGuardConjuncts()) {
            result.append(getGuardConjuncts());
            result.append(" | ");
        }
        if (hasBodyConjuncts())
            result.append(getBodyConjuncts());
        else
            result.append("true");
        
        return result.toString();
    }
    
    public int getNbr() {
        return nbr;
    }
    protected void setNbr(int nbr) {
        this.nbr = nbr;
    }
    
    public int compareTo(Rule rule) {
        return nbr - rule.nbr; 
    }
    
    public boolean needsPropagationHistory() {
        return (getType() == PROPAGATION) && (getNbOccurrences() > 1);
    }
}