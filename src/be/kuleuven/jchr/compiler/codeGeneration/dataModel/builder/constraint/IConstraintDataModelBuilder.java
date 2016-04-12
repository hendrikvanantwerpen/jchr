package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.lookupType.HashMapArgumentInfosBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.constraint.lookupType.NoneArgumentInfosBuilder;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IDataModelBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;


/**
 * @author Peter Van Weert
 */
public interface IConstraintDataModelBuilder<Result> extends IDataModelBuilder<Result> {

public void beginConstraint(String identifier, String infix) throws BuilderException;

    public void beginConstraintVariables(int nbVariables) throws BuilderException;

        public void addConstraintVariable(String identifier, int variableTypeIndex) throws BuilderException;

    public void endConstraintVariables() throws BuilderException;
    
    public void beginLookupCategories(int nbrLookupCategories) throws BuilderException;
    
        public void beginLookupCategory(String indexType, boolean master) throws BuilderException;

            public void beginLookupTypes(int nbrLookupTypes) throws BuilderException;
                    
                public void beginLookupType() throws BuilderException;
                    
                    public NoneArgumentInfosBuilder getNoneArgumentInfosBuilder() throws BuilderException;
                    public HashMapArgumentInfosBuilder getHashMapArgumentInfosBuilder() throws BuilderException;
                        
                public void endLookupType() throws BuilderException;
                        
            public void endLookupTypes() throws BuilderException;
            
            public void beginVariables(int nbrVariables) throws BuilderException;
                
                public void addVariable(int variableIndex) throws BuilderException;
            
            public void endVariables() throws BuilderException;
            
        public void endLookupCategory() throws BuilderException;
        
    public void endLookupCategories() throws BuilderException;
		
public void endConstraint() throws BuilderException;

}