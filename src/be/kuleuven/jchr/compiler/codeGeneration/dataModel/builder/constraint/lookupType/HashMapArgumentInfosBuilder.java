package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.lookupType;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.util.builder.BuilderException;


public interface HashMapArgumentInfosBuilder extends LookupInfoBuilder {
    
    public void buildOtherType(String otherType) throws BuilderException;
    
    public IInsertionPoint getEqInsertionPoint() throws BuilderException;
}
