package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.IndexType;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.lookupType.HashMapArgumentInfosBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.lookupType.NoneArgumentInfosBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender.StackBasedDataModelExtender;
import be.kuleuven.jchr.compiler.codeGeneration.util.builder.BuilderStack;
import be.kuleuven.jchr.compiler.codeGeneration.util.builder.BuilderStack.Node;
import be.kuleuven.jchr.util.builder.BuilderException;



/**
 * @author Peter Van Weert
 */
public class ConstraintDataModelExtender
    extends StackBasedDataModelExtender
    implements IConstraintDataModelExtender {
    
    public ConstraintDataModelExtender(DataModel dataModel, IInsertionPoint insertionPoint) {
        super(dataModel, insertionPoint);
    }

    @Override
    public void init() throws BuilderException {
        super.init();
        setLookupInfoBuilder(new LookupInfoBuilderImpl());
    }   

    public void abort() {
        // NOP
    }

    public void finish() {
        // NOP
    }
    
    public void beginConstraint(String identifier, String infix) throws BuilderException {
        final int CONSTRAINT_SIZE = (infix == null)? 4 : 5;
        createRoot(CONSTRAINT_SIZE);
        getInsertionPoint().setRootIdentifier(identifier);
        peek().put("identifier", identifier);
        if (infix != null)
            peek().put("infix", infix);
    }
  
    public void beginConstraintVariables(int nbrVariables) throws BuilderException {
        beginList(nbrVariables, "variables");
    }

    public void addConstraintVariable(
        String identifier, 
        int variableTypeIndex
    ) throws BuilderException {
        try {
            final int VARIABLE_SIZE = 2;
            final Map<String, Object> variable = new HashMap<String, Object>(VARIABLE_SIZE);
            variable.put("identifier", identifier);
            variable.put("variableType", getResult().getVariableType(variableTypeIndex));
            peek().add(variable);
            
        } catch (ClassCastException cce) {
            throw new BuilderException(cce);
        } catch (IndexOutOfBoundsException iobe) {
            throw new BuilderException(iobe);
        } catch (NullPointerException npe) {
            throw new BuilderException(npe);
        }
    }

    /** 
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.IBasicDataModelBuilder#endConstraintVariables()
     */
    public void endConstraintVariables() throws BuilderException {
        pop();
    }
    
    public void beginLookupCategories(int nbrLookupCategories) throws BuilderException {
        beginList(nbrLookupCategories, "lookupCategories");
    }
    
    public void beginLookupCategory(String indexType, boolean master) throws BuilderException {
        final int LOOKUP_CATEGORY_SIZE = 5;
        final Map<String, Object> lookupCategory = new HashMap<String, Object>(LOOKUP_CATEGORY_SIZE);
        lookupCategory.put("index", getStack().peek().getList().size());
        lookupCategory.put("master", Boolean.valueOf(master));
        lookupCategory.put("indexType", indexType);
        peek().add(lookupCategory);
        push(lookupCategory);
    }
    
    public void beginLookupTypes(int nbrLookupTypes) throws BuilderException {
        beginList(nbrLookupTypes, "lookupTypes");
    }
    
    public void beginLookupType() throws BuilderException {
        final int LOOKUP_TYPE_SIZE = 3;
        final Map<String, Object> lookupType = new HashMap<String, Object>(LOOKUP_TYPE_SIZE);
        final BuilderStack stack = getStack();
        try {            
            lookupType.put("category", stack.get(stack.size() - 2).getMap());            
            lookupType.put("index", stack.peek().getList().size());
            peek().add(lookupType);
            push(lookupType);
        } catch (ArrayIndexOutOfBoundsException iobe) {
            throw new BuilderException(iobe);
        } 
    }
    
    public NoneArgumentInfosBuilder getNoneArgumentInfosBuilder() throws BuilderException {
        return getLookupInfoBuilder(IndexType.NONE);
    }
    public HashMapArgumentInfosBuilder getHashMapArgumentInfosBuilder() throws BuilderException {
        return getLookupInfoBuilder(IndexType.HASH_MAP);
    }
    
    public void endLookupType() throws BuilderException {
        pop();
    }
    
    public void endLookupTypes() throws BuilderException {
        pop();
    }
    
    public void beginVariables(int nbrVariables) throws BuilderException {
        beginList(nbrVariables, "variables");
    }
    
    public void addVariable(int variableIndex) throws BuilderException {
        peek().add(getVariable(variableIndex));
    }
    
    public void endVariables() throws BuilderException {
        pop();
    }
    
    public void endLookupCategory() throws BuilderException {
        pop();
    }
    
    public void endLookupCategories() throws BuilderException {
        pop();
    }
    
    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.AbstractDataModelBuilder#endCurrentConstraint()
     */
    public void endConstraint() throws BuilderException {
        popAndInsert();
    }
    
    private LookupInfoBuilderImpl lookupInfoBuilder;
    
    protected void setLookupInfoBuilderType(IndexType type) {
        getLookupInfoBuilder().setType(type);
    }
    protected LookupInfoBuilderImpl getLookupInfoBuilder(IndexType type) {
        setLookupInfoBuilderType(type);
        return getLookupInfoBuilder();
    }
    protected LookupInfoBuilderImpl getLookupInfoBuilder() {
        return lookupInfoBuilder;
    }
    protected void setLookupInfoBuilder(
            LookupInfoBuilderImpl lookupInfoBuilderImpl) {
        this.lookupInfoBuilder = lookupInfoBuilderImpl;
    }
    
    protected class LookupInfoBuilderImpl    
    implements HashMapArgumentInfosBuilder, NoneArgumentInfosBuilder {
        
        private IndexType type;
        
        protected IndexType getType() {
            return type;
        }
        public void setType(IndexType type) {
            this.type = type;
        }
        
        /*
         * GUARD-INFOS
         ************************/
        public void beginGuardInfos(int nbGuardInfos) throws BuilderException {
            beginList(nbGuardInfos, "guardinfos");
        }
        
        public void beginGuardInfo() throws BuilderException {
            beginMap(getType().getInfoSize());
        }
        
        public void endGuardInfo() throws BuilderException {
            pop();
        }
        
        public void endGuardInfos() throws BuilderException {
            pop();
        }
        
        /*
         * HASH-MAP ARGUMENT-INFOS
         ***************************/
        public void buildOtherType(String otherType) throws BuilderException {
            peek().put("otherType", otherType);
        }
        
        public IInsertionPoint getEqInsertionPoint() throws BuilderException {
            return getCurrentInsertionPoint("eq");
        }
    }
    
    /* hulpmethodes */    
    protected Map getVariable(int variableIndex) throws BuilderException {
        return (Map)((List)getRoot().getMap().get("variables")).get(variableIndex);
    }
}