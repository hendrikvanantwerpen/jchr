package be.kuleuven.jchr.util.builder;


/**
 * @author Peter Van Weert
 */
public abstract class Director<B extends IBuilder<?>> 
implements IDirector<B> {

    /**
     * De builder die deze director dirigeert. 
     */
    private B builder;
    
    /**
     * Maakt een nieuwe Director aan die <code>builder</code>
     * dirigeert.
     * 
     * @pre builder != null
     * @post getBuilder().equals(builder)
     *  
     * @param builder
     * 	De builder die deze director dirigeert.
     */
    public Director(B builder) {
        setBuilder(builder);
    }

    /**
     * @inheritDoc
     */
    public void construct() throws BuilderException {
         getBuilder().init();
         construct2();
         getBuilder().finish();
    }
    
    protected abstract void construct2() throws BuilderException;
    
    /**
     * @inheritDoc
     */
    public B getBuilder() {
        return builder;
    }
    
    /**
     * Stelt de builder in die deze director dirigeert. 
     */
    protected void setBuilder(B builder) {
        this.builder = builder;
    }
}
