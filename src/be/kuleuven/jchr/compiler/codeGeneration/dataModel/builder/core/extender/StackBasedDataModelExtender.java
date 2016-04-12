package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender;

import java.util.HashMap;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.StackBasedDataModelBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;



public abstract class StackBasedDataModelExtender 
extends StackBasedDataModelBuilder 
implements IDataModelExtender {
    
    private IInsertionPoint insertionPoint;
    
    public StackBasedDataModelExtender(DataModel dataModel, IInsertionPoint insertionPoint) {
        setResult(dataModel);
        setInsertionPoint(insertionPoint);
    }
    
    @Override
    protected void createRoot(int rootSize) throws BuilderException {
        push(new HashMap<String, Object>(rootSize));
    }
    
    public IInsertionPoint getInsertionPoint() {
        return insertionPoint;
    }
    protected void popAndInsert() throws BuilderException {
        try {
            getInsertionPoint().insert(pop().getValue());
        } catch (IllegalStateException ise) {
            throw new BuilderException(ise);
        }
    }
    protected void setInsertionPoint(IInsertionPoint insertionPoint) {
        this.insertionPoint = insertionPoint;
    }
}