package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.tuple;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director.DataModelDirector;
import be.kuleuven.jchr.util.builder.BuilderException;

public class TupleDataModelDirector<B extends ITupleDataModelBuilder<?>> extends DataModelDirector<B> {

    private int arity;
    private String pkg;
    
    public TupleDataModelDirector(B builder) {
        super(builder);
    }
    
    public TupleDataModelDirector(B builder, int arity, String pkg ) {
        this(builder);
        setArity(arity);
        setPackage(pkg);
    }

    @Override
    protected void construct2() throws BuilderException {
        getBuilder().buildArity(getArity(),getPackage());
    }

    public int getArity() {
        return arity;
    }
    public void setArity(int arity) {
        this.arity = arity;
    }
    public String getPackage(){
    	return pkg;
    }
    public void setPackage( String pkg ){
    	this.pkg = pkg;
    }
}