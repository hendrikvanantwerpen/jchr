package be.kuleuven.jchr.compiler.analysis.scheduling;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateForm;
import be.kuleuven.jchr.compiler.parser.Options;
import be.kuleuven.jchr.compiler.analysis.AnalysisException;

public final class Scheduler {
    
    private Scheduler() { /* non-instantiatable FACADE */ }
    
    public static void schedule(CHRIntermediateForm cif, Options options) 
    throws AnalysisException {
        if (options.hashMap.value)
            new HashMapScheduler(cif).doAnalysis();
        else
            new EarlyGuardScheduler(cif).doAnalysis();
    }

}
