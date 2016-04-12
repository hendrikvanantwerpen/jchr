package be.kuleuven.jchr.util.builder;

/**
 * Iedere buildermethode kan per definitie een BuilderException
 * gooien. Dit is een generische exceptie: het is duidelijk niet
 * geweten welke uitzonderingen er in concrete builders kunnen
 * optreden.
 * 
 * @author Peter Van Weert
 */
public interface IBuilder<Result> {

    /**
     * Hier moeten eventuele benodigde bronnen worden 
     * gereserveerd, niet bij constructie...
     */
    public void init() throws BuilderException;

    /**
     * Het is steeds mogelijk dat een director besluit het
     * bouwen te onderbreken.
     */
    public void abort() throws BuilderException;

    /**
     * Hier moeten de gereserveerde bronnen worden vrijgegeven...
     */
    public void finish() throws BuilderException;

    /**
     * Geeft het resultaat van het constructieproces.
     * Deze methode hoeft niet geïmplementeerd te zijn...
     * 
     * @return Het resultaat van het constructieproces.
     * 
     * @exception UnsupportedOperationException
     * 	Deze operatie hoeft niet geïmplementeerd te zijn...
     */
    public Result getResult() 
    throws UnsupportedOperationException, BuilderException;

}