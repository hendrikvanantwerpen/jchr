package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.util.builder.BuilderException;


public class OccurrenceDataModelDirector<B extends IArgumentedDataModelBuilder<?>>
extends ArgumentedDataModelDirector<B, Occurrence> {
    
    public OccurrenceDataModelDirector(B builder, Occurrence occurrence) {
        super(builder, occurrence);
    }
    
    @Override
    protected void construct2() throws BuilderException {
        final Occurrence occurrence = getArgumented();
        getBuilder().beginOccurrence(
            occurrence.getIdentifier(),
            occurrence.getOccurrenceNbr(),
            occurrence.isRemoved(),
            occurrence.isPassive()
        );
        constructArguments(occurrence);
        getBuilder().endArgumented();
    }
}