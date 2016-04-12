package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.Lookup;
import be.kuleuven.jchr.util.builder.BuilderException;


public class LookupDataModelDirector<B extends IArgumentedDataModelBuilder<?>>
extends ArgumentedDataModelDirector<B, Lookup> {

    public LookupDataModelDirector(B builder, Lookup lookup) {
        super(builder, lookup);
    }

    @Override
    protected void construct2() throws BuilderException {
        getBuilder().beginLookup();
        constructArguments();
        getBuilder().endArgumented();
    }
}
