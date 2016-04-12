package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando;

import be.kuleuven.jchr.util.builder.BuilderException;

/**
 * @author Peter Van Weert
 */
public interface CheckGuardCommandoBuilder extends CommandoBuilder {
    public void buildCheck(int ruleNbr, int checkIndex) throws BuilderException;
}