package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.lookupType;

import be.kuleuven.jchr.util.builder.BuilderException;

public interface LookupInfoBuilder {

    public void beginGuardInfos(int nbGuardInfos) throws BuilderException;
    
    public void beginGuardInfo() throws BuilderException;
    
    public void endGuardInfo() throws BuilderException;
    
    public void endGuardInfos() throws BuilderException;
}
