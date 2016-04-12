package be.kuleuven.jchr.compiler.analysis.scheduling;

import java.util.Observable;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;


public class VariableWrapper extends Observable {

    private Variable variable;
    
    public VariableWrapper(Variable variable) {
        setVariable(variable);
    }
    
    public void reset() {
        clearChanged();
    }
    public void tellKnown() {        
        setChanged();
        notifyObservers(getVariable());
    }
    
    protected Variable getVariable() {
        return variable;
    }
    protected void setVariable(Variable variable) {
        this.variable = variable;
    }
    
    @Override
    public String toString() {
        return getVariable() + " (observed by " + countObservers() + " oberver(s))";
    }
}