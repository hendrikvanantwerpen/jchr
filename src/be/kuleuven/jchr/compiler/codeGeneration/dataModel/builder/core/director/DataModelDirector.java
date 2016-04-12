package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IDataModelBuilder;
import be.kuleuven.jchr.util.builder.Director;


public abstract class DataModelDirector<B extends IDataModelBuilder<?>>
    extends Director<B>
    implements IDataModelDirector<B> {

    public DataModelDirector(B builder) {
        super(builder);
    }
}
