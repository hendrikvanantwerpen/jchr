package be.kuleuven.jchr.compiler.codeGeneration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;


public final class TupleArities {
    
    private TupleArities() {
        // NOP
    }
    
    public static Set<Integer> getAllTupleArities(ICHRIntermediateForm intermediateForm) {
        return getTupleArities(intermediateForm.getRules());
    }
    
    public static Set<Integer> getTupleArities(UserDefinedConstraint constraint) {
        return getTupleArities(constraint.getRules());
    }
    
    private static Set<Integer> getTupleArities(final Collection<Rule> rules) {
        final Set<Integer> arities = new HashSet<Integer>();
        for (Rule rule : rules)
            if (rule.needsPropagationHistory())
                arities.add(rule.getNbOccurrences());
        return arities;
    }
}
