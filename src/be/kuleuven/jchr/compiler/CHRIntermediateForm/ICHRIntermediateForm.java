package be.kuleuven.jchr.compiler.CHRIntermediateForm;

import java.util.Collection;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;


public interface ICHRIntermediateForm {

    public Collection<UserDefinedConstraint> getUserDefinedConstraints();
    public int getNbUdConstraints();

    public Collection<Rule> getRules();
    public int getNbRules();
    
    public Collection<Variable> getVariables();
    public int getNbVariables();

    public Collection<Solver> getSolvers();
    public int getNbSolvers();

    public Handler getHandler();
    public String getHandlerName();
    
    public String getChrPackage();

    public Set<VariableType> getVariableTypes();
    public int getNbVariableTypes();
}