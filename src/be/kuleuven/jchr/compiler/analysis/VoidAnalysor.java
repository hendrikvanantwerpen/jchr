package be.kuleuven.jchr.compiler.analysis;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateForm;

/**
 * Een <code>VoidAnalysor</code> is een <code>Analysor</code> die
 * geen resultaat teruggeeft. Typisch zal dit type dan ook wijzigingen
 * (verbeteringen...) aanbrengen aan de <code>CHRIntermediateForm</code>. 
 * 
 * @author Peter Van Weert
 */
public abstract class VoidAnalysor extends AbstractAnalysor<Void> {
    public VoidAnalysor(CHRIntermediateForm intermediateForm) {
        super(intermediateForm);
    }

    @Override
    public final Void doAnalysis() throws AnalysisException {
        doAnalysis2();
        return null;
    }
    
    protected abstract void doAnalysis2() throws AnalysisException;
}
