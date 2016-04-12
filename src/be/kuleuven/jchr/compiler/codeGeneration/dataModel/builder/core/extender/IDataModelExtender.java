package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IDataModelBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;

public interface IDataModelExtender extends IDataModelBuilder<DataModel> {

    public IInsertionPoint getInsertionPoint();
    
}
