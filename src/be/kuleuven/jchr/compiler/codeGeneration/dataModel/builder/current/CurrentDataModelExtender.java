package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender.StackBasedDataModelExtender;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CheckFiredCommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CheckGuardCommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CommandoType;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CommitCommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.LookupCommandoBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;



/**
 * @author Peter Van Weert
 */
public class CurrentDataModelExtender 
extends StackBasedDataModelExtender
implements ICurrentDataModelExtender {

    public CurrentDataModelExtender(DataModel dataModel, IInsertionPoint insertionPoint) {
        super(dataModel, insertionPoint);
    }

    public void abort() {
        // NOP
    }

    public void finish() {
        // NOP
    }
    
    public void beginCurrentConstraint(String constraintId) throws BuilderException {
        final int ROOT_SIZE = 2;
        createRoot(ROOT_SIZE);
        peek().put("constraint", getResult().getConstraint(constraintId));
    }
    
    public void beginOccurrences(int nbrOccurrences) throws BuilderException {
        beginList(nbrOccurrences, "occurrences");
    }

    public void beginOccurrence(int ruleNbr, int occurrenceNbr) throws BuilderException {
        final int OCCURRENCE_SIZE = 3;

        final Map<String, Object> occurrence = new HashMap<String, Object>(
                OCCURRENCE_SIZE);
        occurrence.put("ruleNbr", Integer.valueOf(ruleNbr));
        occurrence.put("occurrenceNbr", Integer.valueOf(occurrenceNbr));
        peek().add(occurrence);
        push(occurrence);
    }

    public void beginOccurrenceCommandos() throws BuilderException {
        beginList(16, "commandos");
    }

    public CheckFiredCommandoBuilder getCheckFiredCommandoBuilder()
            throws BuilderException {
        return getCommandoBuilder(CommandoType.CHECK_FIRED);
    }

    public CheckGuardCommandoBuilder getCheckGuardCommandoBuilder()
            throws BuilderException {
        return getCommandoBuilder(CommandoType.GUARD);
    }

    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.AbstractDataModelBuilder#getCommitCommandoBuilder()
     */
    public CommitCommandoBuilder getCommitCommandoBuilder() throws BuilderException {
        return getCommandoBuilder(CommandoType.COMMIT);
    }

    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.AbstractDataModelBuilder#getSearchCommando()
     */
    public LookupCommandoBuilder getLookupCommandoBuilder() throws BuilderException {
        return getCommandoBuilder(CommandoType.LOOKUP);
    }

    /**
     * @see be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.AbstractDataModelBuilder#endOccurrenceCommandos()
     */
    public void endOccurrenceCommandos() throws BuilderException {
        pop();
    }

    public void endOccurrence() throws BuilderException {
        pop();
    }

    public void endOccurrences() throws BuilderException {
        pop();
    }
    
    public void endCurrentConstraint() throws BuilderException {
        popAndInsert();
    }
    
    private final CommandoBuilderImpl commandoBuilderImpl = new CommandoBuilderImpl();

    protected void setCommandoBuilderType(CommandoType type) {
        getCommandoBuilder().setType(type);
    }

    protected CommandoBuilderImpl getCommandoBuilder() {
        return commandoBuilderImpl;
    }

    protected CommandoBuilderImpl getCommandoBuilder(CommandoType type) {
        setCommandoBuilderType(type);
        return getCommandoBuilder();
    }

    protected class CommandoBuilderImpl implements CommandoBuilder,
            LookupCommandoBuilder, CheckFiredCommandoBuilder,
            CheckGuardCommandoBuilder, CommitCommandoBuilder {

        private CommandoType type;
        
        protected CommandoBuilderImpl() {
            // NOP (just indicating it should be a protected constructor)
        }

        /*
         * COMMANDO BUILDER
         *******************/        
        protected CommandoType getType() {
            return type;
        }
        public void setType(CommandoType type) {
            this.type = type;
        }

        /**
         * @see be.kuleuven.jchr.compiler.codeGeneration.code.commando.CommandoBuilder#beginCommando()
         */
        public void beginCommando() throws BuilderException {
            final int COMMANDO_SIZE = getType().getInfoSize();

            final Map<String, Object> commando = 
                new HashMap<String, Object>(COMMANDO_SIZE);
            commando.put("type", getType());
            peek().add(commando);
            push(commando);
        }

        /**
         * @see be.kuleuven.jchr.compiler.codeGeneration.code.commando.CommandoBuilder#endCommando()
         */
        public void endCommando() throws BuilderException {
            pop(); // info
        }

        /*
         * LOOKUP COMMANDO BUILDER
         **************************/
        public void beginLookup(int ruleNbr, int occurrenceNbr, int lookupCategoryIndex, int lookupTypeIndex) throws BuilderException {
            final Map occurrence = getResult().getOccurrence(ruleNbr, occurrenceNbr - 1);
            final Map lookupType = getResult().getLookupType(
                (String)occurrence.get("identifier"), 
                lookupCategoryIndex, 
                lookupTypeIndex
            );
            peek().put("occurrence", occurrence);
            peek().put("lookupType", lookupType);
        }
        
        public IInsertionPoint getLookupArgumentsInsertionPoint() throws BuilderException {
            return getCurrentInsertionPoint("arguments");
        }
        
        public void endLookup() throws BuilderException {
            // NOP
        }
        
        /*
         * CHECK FIRED COMMANDO BUILDER
         *******************************/
        /**
         * @see be.kuleuven.jchr.compiler.codeGeneration.code.commando.CheckFiredCommandoBuilder#buildCheck(int)
         */
        public void buildFiredCheck(int ruleNbr, int occurrenceNumber) throws BuilderException {            
            peek().put("ruleNbr", Integer.valueOf(ruleNbr));
            peek().put("occurrenceNbr", Integer.valueOf(occurrenceNumber));
        }

        /*
         * CHECK GUARD COMMANDO BUILDER
         ********************************/
        /**
         * @see be.kuleuven.jchr.compiler.codeGeneration.code.commando.CheckGuardCommandoBuilder#buildCheck(java.lang.String)
         */
        public void buildCheck(int ruleNbr, int checkIndex) throws BuilderException {            
            peek().put("occurrence", getResult().getGuardConjunct(ruleNbr, checkIndex));
        }

        /*
         * COMMIT COMMANDO BUILDER
         ***************************/
        /**
         * @see be.kuleuven.jchr.compiler.codeGeneration.code.commando.CommitCommandoBuilder#buildCommit(int,
         *      int, int)
         */
        public void buildCommit(int ruleNbr, int occurrenceNbr) throws BuilderException {
            peek().put("ruleNbr", Integer.valueOf(ruleNbr));
            peek().put("occurrenceNbr", Integer.valueOf(occurrenceNbr));
        }
    }
}