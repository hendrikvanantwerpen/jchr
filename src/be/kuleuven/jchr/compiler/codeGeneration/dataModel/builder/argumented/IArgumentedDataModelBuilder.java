package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IDataModelBuilder;
import be.kuleuven.jchr.util.builder.BuilderException;


public interface IArgumentedDataModelBuilder<Result> extends IDataModelBuilder<Result> {
    public void beginOccurrence(String identifier, int nb, boolean isRemoved, boolean isPassive) throws BuilderException;
    public void beginUserDefinedConjunct(String identifier) throws BuilderException;
    public void beginMethodInvocation(String method) throws BuilderException;
    public void beginInfixConjunct(String identifier) throws BuilderException;
    
    public void beginDeclaration(String type) throws BuilderException;
    
    public void beginLookup() throws BuilderException;
    
        public void beginArguments(int nbArguments) throws BuilderException;
        
            public void beginArgument() throws BuilderException;
            
                public void buildVariableArgument(String identifier, boolean anonymous) throws BuilderException;
                public void buildSolverArgument(String identifier) throws BuilderException;
                public void buildClassNameArgument(String name) throws BuilderException;
                
                public void buildBooleanArgument(Boolean value) throws BuilderException;
                public void buildByteArgument(Byte value) throws BuilderException;
                public void buildShortArgument(Short value) throws BuilderException;
                public void buildIntArgument(Integer value) throws BuilderException;
                public void buildCharArgument(Character value) throws BuilderException;
                public void buildLongArgument(Long value) throws BuilderException;
                public void buildFloatArgument(Float value) throws BuilderException;
                public void buildDoubleArgument(Double value) throws BuilderException;
                public void buildStringArgument(String value) throws BuilderException;
                
                public void buildNullArgument() throws BuilderException;
                public void buildOneDummyArgument() throws BuilderException;
                public void buildOtherDummyArgument() throws BuilderException;
            
                public void beginConstructorInvocationArgument(String type) throws BuilderException;
                public void beginMethodInvocationArgument(String name) throws BuilderException;
                public void beginFieldAccessArgument(String field) throws BuilderException;

                    // arguments......
                
                public void endFieldAccessArgument() throws BuilderException;
                public void endMethodInvocationArgument() throws BuilderException;
                public void endConstructorInvocationArgument() throws BuilderException;
                
            public void endArgument() throws BuilderException;
        
        public void endArguments() throws BuilderException;

    public void endArgumented() throws BuilderException;
}
