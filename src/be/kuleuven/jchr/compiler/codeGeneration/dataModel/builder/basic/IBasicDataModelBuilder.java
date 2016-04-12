package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IDataModelBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.util.builder.BuilderException;


/**
 * @author Peter Van Weert
 */
public interface IBasicDataModelBuilder<Result> extends IDataModelBuilder<Result> {

/* HANDLER */
public void buildHandler(String name) throws BuilderException;
    
/* TYPEVARIABLES */
public void beginTypeParameters(int nbTypeParameters) throws BuilderException;

    public void beginTypeParameter(String name) throws BuilderException;
    
        public void beginUpperBounds(int nbBounds) throws BuilderException;

            public void addUpperBound(String type) throws BuilderException;
        
        public void endUpperBounds() throws BuilderException;
        
    public void endTypeParameter() throws BuilderException;

public void endTypeParameters() throws BuilderException;
        
/* SOLVERS */
public void beginSolvers(int nbSolvers) throws BuilderException;
	
	public void addSolver(String solverType, String solverName) throws BuilderException;

public void endSolvers() throws BuilderException;

/* VARIABLETYPES */
public void beginVariableTypes(int nbVariableTypes) throws BuilderException;

    public void beginVariableType(String type, boolean fixed) throws BuilderException;
    
        public IInsertionPoint getVariableTypeEqConstraintInsertionPoint() throws BuilderException;
    
    public void endVariableType() throws BuilderException;

public void endVariableTypes() throws BuilderException;

/* CONSTRAINTS */
public void beginConstraints(int nbConstraints) throws BuilderException;

    public IInsertionPoint getConstraintsInsertionPoint() throws BuilderException;

public void endConstraints() throws BuilderException;


/* RULES */
public void beginRules(int nbRules) throws BuilderException;

	public void beginRule(String identifier, RuleType type, boolean endsWithFailure) throws BuilderException;
	
		public void beginHead(int nbOccurrences) throws BuilderException;
			
            public IInsertionPoint getOccurrencesInsertionPoint() throws BuilderException;
        
		public void endHead() throws BuilderException;
		
		public void beginGuard(int nbChecks) throws BuilderException;
			
            public IInsertionPoint getGuardConjunctsInsertionPoint() throws BuilderException;
		
		public void endGuard() throws BuilderException;
		
		public void beginBody(int nbStatements) throws BuilderException;

            public IInsertionPoint getBodyConjunctsInsertionPoint() throws BuilderException;
            
            public void insertFailure() throws BuilderException;
        
		public void endBody() throws BuilderException;
		
	public void endRule() throws BuilderException;
	
public void endRules() throws BuilderException;

}