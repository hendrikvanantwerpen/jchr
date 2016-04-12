package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender.StackBasedDataModelExtender;
import be.kuleuven.jchr.util.builder.BuilderException;



public class ArgumentedDataModelExtender
extends StackBasedDataModelExtender
implements IArgumentedDataModelBuilder<DataModel> {
    
    public ArgumentedDataModelExtender(DataModel dataModel, IInsertionPoint insertionPoint) {
        super(dataModel, insertionPoint);
    }

    public void abort() throws BuilderException {
        // NOP
    }
    
    public void finish() throws BuilderException {
        // NOP
    }
    
    private boolean buildingArgumentsOnly;
    
    public void beginLookup() throws BuilderException {        
        final int ROOT_SIZE = 1;
        createRoot(ROOT_SIZE);
        setBuildingArgumentsOnly(true);
    }
    
    public void beginOccurrence(String identifier, int nbr, boolean isRemoved, boolean isPassive) throws BuilderException {
        final int ROOT_SIZE = 5;        
        createRoot(ROOT_SIZE);
        setBuildingArgumentsOnly(false);
        
        peek().put("identifier", identifier);
        peek().put("nbr", Integer.valueOf(nbr));
        peek().put("isRemoved", Boolean.valueOf(isRemoved));
        peek().put("isPassive", Boolean.valueOf(isPassive));
    }
    
    protected void beginConjunct(String type, int infoSize) throws BuilderException {
        final int ROOT_SIZE = infoSize + 1;
        createRoot(ROOT_SIZE);
        setBuildingArgumentsOnly(false);
        peek().put("type", type);        
    }
    
    public void beginUserDefinedConjunct(String identifier) throws BuilderException {
        final int INFO_SIZE = 1;
        beginConjunct("udConstraint", INFO_SIZE);
        peek().put("identifier", identifier);        
    }
    
    public void beginInfixConjunct(String identifier) throws BuilderException {
        final int INFO_SIZE = 1;
        beginConjunct("infixConstraint", INFO_SIZE);
        peek().put("identifier", identifier);
    }
    
    public void beginDeclaration(String type) throws BuilderException {
        final int INFO_SIZE = 1;
        beginConjunct("declaration", INFO_SIZE);
        peek().put("declaringType", type);
    }
    
    public void beginMethodInvocation(String methodName) throws BuilderException {
        final int INFO_SIZE = 1;
        beginConjunct("methodInvocation", INFO_SIZE);
        peek().put("methodName", methodName);
    }
    
    public void beginArguments(int nbrVariables) throws BuilderException {
        beginList(nbrVariables, "arguments");
    }
    
    protected void buildConstantArgument(String type, Object value) throws BuilderException {
        final int CONST_INFO_SIZE = 1;
        beginArgumentInfo(type, CONST_INFO_SIZE);
        pop().put("value", value);
    }
    
    protected void buildArgumentType(String type) throws BuilderException {
        peek().put("type", type);
    }
    
    protected void beginArgumentInfo(String type, int infoSize) throws BuilderException {
        buildArgumentType(type);
        beginMap(infoSize, "argumentInfo");        
    }
    
    public void beginArgument() throws BuilderException {
        final int ARGUMENT_SIZE = 2;
                        
        final Map<String, Object> argument 
            = new HashMap<String, Object>(ARGUMENT_SIZE);
        peek().add(argument);
        push(argument);
    }
    
    public void buildBooleanArgument(Boolean value) throws BuilderException {        
        buildConstantArgument("boolean", value);        
    }
    
    public void buildByteArgument(Byte value) throws BuilderException {
        buildConstantArgument("byte", value);
    }
    
    public void buildCharArgument(Character value) throws BuilderException {
        buildConstantArgument("char", value);
    }
    
    public void buildDoubleArgument(Double value) throws BuilderException {
        buildConstantArgument("double", value);
    }
    
    public void buildFloatArgument(Float value) throws BuilderException {
        buildConstantArgument("float", value);
    }
    
    public void buildIntArgument(Integer value) throws BuilderException {
        buildConstantArgument("int", value);
    }
    
    public void buildLongArgument(Long value) throws BuilderException {
        buildConstantArgument("long", value);
    }
    
    public void buildNullArgument() throws BuilderException {
        buildArgumentType("null");
    }
    
    public void buildOneDummyArgument() throws BuilderException {
        buildArgumentType("one_dummy");
    }

    public void buildOtherDummyArgument() throws BuilderException {
        buildArgumentType("other_dummy");
    }

    public void buildShortArgument(Short value) throws BuilderException {
        buildConstantArgument("short", value);
    }
    
    public void buildStringArgument(String value) throws BuilderException {
        buildConstantArgument("string", value);
    }
       
    public void buildVariableArgument(String identifier, boolean anonymous) throws BuilderException {
        final int VAR_INFO_SIZE = 2;
        beginArgumentInfo("variable", VAR_INFO_SIZE);
        peek().put("identifier", identifier);
        pop().put("anonymous", Boolean.valueOf(anonymous));
    }
    
    public void buildClassNameArgument(String name) throws BuilderException {
        final int INFO_SIZE = 1;
        beginArgumentInfo("className", INFO_SIZE);
        pop().put("name", name);
    }
    
    public void buildSolverArgument(String identifier) throws BuilderException {
        final int INFO_SIZE = 1;
        beginArgumentInfo("solver", INFO_SIZE);
        pop().put("identifier", identifier);
    }

    public void beginMethodInvocationArgument(String methodName) throws BuilderException {
        final int INFO_SIZE = 2;
        beginArgumentInfo("methodInvocation", INFO_SIZE);
        peek().put("methodName", methodName);
    }
    
    public void endMethodInvocationArgument() throws BuilderException {
        pop();
    }
    
    public void beginConstructorInvocationArgument(String type) throws BuilderException {
        final int INFO_SIZE = 2;
        beginArgumentInfo("constructorInvocation", INFO_SIZE);
        peek().put("type", type);
    }
    
    public void endConstructorInvocationArgument() throws BuilderException {
        pop();
    }
    
    public void beginFieldAccessArgument(String fieldName) throws BuilderException {
        final int INFO_SIZE = 2;
        beginArgumentInfo("field", INFO_SIZE);
        peek().put("fieldName", fieldName);
    }
    
    public void endFieldAccessArgument() throws BuilderException {
        pop();
    }
    
    public void endArgument() throws BuilderException {
        pop();
    }
    
    public void endArguments() throws BuilderException {
        if (isBuildingArgumentsOnly()) 
            popAndInsert();
        else
            pop();
    }
    
    public void endArgumented() throws BuilderException {
        if (isBuildingArgumentsOnly()) 
            pop();
        else
            popAndInsert();
    }

    protected boolean isBuildingArgumentsOnly() {
        return buildingArgumentsOnly;
    }
    protected void setBuildingArgumentsOnly(boolean buildingArgumentsOnly) {
        this.buildingArgumentsOnly = buildingArgumentsOnly;
    }
}
