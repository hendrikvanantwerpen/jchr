package be.kuleuven.jchr.compiler.CHRIntermediateForm;

import java.util.Collection;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;


/**
 * @author Peter Van Weert
 */
public class CHRIntermediateForm implements ICHRIntermediateForm {
    
	private String chrPkg;
	
    private Handler handler;

    private Set<VariableType> variableTypes;
    
    private Collection<Variable> variables;

    private Collection<UserDefinedConstraint> udConstraints;
    
    private Collection<Rule> rules;
    
    private Collection<Solver> solvers;
    
    public CHRIntermediateForm(
    	String pkg,
        Handler handler, 
        Set<VariableType> variableTypes,
        Collection<Variable> variables,
        Collection<UserDefinedConstraint> udConstraints,
        Collection<Rule> rules,
        Collection<Solver> solvers) {       
        
    	setChrPackage(pkg);
        setHandler(handler);
        setVariableTypes(variableTypes);
        setVariables(variables);
        setUdConstraints(udConstraints);
        setRules(rules);
        setSolvers(solvers);
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm#getUserDefinedConstraints()
     */
    public Collection<UserDefinedConstraint> getUserDefinedConstraints() {
        return  udConstraints;
    }
    public int getNbUdConstraints() {
        return getUserDefinedConstraints().size();
    }

    public Collection<Rule> getRules() {
        return rules;
    }
    public int getNbRules() {
        return getRules().size();
    }

    public Collection<Variable> getVariables() {
        return variables;
    }
    public int getNbVariables() {
        return getVariables().size();
    }

    public Collection<Solver> getSolvers() {
        return solvers;
    }
    public int getNbSolvers() {
        return getSolvers().size();
    }

    @Override
    public String toString() {
        return new StringBuffer()
            .append(getHandler())
            .append("\n\n")
            .append(getVariableTypes())
            .append("\n\n")
            .append(getVariables())
            .append("\n\n")
            .append(getUserDefinedConstraints())
            .append("\n\n")
            .append(getRules())
            .toString();
    }

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm#getHandler()
     */
    public Handler getHandler() {
        return handler;
    }
    public String getHandlerName() {
        return getHandler().getIdentifier();
    }
    protected void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Set<VariableType> getVariableTypes() {
        return variableTypes;
    }
    public int getNbVariableTypes() {
        return getVariableTypes().size();
    }
    protected void setVariableTypes(Set<VariableType> variableTypes) {
        this.variableTypes = variableTypes;
    }

    protected void setRules(Collection<Rule> rules) {
        this.rules = rules;
    }

    protected void setSolvers(Collection<Solver> solvers) {
        this.solvers = solvers;
    }

    protected void setUdConstraints(Collection<UserDefinedConstraint> udConstraints) {
        this.udConstraints = udConstraints;
    }

    protected void setVariables(Collection<Variable> variables) {
        this.variables = variables;
    }

    protected void setChrPackage( String pkg ){
    	this.chrPkg = pkg;
    }
    
    public String getChrPackage(){
    	return chrPkg;
    }
}
