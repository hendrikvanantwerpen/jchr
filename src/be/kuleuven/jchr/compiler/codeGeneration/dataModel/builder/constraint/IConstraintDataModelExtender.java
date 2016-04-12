package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender.IDataModelExtender;

public interface IConstraintDataModelExtender extends IDataModelExtender,
        IConstraintDataModelBuilder<DataModel> {
    // no new methods
}
