package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando;

import be.kuleuven.jchr.util.builder.BuilderException;

/**
 * @author Peter Van Weert
 */
public interface CommitCommandoBuilder extends CommandoBuilder {
    public void buildCommit(
        int ruleNbr, 
        int occurrenceNbr
    )  throws BuilderException; 
}
