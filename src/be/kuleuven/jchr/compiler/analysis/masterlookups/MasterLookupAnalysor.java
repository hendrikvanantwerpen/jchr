package be.kuleuven.jchr.compiler.analysis.masterlookups;

import java.util.Arrays;
import java.util.Comparator;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.analysis.AnalysisException;
import be.kuleuven.jchr.compiler.analysis.VoidAnalysor;


public class MasterLookupAnalysor extends VoidAnalysor {

    public MasterLookupAnalysor(CHRIntermediateForm cif) {
        super(cif);
    }
    
    @Override
    protected void doAnalysis2() throws AnalysisException {
        for (UserDefinedConstraint constraint : getUserDefinedConstraints())
            analyse(constraint);
    }
    
    protected void analyse(UserDefinedConstraint constraint) {
        // If it has a master category, this is the default category
        // (implemented with a single linked list): you can't do
        // any better then that!
        if (constraint.hasMasterLookupCategory()) return;

        // Otherwise we look for the best lookup category as follows:
        ILookupCategory[] array = constraint.getLookupCategories().toArray();
        Arrays.sort(array, LookupCategoryComparator.getInstance());
        array[0].setMasterCategory();
    }
    
    private static class LookupCategoryComparator implements Comparator<ILookupCategory> {
        private LookupCategoryComparator() {/* NOP */}
        
        private static LookupCategoryComparator instance;
        public static LookupCategoryComparator getInstance() {
            if (instance == null)
                instance = new LookupCategoryComparator();
            return instance;
        }
        
        public int compare(ILookupCategory one, ILookupCategory other) {
            // For now: we don't do no reasoning here at all...
            return 0;
        }
    }
}
