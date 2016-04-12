package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.BasicDataModelBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic.BasicDataModelDirector;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.CurrentDataModelExtenderFacade;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.tuple.TupleDataModelBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.tuple.TupleDataModelDirector;
import be.kuleuven.jchr.util.builder.BuilderException;


public abstract class DataModelBuilderFacade {
    private DataModelBuilderFacade() {/* non-instantiatable facade */}

    public static DataModel buildDataModel(ICHRIntermediateForm intermediateForm) throws BuilderException {
        BasicDataModelBuilder builder = new BasicDataModelBuilder();
        BasicDataModelDirector director = new BasicDataModelDirector(builder, intermediateForm);
        director.construct();
        return builder.getResult();
    }
    
    public static DataModel setCurrentConstraint(UserDefinedConstraint current, DataModel dataModel) throws BuilderException {
        return CurrentDataModelExtenderFacade.getDefaultInstance().buildCurrent(current, dataModel);
    }
    
    public static DataModel buildTupleDataModel(int arity, String pkg) throws BuilderException {
        TupleDataModelBuilder builder = new TupleDataModelBuilder();
        TupleDataModelDirector<TupleDataModelBuilder> director = new TupleDataModelDirector<TupleDataModelBuilder>(builder, arity, pkg);
        director.construct();
        return builder.getResult();
    }
}