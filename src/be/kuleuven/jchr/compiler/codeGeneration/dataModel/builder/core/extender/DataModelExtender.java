package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.DataModelBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;

public abstract class DataModelExtender 
extends DataModelBuilder 
implements IDataModelExtender {
    
    private IInsertionPoint insertionPoint;
    
    public DataModelExtender(DataModel dataModel, IInsertionPoint insertionPoint) {
        setResult(dataModel);
        setInsertionPoint(insertionPoint);
    }

    public IInsertionPoint getInsertionPoint() {
        return insertionPoint;
    }
    protected void setInsertionPoint(IInsertionPoint insertionPoint) {
        this.insertionPoint = insertionPoint;
    }
}
