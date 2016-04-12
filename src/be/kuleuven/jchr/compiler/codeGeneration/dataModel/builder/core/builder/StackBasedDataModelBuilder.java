package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender.InsertionPoint;
import be.kuleuven.jchr.compiler.codeGeneration.util.builder.StackBasedBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;



public abstract class StackBasedDataModelBuilder
    extends StackBasedBuilder<DataModel>
    implements IDataModelBuilder<DataModel> {
    
    private DataModel result;
    
    protected void createRoot(int rootSize) throws BuilderException {
        final Map<String, Object> root = new HashMap<String, Object>(rootSize);
        setResult(new DataModel(root));
        push(root);
    }

    public DataModel getResult() throws BuilderException {
        return result;
    }
    protected void setResult(DataModel result) {
        this.result = result;
    }
    
    protected IInsertionPoint getCurrentInsertionPoint() throws BuilderException {
        return new InsertionPoint(peek());
    }
    protected IInsertionPoint getCurrentInsertionPoint(String identifier) throws BuilderException {
        return new InsertionPoint(peek(), identifier);
    }
    
}