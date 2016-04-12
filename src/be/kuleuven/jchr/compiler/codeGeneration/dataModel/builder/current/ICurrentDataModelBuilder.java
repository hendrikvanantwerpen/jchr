package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IDataModelBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CheckFiredCommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CheckGuardCommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.CommitCommandoBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando.LookupCommandoBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;


public interface ICurrentDataModelBuilder<Result> extends IDataModelBuilder<Result> {
    
public void beginCurrentConstraint(String constraintId) throws BuilderException;

    public void beginOccurrences(int nbrOccurrences) throws BuilderException;

        public void beginOccurrence(int ruleNbr, int occurrenceNbr) throws BuilderException;
            
            public void beginOccurrenceCommandos() throws BuilderException;
            
                public LookupCommandoBuilder getLookupCommandoBuilder() throws BuilderException;
                public CheckGuardCommandoBuilder getCheckGuardCommandoBuilder() throws BuilderException;
                public CheckFiredCommandoBuilder getCheckFiredCommandoBuilder() throws BuilderException;
                public CommitCommandoBuilder getCommitCommandoBuilder() throws BuilderException;
            
            public void endOccurrenceCommandos() throws BuilderException;
        
        public void endOccurrence() throws BuilderException;
    
    public void endOccurrences() throws BuilderException;

public void endCurrentConstraint() throws BuilderException;
}
