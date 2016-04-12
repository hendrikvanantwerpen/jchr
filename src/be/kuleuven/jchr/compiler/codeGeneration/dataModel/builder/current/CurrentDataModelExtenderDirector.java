package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.Lookup;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented.ArgumentedDataModelExtenderFacade;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director.IDataModelExtenderDirector;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.LookupCommandoBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;


public class CurrentDataModelExtenderDirector<B extends ICurrentDataModelExtender> 
extends CurrentDataModelDirector<B>
implements IDataModelExtenderDirector<B> {
    

    /**
     * @inheritDoc
     */
    public CurrentDataModelExtenderDirector(B builder, UserDefinedConstraint current, ArgumentedDataModelExtenderFacade conjunctDataModelExtenderFacade) {
        super(builder, current, conjunctDataModelExtenderFacade);
    }

    /**
     * @inheritDoc
     */
    public CurrentDataModelExtenderDirector(B builder, UserDefinedConstraint current) {
        super(builder, current);
    }

    /**
     * @inheritDoc
     */
    public CurrentDataModelExtenderDirector(B builder) {
        super(builder);
    }

    @Override
    protected void insertLookupArguments(Lookup lookup, LookupCommandoBuilder builder) throws BuilderException {
        getConjunctDataModelExtenderFacade().insertLookupArguments(lookup,
            getBuilder().getResult(),
            builder.getLookupArgumentsInsertionPoint()
        );
    }
}
