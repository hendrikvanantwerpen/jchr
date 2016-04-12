package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando;

import be.kuleuven.jchr.util.builder.BuilderException;

/**
 * @author Peter Van Weert
 */
public interface CommandoBuilder {

    public void beginCommando() throws BuilderException;
    
    public void endCommando() throws BuilderException;
    
}