package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.BinaryGuardedLookupType.BinaryGuardInfo;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented.ArgumentedDataModelExtenderFacade;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.lookupType.HashMapArgumentInfosBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director.IDataModelExtenderDirector;
import be.kuleuven.jchr.util.builder.BuilderException;


public class ConstraintDataModelExtenderDirector<B extends ConstraintDataModelExtender> 
    extends ConstraintDataModelDirector<B> 
    implements IDataModelExtenderDirector<B> {

    public ConstraintDataModelExtenderDirector(ICHRIntermediateForm intermediateForm, B builder, UserDefinedConstraint current, ArgumentedDataModelExtenderFacade extenderFacade) {
        super(intermediateForm, builder, current, extenderFacade);
    }

    public ConstraintDataModelExtenderDirector(ICHRIntermediateForm intermediateForm, B builder, UserDefinedConstraint current) {
        super(intermediateForm, builder, current);
    }

    @Override
    protected void insertEqualityConjunct(BinaryGuardInfo guardInfo, HashMapArgumentInfosBuilder builder) throws BuilderException {
        getConjunctDataModelExtenderFacade().insertConjunct(
            guardInfo.getGuard(),
            getBuilder().getResult(),
            builder.getEqInsertionPoint()
        );
    }
}
