package be.kuleuven.jchr.compiler.analysis.scheduling;

import java.util.Collection;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.Lookup;
import be.kuleuven.jchr.util.builder.BuilderException;
import be.kuleuven.jchr.util.builder.IBuilder;



public interface ILookupBuilder extends IBuilder<Lookup> {
    /**
     * Sets the current <i>partner occurrence</i>. This is the constraint
     * who's variables are told they're know, possibly triggering an
     * activision of a guard.
     * 
     * @param occurrence
     *  The current <i>partner occurrence</i>
     * @throws BuilderException
     *  A generic exception indicating something went wrong building
     *  the lookup. 
     */
    public void setCurrentPartner(Occurrence occurrence) throws BuilderException;
    
    /**
     * Adds the given guard to the lookup that's being built.
     * 
     * @pre canAddGuard(guard)
     * 
     * @param guard
     *  The guard that's to be added to the current lookup.
     * @throws BuilderException
     *  A generic exception indicating something went wrong building
     *  the lookup. 
     */
    public void addGuard(IGuardConjunct guard) throws BuilderException;
    
    /**
     * This method checks whether the given guard conjunct can be added
     * to the lookup that's being built by this builder.
     * 
     * @pre All variables occurring in this conjunct are <i>known</i>,
     *  and this is the first time they're known. In other words: one of
     *  the variables in the current partner occurrence has caused
     *  this guard conjunct to be triggered.
     * 
     * @param guard
     *  The guard conjunct that has to be tested whether it is
     *  suited for addition to the lookup that's being built.
     * @return <code>True</code> iff <code>guard</code> can be added to the
     *  lookup that's being built.  
     * @throws BuilderException
     *  A generic exception indicating something went wrong building
     *  the lookup. 
     */
    public boolean canAddGuard(IGuardConjunct guard) throws BuilderException;
    
    public Collection<IGuardConjunct> getRemainingGuards() throws BuilderException;
}
