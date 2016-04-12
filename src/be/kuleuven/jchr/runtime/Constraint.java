package be.kuleuven.jchr.runtime;

import java.util.Iterator;

import be.kuleuven.jchr.runtime.list.SingleLinkedList;


/**
 * @author Peter Van Weert
 */
public abstract class Constraint 
// deze extends is misschien een van de flagrantste design-foutjes in dit systeem :-)
// kan eenvoudig vervangen worden door een decorator-aanpak natuurlijk ;-)
extends SingleLinkedList<be.kuleuven.jchr.runtime.list.DoubleLinkedList.Node<? extends Constraint>> {
    
    // Dummy value, used in the propagation histories...
    protected final static Object PRESENT = new Object();
    
    protected boolean alive = true;
    
    protected boolean stored = false;
    
    private static int idCounter = 0;//Integer.MIN_VALUE;
    
    public final int ID = idCounter++;
    
    protected int generation;
    
    public boolean isAlive() {
        return alive;
    }
    
    public boolean isStored() {
        return stored;
    }
    
    public boolean isNewerThan(Constraint other) {
        return this.ID > other.ID;
    }
    public boolean isOlderThan(Constraint other) {
        return this.ID < other.ID;
    }
    
    public void kill() {
        alive = false;
        
        if (stored) {
            final Iterator<be.kuleuven.jchr.runtime.list.DoubleLinkedList.Node<? extends Constraint>> 
                nodesContainingMe = this.iterator();
            while (nodesContainingMe.hasNext())
                nodesContainingMe.next().removeSelf();

            clear();
        }
    }
    
    /**
     * Gaat na of een bepaalde <code>Node</code> mag worden toegevoegd,
     * maw of de <code>Node</code> deze constraint bevat.  
     * 
     * @param x 
     *  De te controleren <code>Node</code>.
     * @return Gaat na of de gegeven knoop <code>x</code> wel degelijk 
     *  deze constraint bevat.<br/>
     *  <code>(x != null) && this == x.getValue()</code>
     */
    @Override
    public boolean canAdd(be.kuleuven.jchr.runtime.list.DoubleLinkedList.Node<? extends Constraint> x) {
        return super.canAdd(x) && this == x.getValue();
    }

    public abstract void activate();
    
    public abstract void reactivate();
}