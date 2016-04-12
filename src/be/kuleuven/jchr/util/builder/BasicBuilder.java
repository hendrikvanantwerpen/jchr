package be.kuleuven.jchr.util.builder;

public abstract class BasicBuilder<Result> extends Builder<Result> {

    /* 
     * Do not remove constructor, since a no-argument constructor
     * is needed!
     */ 
    public BasicBuilder() {
        // NOP 
    }
    public BasicBuilder(Result result) {
        setResult(result);
    }

    private Result result;

    @Override
    public Result getResult() throws BuilderException {        
        return result;
    }
    protected void setResult(Result result) {
        this.result = result;
    }
}
