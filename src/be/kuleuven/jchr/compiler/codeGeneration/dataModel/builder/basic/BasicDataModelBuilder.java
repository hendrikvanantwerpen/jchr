package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.StackBasedDataModelBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.util.methods.CreatePartnerConstraintsStoreMethod;
import be.kuleuven.jchr.compiler.codeGeneration.util.methods.IsPropagationRuleMethod;
import be.kuleuven.jchr.util.builder.BuilderException;



/**
 * @author Peter Van Weert
 */
public class BasicDataModelBuilder 
    extends StackBasedDataModelBuilder
    implements IBasicDataModelBuilder<DataModel> {
    
    public void abort() {
        // NOP
    }
        
    public void finish() {
        // NOP
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#init()
     */
    @Override
    public void init() throws BuilderException {
        super.init();
        
        final int ROOT_SIZE = 5;
        
        createRoot(ROOT_SIZE);
        
        peek().put("isPropagationRule", IsPropagationRuleMethod.getInstance());
        peek().put("createPartnerConstraintsStore", CreatePartnerConstraintsStoreMethod.getInstance());
    }
    
    public void buildHandler(String name) throws BuilderException {
        peek().put("handler", name);
    }
    
    public void beginTypeParameters(int nbTypeParameters) throws BuilderException {
        beginList(nbTypeParameters, "typeParameters");
    }

    public void beginTypeParameter(String name) throws BuilderException {
        final int TYPE_PARAMETER_SIZE = 2;
        
        final Map<String, Object> typeParameter = new HashMap<String, Object>(TYPE_PARAMETER_SIZE);
        typeParameter.put("name", name);
        peek().add(typeParameter);        
        push(typeParameter);
    }
    
    public void beginUpperBounds(int nbBounds) throws BuilderException {
        beginList(nbBounds, "upperBounds");
    }

    public void addUpperBound(String type) throws BuilderException {
        peek().add(type);
    }
    
    public void endUpperBounds() throws BuilderException {
        pop();
    }
    
    public void endTypeParameter() throws BuilderException {
        pop();
    }
    
    public void endTypeParameters() throws BuilderException {
        pop();
    }
    
    public void beginSolvers(int nbrSolvers) throws BuilderException {
        beginList(nbrSolvers, "solvers");
    }
    
    /** 
     * @see compiler.codeGeneration.CodeBuilder#addSolver(null, compiler.Identifier)
     */
    public void addSolver(String interface_, String identifier) throws BuilderException {
        final int SOLVER_SIZE = 2;
        
        final Map<String, Object> solver = new HashMap<String, Object>(SOLVER_SIZE);
        solver.put("identifier", identifier);
        solver.put("interface", interface_);
        peek().add(solver);
    }

    /** 
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#endSolvers()
     */
    public void endSolvers() throws BuilderException {
        pop();
    }
    
    public void beginVariableTypes(int nbVariableTypes) throws BuilderException {
        beginList(nbVariableTypes, "variableTypes");
    }
    
    public void beginVariableType(String type, boolean fixed) throws BuilderException {
        final int VARIABLE_TYPE_SIZE = 3;
        final Map<String, Object> variableType = new HashMap<String, Object>(VARIABLE_TYPE_SIZE);
        variableType.put("type", type);
        variableType.put("fixed", Boolean.valueOf(fixed));
        peek().add(variableType);
        push(variableType);
    }
    
    public IInsertionPoint getVariableTypeEqConstraintInsertionPoint() throws BuilderException {
        return getCurrentInsertionPoint("eq");
    }
    
    public void endVariableType() throws BuilderException {
        pop();
    }
    
    public void endVariableTypes() throws BuilderException {
        pop();
    }
    

    public void beginConstraints(int nbrConstraints)  throws BuilderException {
        beginMap(nbrConstraints, "constraints");
    }
    
    public IInsertionPoint getConstraintsInsertionPoint() throws BuilderException {
        return getCurrentInsertionPoint();
    }

    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#endConstraints()
     */
    public void endConstraints() throws BuilderException {
        pop();
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#beginRules(int)
     */
    public void beginRules(int nbrRules) throws BuilderException {
        beginList(nbrRules, "rules");
    }
    
    /** 
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#beginRule(be.kuleuven.compiler.Identifier, byte)
     */
    public void beginRule(String identifier, RuleType type, boolean endsWithFailure) throws BuilderException {
        final int RULE_SIZE = 6;
        
        final Map<String, Object> rule = new HashMap<String, Object>(RULE_SIZE);
        rule.put("identifier", identifier);
        rule.put("type", type.ordinal());
        rule.put("endsWithFailure", endsWithFailure);
        peek().add(rule);
        push(rule);
    }

    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#beginHead(int)
     */
    public void beginHead(int nbrOccurrences) throws BuilderException {
        beginList(nbrOccurrences, "head");
    }
    
    public IInsertionPoint getOccurrencesInsertionPoint() throws BuilderException {
        return getCurrentInsertionPoint();
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#endHead()
     */
    public void endHead() throws BuilderException {
        pop();
    }    
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#beginGuard(int)
     */
    public void beginGuard(int nbrChecks) throws BuilderException {
        beginList(nbrChecks, "guard");
    }
    
    public IInsertionPoint getGuardConjunctsInsertionPoint() throws BuilderException {
        return getCurrentInsertionPoint();
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#endGuard()
     */
    public void endGuard() throws BuilderException {
        pop();
    }    
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#beginBody(int)
     */
    public void beginBody(int nbStatements) throws BuilderException {
        beginList(nbStatements, "body");
    }
    
    public IInsertionPoint getBodyConjunctsInsertionPoint() throws BuilderException {
        return getCurrentInsertionPoint();
    }
    
    public void insertFailure() throws BuilderException {
        final int FAILURE_SIZE = 1;
        
        beginMap(FAILURE_SIZE);
        pop().put("type", "failure");
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#endBody()
     */
    public void endBody() throws BuilderException {
        pop();
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#endRule()
     */
    public void endRule() throws BuilderException {
        pop();
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#endRules()
     */
    public void endRules() throws BuilderException {
        pop();
    }

    public void buildPackage(String name) throws BuilderException {
        peek().put("chrpackage", name);
    }
}