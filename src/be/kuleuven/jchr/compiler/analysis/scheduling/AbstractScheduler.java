package be.kuleuven.jchr.compiler.analysis.scheduling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.CHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.IScheduleComponent;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.Schedule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.compiler.analysis.AnalysisException;
import be.kuleuven.jchr.compiler.analysis.VoidAnalysor;


public abstract class AbstractScheduler extends VoidAnalysor implements Observer {
    private Map<Variable, VariableWrapper> variableMap;
    
    private Collection<GuardWrapper> guards;
    
    private Schedule schedule;
    
    public AbstractScheduler(CHRIntermediateForm intermediateForm) {
        super(intermediateForm);
        
        setVariableMap(new HashMap<Variable, VariableWrapper>());
        setGuards(new ArrayList<GuardWrapper>());
    }
    
    @Override
    public void doAnalysis2() throws AnalysisException {
        for (Rule rule : getRules()) analyse(rule);
    }
    
    /**
     * Schedules all partner constraints lookups and guard checks for
     * the occurrences in the head of the given rule.  
     * 
     * @param rule
     *  The rule whose occurrences have to be scheduled.
     *   
     * @throws AnalysisException
     *  A generic exception for something that has gone wrong during
     *  this analysis.
     */
    public void analyse(Rule rule) throws AnalysisException {
        prepareDataStructures(rule);
        
        for (int i = 0; i < rule.getNbOccurrences(); i++)
            analyse(rule, i);
    }
    
    /**
     * Schedules all partner constraints lookups and guard checks for
     * the given <i>active</i> occurrence in the head of the given rule.  
     * 
     * @param rule
     *  The rule whose <code>activeIndex</code>'th occurrence has to be scheduled.
     * @param activeIndex
     *  The index of the occurrence that has to be scheduled (called the
     *  <i>active</i> occurrence).
     *   
     * @throws AnalysisException
     *  A generic exception for something that has gone wrong during
     *  this analysis.
     */
    protected void analyse(Rule rule, int activeIndex) throws AnalysisException {
        final Occurrence activeOccurrence = rule.getOccurrenceAt(activeIndex); 
        
        // A default schedule is more then enough since no code will be generated!
        if (activeOccurrence.isPassive())   // contradictio in terminis ;-)
            return;
        
        // de datastructuren in orde brengen
        resetDataStructures();
        beginNewSchedule();
        
        // daarna alle guards die al kunnen zetten
        analyseActiveOccurrence(activeOccurrence);

        // tenslotte alle lookups maken
        analysePartners(rule, activeIndex);

        // en we hebben de gewenste schedule:
        rule.getOccurrenceAt(activeIndex).setSchedule(getSchedule());
    }
    
    /**
     * Analyses all <i>partner</i> occurrences (all occurrences other than
     * the active one at index <code>activeIndex</code>).
     * 
     * @param rule
     *  The rule whose occurrences are being analysed.
     * @param activeIndex
     *  The index of the active occurrence.
     * @throws AnalysisException
     *  A generic exception for something that has gone wrong during
     *  this analysis.
     */
    protected void analysePartners(Rule rule, int activeIndex) throws AnalysisException {
        for (int i = 0; i < rule.getNbOccurrences(); i++)
            if (i != activeIndex) analysePartner(rule.getOccurrenceAt(i));
    }
    
    /**
     * Analyses a <i>partner</i> occurrence (i.e. an occurrence other than
     * the active one, meaning a partner constraint lookup might have to
     * be scheduled. Note that also other guards might have to be scheduled
     * after this lookup!).
     * 
     * @param occurrence
     *  The i>partner</i> occurrence whose lookups have to be scheduled.
     * @throws AnalysisException
     *  A generic exception for something that has gone wrong during
     *  this analysis.
     */
    protected abstract void analysePartner(Occurrence occurrence) throws AnalysisException;
    
    /**
     * All variables occurring in the given <code>occurrence</code> are
     * to be told they are known.
     * (recall: rules are in <acronym title="Head Normal Form">HNF</acronymm>)
     * 
     * @param occurrence
     *  The occurrence whose variables have are to be told they are known.
     */
    protected void tellAllKnown(Occurrence occurrence) {
        for (Variable variable : occurrence.getVariablesInArguments())
            if (!variable.isAnonymous())
                getVariableMap().get(variable).tellKnown();
    }
    
