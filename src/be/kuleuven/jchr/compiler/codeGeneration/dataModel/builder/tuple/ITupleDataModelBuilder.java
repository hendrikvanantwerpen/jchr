package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.tuple;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IDataModelBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;


public interface ITupleDataModelBuilder<Result> extends IDataModelBuilder<Result> {

    public void buildArity(int arity,String pkg) throws BuilderException;
}
