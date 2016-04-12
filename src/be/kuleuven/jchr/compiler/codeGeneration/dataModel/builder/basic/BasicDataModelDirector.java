package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic;

import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.BasicArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.IConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.Failure;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.TypeParameter;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.VariableType;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented.ArgumentedDataModelExtenderFacade;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.ConstraintDataModelExtenderFacade;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director.CHRIntermediateFormDataModelDirector;
import be.kuleuven.jchr.util.builder.BuilderException;



/**
 * @author Peter Van Weert
 */
public class BasicDataModelDirector
extends CHRIntermediateFormDataModelDirector<BasicDataModelBuilder>
implements IBasicDataModelDirector<BasicDataModelBuilder> {
    
    private ConstraintDataModelExtenderFacade constraintDataModelExtenderFacade;
    
    private ArgumentedDataModelExtenderFacade conjunctDataModelExtenderFacade;
    
    public BasicDataModelDirector(
            BasicDataModelBuilder codeBuilder,
            ICHRIntermediateForm intermediateForm) {
        this(codeBuilder, intermediateForm, 
            ConstraintDataModelExtenderFacade.getDefaultInstance(),
            ArgumentedDataModelExtenderFacade.getDefaultInstance());
    }
    
    public BasicDataModelDirector(
        BasicDataModelBuilder codeBuilder,
        ICHRIntermediateForm intermediateForm,
        ConstraintDataModelExtenderFacade constraintDataModelExtenderFacade,
        ArgumentedDataModelExtenderFacade conjunctDataModelExtenderFacade) {
        
        super(intermediateForm, codeBuilder);
        setConstraintDataModelExtenderFacade(constraintDataModelExtenderFacade);
        setConjunctDataModelExtenderFacade(conjunctDataModelExtenderFacade);        
    }
    
    public void construct() throws BuilderException {
        IBasicDataModelDirector.Helper.constructWith(this);
    }
    
	public void constructPackage() throws BuilderException {
		getBuilder().buildPackage( getChrPackage() );
		
	}    
    public void constructHandler() throws BuilderException {
        getBuilder().buildHandler(getHandler().getIdentifier());
    }
    
    public void constructTypeParameters() throws BuilderException {
        getBuilder().beginTypeParameters(getHandler().getNbTypeParameters());
        for (TypeParameter typeParameter : getHandler().getTypeParameters())
            constructTypeVariable(typeParameter);
        getBuilder().endTypeParameters();        
    }
    
    protected void constructTypeVariable(TypeParameter typeParameter) throws BuilderException {
        getBuilder().beginTypeParameter(typeParameter.getIdentifier());
        getBuilder().beginUpperBounds(typeParameter.getNbUpperBounds());
        for (IType upperBound : typeParameter.getUpperBounds())
            getBuilder().addUpperBound(upperBound.toTypeString());
        getBuilder().endUpperBounds();
        getBuilder().endTypeParameter();
    }
    
    public void constructVariableTypes() throws BuilderException {
        getBuilder().beginVariableTypes(getNbVariableTypes());
        for (VariableType variableType : getVariableTypes()) 
            constructVariableType(variableType);
        getBuilder().endVariableTypes();
    }
    
    protected void constructVariableType(VariableType variableType) throws BuilderException {
        getBuilder().beginVariableType(
            variableType.getTypeString(),
            variableType.isFixed()
        );
        getArgumentedDataModelExtenderFacade().insertConjunct(
            variableType.getEq(), 
            getBuilder().getResult(),
            getBuilder().getVariableTypeEqConstraintInsertionPoint()
        );            
        getBuilder().endVariableType();
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.AbstractDataModelGenerator#constructConstraints()
     */
    public void constructConstraints() throws BuilderException {
        getBuilder().beginConstraints(getNbUdConstraints());
        
        for (UserDefinedConstraint constraint : getUserDefinedConstraints())
            getConstraintDataModelExtenderFacade().insertConstraint(
                getCHRIntermediateForm(),
                constraint, 
                getBuilder().getResult(),
                getBuilder().getConstraintsInsertionPoint()
            );
            
        getBuilder().endConstraints();
    }
    

    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.AbstractDataModelGenerator#constructRules()
     */
    public void constructRules() throws BuilderException {
        getBuilder().beginRules(getNbRules());
        
        for (Rule rule : getRules()) {
            getBuilder().beginRule(rule.getIdentifier(), rule.getType(), rule.endsWithFailure());            
            constructHead(rule);
            constructGuard(rule);
            constructBody(rule);            
            getBuilder().endRule();
        }
        
        getBuilder().endRules();
    }
    
    protected void constructHead(Rule rule) throws BuilderException {
        getBuilder().beginHead(rule.getNbActiveOccurrences());
        
        for (Occurrence occurrence : rule.getOccurrences()) {
            getArgumentedDataModelExtenderFacade().insertOccurrence(
                occurrence, 
                getBuilder().getResult(),
                getBuilder().getOccurrencesInsertionPoint()
            );
        }
                
        getBuilder().endHead();
    }
    

    
    protected void constructBody(Rule rule) throws BuilderException {
        getBuilder().beginBody(rule.getNbBodyConjuncts());        
        constructConjunctList(rule.getBodyConjuncts(), getBuilder().getBodyConjunctsInsertionPoint());        
        getBuilder().endBody();
    }
    
    protected void constructGuard(Rule rule) throws BuilderException {
        getBuilder().beginGuard(rule.getNbGuardConjuncts());
        constructConjunctList(rule.getGuardConjuncts(), getBuilder().getGuardConjunctsInsertionPoint());
        getBuilder().endGuard();
    }
    
    protected <T extends IConjunct> void constructConjunctList(List<T> list, IInsertionPoint insertionPoint) throws BuilderException {
        for (T conjunct : list) {
            if (conjunct instanceof BasicArgumented)
                getArgumentedDataModelExtenderFacade().insertConjunct(
                    (BasicArgumented)conjunct,
                    getBuilder().getResult(),
                    insertionPoint
                );
            else if (conjunct instanceof Failure)
                getBuilder().insertFailure();
            else
                throw new BuilderException("Unknown conjunct: " + conjunct);
        }
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.AbstractDataModelGenerator#constructSolvers()
     */
    public void constructSolvers() throws BuilderException {
        getBuilder().beginSolvers(getNbSolvers());
        for (Solver solver : getSolvers())
            getBuilder().addSolver(solver.toTypeString(), solver.getIdentifier());
        getBuilder().endSolvers();
    }

    protected ConstraintDataModelExtenderFacade getConstraintDataModelExtenderFacade() {
        return constraintDataModelExtenderFacade;
    }
    protected void setConstraintDataModelExtenderFacade(ConstraintDataModelExtenderFacade constraintDataModelExtender) {
        this.constraintDataModelExtenderFacade = constraintDataModelExtender;
    }

    protected ArgumentedDataModelExtenderFacade getArgumentedDataModelExtenderFacade() {
        return conjunctDataModelExtenderFacade;
    }
    protected void setConjunctDataModelExtenderFacade(ArgumentedDataModelExtenderFacade conjunctDataModelExtender) {
        this.conjunctDataModelExtenderFacade = conjunctDataModelExtender;
    }

}