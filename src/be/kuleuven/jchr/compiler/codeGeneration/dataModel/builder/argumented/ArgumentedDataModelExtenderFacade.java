package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IBasicArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.Lookup;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.util.builder.BuilderException;


public abstract class ArgumentedDataModelExtenderFacade {

    protected ArgumentedDataModelExtenderFacade() {/* FACADE */}
    
    public abstract DataModel insertOccurrence(Occurrence occurrence, DataModel dataModel, IInsertionPoint insertionPoint) throws BuilderException;
    
    public abstract DataModel insertConjunct(IBasicArgumented conjunct, DataModel dataModel, IInsertionPoint insertionPoint) throws BuilderException;
    
    public abstract DataModel insertLookupArguments(Lookup lookup, DataModel dataModel, IInsertionPoint insertionPoint) throws BuilderException;
    
    private static ArgumentedDataModelExtenderFacade defaultInstance;
    
    public static ArgumentedDataModelExtenderFacade getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new ArgumentedDataModelExtenderFacade() {
                @Override
                public DataModel insertOccurrence(Occurrence occurrence, DataModel dataModel, IInsertionPoint insertionPoint) throws BuilderException {
                    ArgumentedDataModelExtender builder = new ArgumentedDataModelExtender(dataModel, insertionPoint);
                    ArgumentedDataModelDirector<ArgumentedDataModelExtender, Occurrence> director 
                        = new OccurrenceDataModelDirector<ArgumentedDataModelExtender>(builder, occurrence);
                    director.construct();
                    return builder.getResult();
                }
                
                @Override
                public DataModel insertConjunct(IBasicArgumented conjunct, DataModel dataModel, IInsertionPoint insertionPoint) throws BuilderException {
                    ArgumentedDataModelExtender builder = new ArgumentedDataModelExtender(dataModel, insertionPoint);
                    ArgumentedDataModelDirector<ArgumentedDataModelExtender, IBasicArgumented> director 
                        = new ConjunctDataModelDirector<ArgumentedDataModelExtender>(builder, conjunct);
                    director.construct();
                    return builder.getResult();
                }
                
                @Override
                public DataModel insertLookupArguments(Lookup lookup, DataModel dataModel, IInsertionPoint insertionPoint) throws BuilderException {
                    ArgumentedDataModelExtender builder = new ArgumentedDataModelExtender(dataModel, insertionPoint);
                    ArgumentedDataModelDirector<ArgumentedDataModelExtender, Lookup> director 
                        = new LookupDataModelDirector<ArgumentedDataModelExtender>(builder, lookup);
                    director.construct();
                    return builder.getResult();
                }
            };            
        }

        return defaultInstance;
    }
}
