package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.types.ILookupCategories;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.DefaultSchedule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.ISchedule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;


/**
 * @author Peter Van Weert
 *  
 */
public class Occurrence extends UserDefindedConjunct {
    private Rule rule;
    
    private boolean removed;
    
    private ISchedule schedule;
    
    private boolean passive;
    
    public final static boolean REMOVED = true, KEPT = false;
    
    public Occurrence(Rule rule, UserDefinedConstraint constraint, IArguments args, boolean removed) {        
        super(constraint, args);
        init(rule, removed);
        setSchedule(new DefaultSchedule(this));
    }
    
    protected void init(Rule rule, boolean removed) {
        // LET OP: removed moet eerst ingesteld alvorens addOccurrence
        setRemoved(removed);
        
        setRule(rule);
        rule.addOccurrence(this);
        
        // LET OP: rule moet eerst gezet alvorens addOccurrence!
        getArgumentableType().addOccurrence(this);   
    }
    
    public Rule getRule() {
        return rule;
    }
    protected void setRule(Rule rule) {
        this.rule = rule;
    }
    
    public boolean isKept() {
        return !isRemoved();
    }
    public boolean isRemoved() {
        return removed;
    }
    protected void setRemoved(boolean isRemoved) {
        this.removed = isRemoved;
    }
    
    @Override
    public String toString() {
	    StringBuffer result = new StringBuffer(super.toString());
	    result.append(" ~ removed=").append(isRemoved());	    
	    return result.toString();
	}
    
    public int getRuleNbr() {
        return getRule().getNbr();
    }
    public int getRuleOccurrenceNbr() {
        return getRuleOccurrenceIndex() + 1;
    }
    public int getRuleOccurrenceIndex() {
        return getRule().getOccurrenceIndex(this);
    }
    public int getOccurrenceNbr() {
        return getArgumentableType().getIndexOf(this) + 1;
    }

    public ISchedule getSchedule() {
        return schedule;
    }
    public void setSchedule(ISchedule schedule) {
        this.schedule = schedule;
    }
    
    public List<Variable> getVariables() {
        return getArgumentableType().getVariables();
    }
    
    /*
     * Regels in de CHRIntermediateForm staan in HNF !!!
     */
    @Override
    public Set<Variable> getVariablesInArguments() {
        // bij gebrek aan een degelijke volgorde-behoudende Set-implementatie...
        return new AbstractSet<Variable>() {
            @Override
            public Iterator<Variable> iterator() {
                final Iterator<IArgument> iterator 
                    = getArguments().iterator();
                
                return new Iterator<Variable>() {
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }
                    
                    public Variable next() {
                        return (Variable)iterator.next();
                    }
                    
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size() {
                return getArity();
            }
        };
    }
    @Override
    public int getNbVariableArguments() {
        return getArity();
    }
    
    public ILookupCategories getLookupCategories() {
        return getArgumentableType().getLookupCategories();
    }
    public int getIndexOf(ILookupCategory lookupCategory) {
        return getArgumentableType().getIndexOf(lookupCategory);
    }
    
    public void setPassive() {
        passive = true;
    }
    public boolean isPassive() {
        return passive;
    }
}