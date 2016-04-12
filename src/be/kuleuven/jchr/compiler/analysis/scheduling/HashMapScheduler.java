package be.kuleuven.jchr.compiler.analysis.scheduling;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.analysis.AnalysisException;
import be.kuleuven.jchr.util.builder.BuilderException;
import be.kuleuven.jchr.util.builder.IDirector;


public class HashMapScheduler extends AbstractScheduler implements IDirector<ILookupBuilder> {
    private ILookupBuilder builder;
    
    private boolean buildingLookup;
    
    public HashMapScheduler(CHRIntermediateForm intermediateForm) {
        super(intermediateForm);
        setBuilder(new HashMapLookupBuilder());
    }
    
    public void construct() {
        // Per uitzondering is het hier niet de bedoeling om deze methode
        // te gebruiken. De interface-implementatie is vooral als indicatie
        // bedoeld... (cf. constructLookups)
        throw new UnsupportedOperationException("Use \"doAnalysis\" instead!");
    }
    
    /**
     * @inheritDoc
     */
    @Override
    protected void analysePartners(Rule rule, int activeIndex) throws AnalysisException {
        setBuildingLookup(true);
        super.analysePartners(rule, activeIndex);
    }
    
    /**
     * @inheritDoc
     */
    @Override
    protected void analysePartner(Occurrence occurrence) throws AnalysisException {
        try {
            // Initialiase the builder that will be constructing the lookup.
            getBuilder().init();
        
            // It is important the lookup is scheduled before the guards
            // (note that at this point this lookup is not yet actually built...)
            addToSchedule(getBuilder().getResult());
            
            // In this version guards who do not end up in a lookup are scheduled
            // sequentially, in order of activation.
            // It might be possible to further analyse this order if more then one 
            // guard is present...
            
            getBuilder().setCurrentPartner(occurrence);
            tellAllKnown(occurrence);
            // After this point everything is done through observers, 
            // i.e. look at de update-methode! 
            
            getBuilder().finish();
        
            // wat hiermee gedaan eigenlijk? Voorlopig na de andere
            // guards, ook dit zou eventueel beter kunnen. Merk op dat
            // dit niet uitmaakt voorlopig, aangezien dit altijd leeg
            // zal zijn...
            addToSchedule(getBuilder().getRemainingGuards());
            
            // als ooit anders: verzamel guards, dan volgorde, dan pas addToSchedule!
            
        } catch (BuilderException be) {
            throw new AnalysisException(be.getCause());
        }
    }
    
    @Override
    protected void schedule(IGuardConjunct guard) {
        try {
            if (isBuildingLookup() && getBuilder().canAddGuard(guard))
                getBuilder().addGuard(guard);
            else
                addToSchedule(guard);
            
        } catch (BuilderException e) {
            System.err.println("Unable to schedule guard in lookup:");
            System.err.println(e.getLocalizedMessage());
            addToSchedule(guard);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void analyseActiveOccurrence(Occurrence activeOccurrence) {
        setBuildingLookup(false);
        super.analyseActiveOccurrence(activeOccurrence);
    }

    public ILookupBuilder getBuilder() {
        return builder;
    }
    protected void setBuilder(ILookupBuilder lookupBuilder) {
        this.builder = lookupBuilder;
    }

    protected boolean isBuildingLookup() {
        return buildingLookup;
    }
    protected void setBuildingLookup(boolean buildingLookup) {
        this.buildingLookup = buildingLookup;
    }
}