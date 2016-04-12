package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender.InsertionPoint;
import be.kuleuven.jchr.compiler.codeGeneration.util.builder.BuilderStack.Node;
import be.kuleuven.jchr.util.builder.BuilderException;


public abstract class CurrentDataModelExtenderFacade {
    
    protected CurrentDataModelExtenderFacade() {/* FACTORY-METHODS */}
    
    public abstract DataModel buildCurrent(UserDefinedConstraint current, DataModel dataModel) throws BuilderException;
    
    private static CurrentDataModelExtenderFacade defaultInstance;
    public static CurrentDataModelExtenderFacade getDefaultInstance() {
        if (defaultInstance == null)
            defaultInstance = new CurrentDataModelExtenderFacade() {
            
            @Override
            public DataModel buildCurrent(UserDefinedConstraint current, DataModel dataModel) throws BuilderException {
                CurrentDataModelExtender builder = 
                    new CurrentDataModelExtender(dataModel, new InsertionPoint(new Node(dataModel.getRoot()), "current"));
                CurrentDataModelExtenderDirector<CurrentDataModelExtender> director =
                    new CurrentDataModelExtenderDirector<CurrentDataModelExtender>(builder, current);
                director.construct();

                return builder.getResult();
            }
        };

        return defaultInstance;
    }    
}