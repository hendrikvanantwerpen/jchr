package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.basic;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director.IDataModelDirector;
import be.kuleuven.jchr.util.builder.BuilderException;


/**
 * @author Peter Van Weert
 */
public interface IBasicDataModelDirector<B extends IBasicDataModelBuilder<?>>
extends IDataModelDirector<B> {

    public abstract void constructHandler() throws BuilderException;
    
    public abstract void constructTypeParameters() throws BuilderException;
    
    public abstract void constructVariableTypes() throws BuilderException;
    
    public abstract void constructConstraints() throws BuilderException;

    public abstract void constructRules() throws BuilderException;
 
    public abstract void constructSolvers() throws BuilderException;
    
    public final static class Helper {
        private Helper() { /* non-instantiatable helper-class */ }
        public static void constructWith(IBasicDataModelDirector<?> director) throws BuilderException {
            director.getBuilder().init();
                director.constructPackage();
                director.constructHandler();
                director.constructTypeParameters();
                director.constructVariableTypes();
                director.constructSolvers();
                director.constructRules();
                director.constructConstraints();
            director.getBuilder().finish();
        }
    }

	public abstract void constructPackage() throws BuilderException;
}