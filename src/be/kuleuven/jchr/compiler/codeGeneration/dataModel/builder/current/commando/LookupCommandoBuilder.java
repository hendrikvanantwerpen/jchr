package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.util.builder.BuilderException;


/**
 * @author Peter Van Weert
 */
public interface LookupCommandoBuilder extends CommandoBuilder {
    
    public void beginLookup(int ruleNbr, int occurrenceNbr, int lookupCategory, int lookupType) throws BuilderException;
    
        public IInsertionPoint getLookupArgumentsInsertionPoint() throws BuilderException;
    
    public void endLookup() throws BuilderException;
    
}