package be.kuleuven.jchr.compiler.analysis.scheduling;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.EmptyArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.Lookup;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.DefaultLookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;
import be.kuleuven.jchr.compiler.analysis.AnalysisException;

public class EarlyGuardScheduler extends AbstractScheduler {
    public EarlyGuardScheduler(CHRIntermediateForm intermediateForm) {
        super(intermediateForm);
    }
    
    @Override
    protected void analysePartner(final Occurrence occurrence) throws AnalysisException {
        addToSchedule(new Lookup() {
            @Override
            public Occurrence getOccurrence() {
                return occurrence;
            }
            
            @Override
            public ILookupType getLookupType() {
                return DefaultLookupType.getInstance();
            }
            
            public IArguments getArguments() {
                return EmptyArguments.getInstance();
            }
        });
        
        // in deze versie worden de guards die niet in een lookup terechtkomen
        // gewoon sequentieel gepland, in de volgorde dat ze worden geactiveerd.
        // Het zou eventueel mogelijk zijn om ook hierin nog een geschikte 
        // volgorde te bepalen...
        
        tellAllKnown(occurrence);
        // hierna verloopt alles via het observerpatroon: 
        // kijk in de update-methodes dus! 
        
        // als ooit anders: verzamel guards, dan volgorde, dan pas addToSchedule!
    }
    
    @Override
    protected void schedule(IGuardConjunct guard) {
        addToSchedule(guard);
    }
}
