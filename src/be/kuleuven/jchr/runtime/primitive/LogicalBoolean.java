package be.kuleuven.jchr.runtime.primitive;

import be.kuleuven.jchr.annotations.JCHR_Coerce;
import be.kuleuven.jchr.annotations.JCHR_Declare;
import be.kuleuven.jchr.annotations.JCHR_Init;
import be.kuleuven.jchr.runtime.Constraint;
import be.kuleuven.jchr.runtime.Observable;
import be.kuleuven.jchr.runtime.Handler.MutableStorageKey;
import be.kuleuven.jchr.runtime.hash.HashObservable;
import be.kuleuven.jchr.runtime.hash.MutableStorageKeySet;
import be.kuleuven.jchr.runtime.list.ConstraintLinkedList;

/**
 * @author Peter Van Weert
 */
public class LogicalBoolean implements Observable, HashObservable {
    public final static LogicalBoolean getTrueInstance() {
        return new LogicalBoolean(true) {
            @Override
            public int hashCode() {
                return 1;
            }
            
            @Override
            public void reset() {
                super.reset();
                this.value = true;
                this.hasValue = true;
            }
            
            @Override
            public String toString() {
                return "true";
            }
        };
    }
    public final static LogicalBoolean getFalseInstance() {
        return new LogicalBoolean(false) {
            @Override
            public int hashCode() {
                return 0;
            }
            
            @Override
            public void reset() {
                super.reset();
                this.value = false;
                this.hasValue = true;
            }
            
            @Override
            public String toString() {
                return "false";
            }
        };
    }
    
    protected boolean value;
    protected boolean hasValue;

    protected LogicalBoolean parent = this;

    protected int rank;

    protected ConstraintLinkedList<Constraint> variableObservers = new ConstraintLinkedList<Constraint>();
    
    protected MutableStorageKeySet hashObservers; 
    
    private String name;

    private static int counter;

    @JCHR_Declare
    public LogicalBoolean() {
        this("$_" + counter++);
    }

    @JCHR_Declare
    public LogicalBoolean(String name) {
        this.name = name;
    }
    
    @JCHR_Init
    public LogicalBoolean(boolean value) {
        this();
        this.value = value;
        this.hasValue = true;
    }

    @JCHR_Init( identifier = 0 )
    public LogicalBoolean(String name, boolean value) {
        this.name = name;
        this.value = value;
        this.hasValue = true;
    }

    public final void addConstraintObserver(Constraint constraint) {
        find().variableObservers.addFirst(constraint);
    }
    
    public final void addHashObserver(MutableStorageKey observer) {
        final LogicalBoolean found = find(); 
        final MutableStorageKeySet observers = found.hashObservers;
        if (observers == null)
            found.hashObservers = new MutableStorageKeySet(observer);
        else
            observers.add(observer);
    }
    
    public void rehashAll() {
        if (hashObservers != null) hashObservers.rehashAll();
    }
    
    /**
     * {@inheritDoc}
     * <br/>
     * As the result will be stored in this <code>Logical</code>,
     * it is clearly the case that this <code>Logical</code> will
     * be the <em>new</em> representative. Also: this was already
     * a representative of course, so these hashes will remain 
     * the same, unless we have received a value we didn't have
     * before (in this case rehashing has to be done elsewhere, as
     * noted before).
     */
    public void mergeHashObservers(MutableStorageKeySet others) {
        // the trivial case where we do not have observers yet:
        if (hashObservers == null) {
            hashObservers = others;
            // these hashes could have just changed now of course!
            others.rehashAll();
        } 
        // else we have the non-trivial case: 
        else hashObservers.mergeWith(others);
    }
    
    public MutableStorageKeySet getHashObservers() {
        return hashObservers;
    }
    
    public void reset() {
        hashObservers = null;
        if (variableObservers == null) 
            variableObservers = new ConstraintLinkedList<Constraint>();
        else
            variableObservers.clear();
        hasValue = false;
        rank = 0;
        parent = this;
    }
    
    public final boolean isVar() {
        return !find().hasValue;
    }

    public final boolean isGround() {
        return find().hasValue;
    }
    
    public final boolean isNonVar() {
        return find().hasValue;
    }

    @JCHR_Coerce
    public final boolean getValue() {
        return find().value;
    }

    public final LogicalBoolean find() {
        if (parent != this)
            return (parent = parent.find());
        else
            return this;
    }
    
    @Override
    public int hashCode() {
        return parent == this
            ? ( hasValue
                ? ( value? 1 : 0 )
                : super.hashCode() )
            : parent.hashCode();
    }

    @Override
    public String toString() {
        final LogicalBoolean found = find(); 
        if (!found.hasValue)
            return name;
        else
            return found.value? "true" : "false";
    }
}