    public void update(Observable observable, Object arg) throws ClassCastException {
        schedule(((GuardWrapper)observable).getGuard());
    }
    
    protected abstract void schedule(IGuardConjunct guard);
    
    protected void addToSchedule(IScheduleComponent scheduleComponent) {
        getSchedule().addComponent(scheduleComponent);
    }
    protected <T extends IScheduleComponent> void addToSchedule(Collection<T> scheduleComponents) {
        getSchedule().addComponents(scheduleComponents);
    }    
    
    protected void resetDataStructures() {
        resetVariableMap();
        resetGuards();
    }
    protected void prepareDataStructures(Rule rule) throws AnalysisException {
        try {
            prepareVariableMap(rule);
            prepareGuards(rule);

        } catch (IllegalStateException ise) {
            throw new AnalysisException(ise);
        }
    }
    
    
    protected void resetVariableMap() {
        for (VariableWrapper variableWrapper : getVariableMap().values())
            variableWrapper.reset();
    }
    /**
     * Prepares the variable map needed for scheduling a given <code>rule</code>.
     * This has to be done before the guard wrappers are set up, because these
     * will be observing these variable-wrappers!
     * 
     * @param rule
     *  The rule whose schedules are about to be analysed.
     * @throws IllegalStateException
     *  The given rule is not in <acronym title="Head Normal Form">HNF</acronymm>
     *  (this should never happen of course).
     */
    protected void prepareVariableMap(Rule rule) throws IllegalStateException {
        clearVariableMap();
        for (Occurrence occurrence : rule.getOccurrences())
            for (Variable variable : occurrence.getVariablesInArguments())
                if (! variable.isAnonymous())
                    if (getVariableMap().put(variable, new VariableWrapper(variable)) != null)
                        throw new IllegalStateException("Rule " + rule.getIdentifier() + " is not in HNF.");
    }

    protected Map<Variable, VariableWrapper> getVariableMap() {
        return variableMap;
    }
    protected void setVariableMap(Map<Variable, VariableWrapper> variableMap) {
        this.variableMap = variableMap;
    }
    protected void clearVariableMap() {
        for (VariableWrapper variableWrapper : getVariableMap().values())
            variableWrapper.deleteObservers();
        getVariableMap().clear();
    }
    
    /**
     * Prepares the variable map needed for scheduling a given <code>rule</code>.
     * Precondition: the current variable map is set up for the same rule!
     * Each guard wrapper will be observing the wrappers of the variables in
     * it...
     * 
     * @param rule
     *  The rule whose schedules are about to be analysed.
     */
    protected void prepareGuards(Rule rule) {
        GuardWrapper guard;
        for (IGuardConjunct conjunct : rule.getGuardConjuncts()) {
            guard = new GuardWrapper(conjunct, getVariableMap());
            guard.addObserver(this);
            addGuard(guard);
        }
    }
    protected void resetGuards() {
        for (GuardWrapper guardWrapper : getGuards())
            guardWrapper.reset();
    }
    
    /**
     * Schedules all already known guards when the given occurrence is
     * the active occurrence. This includes both guards without 
     * variables in their arguments and guards only containing variables
     * present in the <code>activeOccurrence</code>. 
     * 
     * @param activeOccurrence
     *  The active occurrence.
     */
    protected void analyseActiveOccurrence(Occurrence activeOccurrence) {
        // First all guards without variable in their arguments
        for (GuardWrapper guardWrapper : getGuards())
            guardWrapper.checkAllKnown();
        
        // Followed by guards with only variables occurring in the active
        // occurrence.
        tellAllKnown(activeOccurrence);
    }

    protected Collection<GuardWrapper> getGuards() {
        return guards;
    }
    protected void setGuards(Collection<GuardWrapper> guards) {
        this.guards = guards;
    }
    protected void clearGuards() {
        for (GuardWrapper guardWrapper : getGuards())
            guardWrapper.deleteObservers();
        getGuards().clear();
    }
    protected void addGuard(GuardWrapper guard) {
        getGuards().add(guard);
    }


    protected void beginNewSchedule() {
        setSchedule(new Schedule());
    }
    protected Schedule getSchedule() {
        return schedule;
    }
    protected void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

}
