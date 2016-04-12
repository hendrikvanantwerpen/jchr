package be.kuleuven.jchr.compiler.analysis.scheduling;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;


public class GuardWrapper extends Observable implements Observer {

    private IGuardConjunct guard;
    
    private int nbVariables;
    
    private int nbUnknownVariables;

    public GuardWrapper(IGuardConjunct guard, Map<Variable, VariableWrapper> variableMap) {
        setGuard(guard);
        
        if (guard instanceof IArgumented<?>) {
            final Set<Variable> variables = 
                ((IArgumented<?>)guard).getVariablesInArguments();
            setNbVariables(variables.size());
            for (Variable variable : variables)
                variableMap.get(variable).addObserver(this);
        } /* else {
            setNbVariables(0);
        } */
        
        reset();
    }
    
    public void reset() {
        clearChanged();
        setNbUnknownVariables(getNbVariables());        
    }
    
    public void update(Observable o, Object argument) {
        variableUsed((Variable)argument);
    }
    
    protected void variableUsed(Variable variable) {
        decrementNbUnknownVariables();
        checkAllKnown();
    }

    /**
     * Checks whether all the guards variables are known already. If
     * this is the case all the observers are notified. 
     */
    public void checkAllKnown() {
        if (getNbUnknownVariables() == 0) {
            setChanged();
            notifyObservers();
        }
    }

    protected IGuardConjunct getGuard() {
        return guard;
    }
    protected void setGuard(IGuardConjunct guard) {
        this.guard = guard;
    }

    protected int getNbVariables() {
        return nbVariables;
    }
    protected void setNbVariables(int nbVariables) {
        this.nbVariables = nbVariables;
    }
    
    protected int getNbUnknownVariables() {
        return nbUnknownVariables;
    }
    protected void decrementNbUnknownVariables() {
        setNbUnknownVariables(getNbUnknownVariables() - 1);
    }
    protected void setNbUnknownVariables(int nbUnknownVariables) {
        this.nbUnknownVariables = nbUnknownVariables;
    }
}