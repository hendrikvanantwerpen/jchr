package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.util.builder.BuilderException;


public abstract class ConstraintDataModelExtenderFacade {

    private static ConstraintDataModelExtenderFacade defaultInstance;

    protected ConstraintDataModelExtenderFacade() {/* FACTORY */}

    public static ConstraintDataModelExtenderFacade getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new ConstraintDataModelExtenderFacade() {
                @Override
                public DataModel insertConstraint(
                        ICHRIntermediateForm intermediateForm,
                        UserDefinedConstraint constraint, DataModel dataModel,
                        IInsertionPoint node) throws BuilderException {
                    ConstraintDataModelExtender builder = new ConstraintDataModelExtender(
                            dataModel, node);
                    ConstraintDataModelDirector<ConstraintDataModelExtender> director 
                        = new ConstraintDataModelExtenderDirector<ConstraintDataModelExtender>(
                            intermediateForm, builder, constraint
                        );
                    director.construct();
                    return builder.getResult();
                }
            };
        }
        return defaultInstance;
    }

    public abstract DataModel insertConstraint(
            ICHRIntermediateForm intermediateForm, UserDefinedConstraint constraint,
            DataModel dataModel, IInsertionPoint node) throws BuilderException;
}