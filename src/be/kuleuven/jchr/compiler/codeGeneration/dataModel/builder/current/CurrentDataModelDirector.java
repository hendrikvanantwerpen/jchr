package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IGuardConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.Lookup;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule.IScheduleComponent;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented.ArgumentedDataModelExtenderFacade;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director.DataModelDirector;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CheckFiredCommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CheckGuardCommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CommitCommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.LookupCommandoBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;


/**
 * @author Peter Van Weert
 */
public abstract class CurrentDataModelDirector<B extends ICurrentDataModelBuilder<?>>
        extends DataModelDirector<B> {

    private ArgumentedDataModelExtenderFacade conjunctDataModelExtenderFacade;
    
    public CurrentDataModelDirector(B builder, UserDefinedConstraint current, ArgumentedDataModelExtenderFacade conjunctDataModelExtenderFacade) {
        super(builder);
        setCurrent(current);
        setConjunctDataModelExtenderFacade(conjunctDataModelExtenderFacade);
    }    
    
    public CurrentDataModelDirector(B builder, UserDefinedConstraint current) {
        this(builder, current, ArgumentedDataModelExtenderFacade.getDefaultInstance());
    }
    
    public CurrentDataModelDirector(B builder) {
        this(builder, null);
    }

    private UserDefinedConstraint current;

    @Override
    protected void construct2() throws BuilderException {
        getBuilder().beginCurrentConstraint(getCurrent().getIdentifier());
        constructOccurrences();
        getBuilder().endCurrentConstraint();
    }
    
    protected void constructOccurrences() throws BuilderException {
    getBuilder().beginOccurrences(getCurrent().getNbActiveOccurrences());
        for (Occurrence occurrence : getCurrent().getOccurrences())
            if (!occurrence.isPassive()) 
                constructOccurrence(occurrence);
        getBuilder().endOccurrences();
    }

    protected void constructOccurrence(final Occurrence occurrence)
            throws BuilderException {
        getBuilder().beginOccurrence(
            occurrence.getRuleNbr(),
            occurrence.getRuleOccurrenceNbr()
        );
        getBuilder().beginOccurrenceCommandos();
        constructOccurrenceCommandos(occurrence);
        getBuilder().endOccurrenceCommandos();
        getBuilder().endOccurrence();
    }

    protected void constructOccurrenceCommandos(final Occurrence occurrence)
            throws BuilderException {
        final Rule rule = occurrence.getRule();
        final int occurrenceNbr = occurrence.getRuleOccurrenceNbr();
        
        constructSchedule(occurrence);
        if (rule.getType() == RuleType.PROPAGATION)
            constructCheckFired(rule, occurrenceNbr);
        constructCommit(rule, occurrenceNbr);
    }

    protected void constructSchedule(final Occurrence occurrence)
            throws BuilderException {
        
        for (IScheduleComponent scheduleComponent : occurrence.getSchedule()) {
            switch (scheduleComponent.getScheduleComponentType()) {
                case GUARD:
                    constructGuard((IGuardConjunct) scheduleComponent, occurrence.getRule());
                break;
    
                case LOOKUP:
                    constructLookup((Lookup) scheduleComponent);
                break;
    
                default:
                    throw new BuilderException("Unsupported ScheduleComponentType");
            }
        }
    }

    protected void constructLookup(Lookup lookup) throws BuilderException {
        final LookupCommandoBuilder builder = getBuilder().getLookupCommandoBuilder();
        final Occurrence occurrence = lookup.getOccurrence();
        final ILookupType lookupType = lookup.getLookupType();
        final ILookupCategory lookupCategory = lookupType.getCategory();
        builder.beginCommando();
        builder.beginLookup(occurrence.getRuleNbr(), 
            occurrence.getRuleOccurrenceNbr(), 
            occurrence.getIndexOf(lookupCategory),
            lookupCategory.getIndexOf(lookupType)
        );
        insertLookupArguments(lookup, builder);
        builder.endLookup();
        builder.endCommando();
    }
    
    protected abstract void insertLookupArguments(Lookup lookup, LookupCommandoBuilder builder) throws BuilderException;

    protected void constructGuard(IGuardConjunct constraint, Rule rule)
            throws BuilderException {
        final CheckGuardCommandoBuilder builder = getBuilder().getCheckGuardCommandoBuilder();
        builder.beginCommando();
        builder.buildCheck(
            rule.getNbr(), 
            rule.getGuardConjuncts().indexOf(constraint)
        );
        builder.endCommando();
    }

    protected void constructCheckFired(final Rule rule, final int occurrenceNbr) throws BuilderException {
        final CheckFiredCommandoBuilder firedCommandoBuilder = getBuilder().getCheckFiredCommandoBuilder();
        firedCommandoBuilder.beginCommando();
        firedCommandoBuilder.buildFiredCheck(rule.getNbr(), occurrenceNbr);
        firedCommandoBuilder.endCommando();
    }

    protected void constructCommit(final Rule rule, final int occurrenceNbr)
            throws BuilderException {
        CommitCommandoBuilder commandoBuilder = getBuilder().getCommitCommandoBuilder();
        commandoBuilder.beginCommando();
        commandoBuilder.buildCommit(rule.getNbr(), occurrenceNbr);
        commandoBuilder.endCommando();
    }

    public UserDefinedConstraint getCurrent() {
        return current;
    }

    public void setCurrent(UserDefinedConstraint current) {
        this.current = current;
    }
    
    protected ArgumentedDataModelExtenderFacade getConjunctDataModelExtenderFacade() {
        return conjunctDataModelExtenderFacade;
    }
    protected void setConjunctDataModelExtenderFacade(
            ArgumentedDataModelExtenderFacade conjunctDataModelExtenderFacade) {
        this.conjunctDataModelExtenderFacade = conjunctDataModelExtenderFacade;
    }
}