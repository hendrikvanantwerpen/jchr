package be.kuleuven.jchr.util.builder;

public abstract class Builder<Result> implements IBuilder<Result> {

    public abstract void init() throws BuilderException;
    
    public abstract void abort() throws BuilderException;
    
    public abstract void finish() throws BuilderException;
    
    public Result getResult() 
    throws UnsupportedOperationException, BuilderException {
        throw new UnsupportedOperationException();
    }   
}