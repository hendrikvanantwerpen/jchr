package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando;

import be.kuleuven.jchr.util.builder.BuilderException;

/**
 * @author Peter Van Weert
 */
public interface CheckFiredCommandoBuilder extends CommandoBuilder {
    public void buildFiredCheck(int ruleNbr, int occurrenceNbr) throws BuilderException;    
}
