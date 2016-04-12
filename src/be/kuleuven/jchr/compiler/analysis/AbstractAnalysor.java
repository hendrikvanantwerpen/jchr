package be.kuleuven.jchr.compiler.analysis;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateFormDecorator;

public abstract class AbstractAnalysor<Returns> extends CHRIntermediateFormDecorator {
    public AbstractAnalysor(CHRIntermediateForm intermediateForm) {
        setIntermediateForm(intermediateForm);
    }
        
    public abstract Returns doAnalysis() throws AnalysisException;
}
