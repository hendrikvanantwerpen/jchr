package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.IConjunct;

public class Failure implements IConjunct {
    
    private Failure() { /* SINGLETON */ }
    private static Failure instance;
    public static Failure getInstance() {
        if (instance == null)
            instance = new Failure();
        return instance;
    }
}
