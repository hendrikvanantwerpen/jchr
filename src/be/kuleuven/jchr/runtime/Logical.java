package be.kuleuven.jchr.runtime;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import be.kuleuven.jchr.annotations.JCHR_Coerce;
import be.kuleuven.jchr.annotations.JCHR_Declare;
import be.kuleuven.jchr.annotations.JCHR_Init;
import be.kuleuven.jchr.runtime.Handler.MutableStorageKey;
import be.kuleuven.jchr.runtime.hash.HashObservable;
import be.kuleuven.jchr.runtime.hash.MutableStorageKeySet;
import be.kuleuven.jchr.runtime.list.ConstraintLinkedList;

/**
 * @author Peter Van Weert
 */
public class Logical<Type> implements Observable, HashObservable {
    public final static Logical<Boolean> getTrueInstance() {
        return new Logical<Boolean>(TRUE) {
            @Override
            public int hashCode() {
                return 1;
            }
            
            @Override
            public void reset() {
                super.reset();
                this.value = TRUE;
            }
            
            @Override
            public String toString() {
                return "true";
            }
        };
    }
    public final static Logical<Boolean> getFalseInstance() {
        return new Logical<Boolean>(FALSE) {
            @Override
            public int hashCode() {
                return 0;
            }
            
            @Override
            public void reset() {
                super.reset();
                this.value = FALSE;
            }
            
            @Override
            public String toString() {
                return "false";
            }
        };
    }
    
    protected Type value;

    protected Logical<Type> parent = this;

    protected int rank;

    protected ConstraintLinkedList<Constraint> variableObservers = new ConstraintLinkedList<Constraint>();
    
    protected MutableStorageKeySet hashObservers; 
    
    private String name;

    private static int counter;

    @JCHR_Declare
    public Logical() {
        this("$_" + counter++);
    }

    @JCHR_Declare
    public Logical(String name) {
        this.name = name;
    }
    
    @JCHR_Init
    public Logical(Type value) {
        this();
        this.value = value;
    }

    @JCHR_Init( identifier = 0 )
    public Logical(String name, Type value) {
        this.name = name;
        this.value = value;
    }

    public final void addConstraintObserver(Constraint constraint) {
        find().variableObservers.addFirst(constraint);
    }
    
    public final void addHashObserver(MutableStorageKey observer) {
        final Logical<Type> found = find(); 
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
        value = null;
        rank = 0;
        parent = this;
    }
    
    public final boolean isVar() {
        return (find().value == null);
    }

    public final boolean isGround() {
        return (find().value != null);
    }
    
    public final boolean isNonVar() {
        return (find().value != null);
    }

    @JCHR_Coerce
    public final Type getValue() {
        return find().value;
    }

    public final Logical<Type> find() {
        if (parent != this)
            return (parent = parent.find());
        else
            return this;
    }
    
    @Override
    public int hashCode() {
        return parent == this
            ? ( value == null
                ? super.hashCode()
                : value.hashCode() )
            : parent.hashCode();
    }

    @Override
    public String toString() {
        final Type value = getValue();
        if (value == null)
            return name;
        else
            return value.toString();
    }
}