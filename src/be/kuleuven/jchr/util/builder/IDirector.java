package be.kuleuven.jchr.util.builder;


/**
 * @author Peter Van Weert
 */
public interface IDirector<B extends IBuilder<?>> {    
    /**
     * Vertel de director dat hij mag beginnen bouwen.
     * 
     * @pre getBuilder() != null
     * 
     * @throws BuilderException
     * 	Er kan uiteraard wel het een het ander misgaan
     * 	tijdens het constructie-proces, vandaar deze
     * 	declaratie.
     */
    public abstract void construct() throws BuilderException;

    /**
     * Geeft de builder terug die deze director dirigeert.
     * 
     * @return De builder die deze director dirigeert.
     */
    public B getBuilder();
}
