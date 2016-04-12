package be.kuleuven.jchr.compiler.analysis;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateForm;
import be.kuleuven.jchr.compiler.parser.Options;
import be.kuleuven.jchr.compiler.analysis.masterlookups.MasterLookupAnalysor;
import be.kuleuven.jchr.compiler.analysis.scheduling.Scheduler;

public final class Analysor {

    private Analysor() { /* non-instantiatable FACADE */ }
    
    public static void analyse(CHRIntermediateForm cif, Options options) 
    throws AnalysisException {
        Scheduler.schedule(cif, options);
        new MasterLookupAnalysor(cif).doAnalysis();
    }
}
