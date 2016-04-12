package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.tuple;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.DataModelBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;


public class TupleDataModelBuilder extends DataModelBuilder 
implements ITupleDataModelBuilder<DataModel> {

    public void buildArity(int arity, String pkg ) throws BuilderException {
        getResult().getRoot().put("arity", Integer.valueOf(arity));
        getResult().getRoot().put( "chrpackage", pkg );
    }

    @Override
    public void init() throws BuilderException {
        createRoot(1);
    }

    @Override
    public void abort() throws BuilderException {
        // NOP
    }

    @Override
    public void finish() throws BuilderException {
        // NOP
    }
}
