package be.kuleuven.jchr.compiler.CHRIntermediateForm;

import java.util.Collection;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;


public class CHRIntermediateFormDecorator implements ICHRIntermediateForm {
    private ICHRIntermediateForm intermediateForm;
    
    protected CHRIntermediateFormDecorator() {
        // NOP
    }
    
    public CHRIntermediateFormDecorator(ICHRIntermediateForm intermediateForm) {
        setIntermediateForm(intermediateForm);
    }
    
    protected ICHRIntermediateForm getCHRIntermediateForm() {
        return intermediateForm;
    }

    protected void setIntermediateForm(ICHRIntermediateForm intermediateForm) {
        this.intermediateForm = intermediateForm;
    }
    
    public Collection<UserDefinedConstraint> getUserDefinedConstraints() {
        return getCHRIntermediateForm().getUserDefinedConstraints();
    }
    public int getNbUdConstraints() {
        return getCHRIntermediateForm().getNbUdConstraints();
    }

    public Collection<Rule> getRules() {
        return getCHRIntermediateForm().getRules();
    }
    public int getNbRules() {
        return getCHRIntermediateForm().getNbRules();
    }

    public Collection<Variable> getVariables() {
        return getCHRIntermediateForm().getVariables();
    }
    public int getNbVariables() {
        return getCHRIntermediateForm().getNbVariables();
    }
    
    public Collection<Solver> getSolvers() {
        return getCHRIntermediateForm().getSolvers();
    }
    public int getNbSolvers() {
        return getCHRIntermediateForm().getNbSolvers();
    }
    
    public Handler getHandler() {
        return getCHRIntermediateForm().getHandler();
    }
    public String getHandlerName() {
        return getCHRIntermediateForm().getHandlerName();
    }
    
    public Set<VariableType> getVariableTypes() {
        return getCHRIntermediateForm().getVariableTypes();
    }
    public int getNbVariableTypes() {
        return getCHRIntermediateForm().getNbVariableTypes();
    }

	public String getChrPackage() {
		return getCHRIntermediateForm().getChrPackage();
	}
    
}
