package be.kuleuven.jchr.runtime.primitive;

import java.util.Iterator;

import be.kuleuven.jchr.runtime.Constraint;
import be.kuleuven.jchr.runtime.FailureException;


public class IntEqualitySolverImpl implements IntEqualitySolver {

    public void tellEqual(LogicalInt X, int value) {
        final LogicalInt Xrepr = X.find();
        final boolean oldHasValue = Xrepr.hasValue;
        
        if (oldHasValue) {
            if (Xrepr.value != value)
                throw new FailureException("Cannot make equal " + Xrepr.value + " != " + value);
        }
        else {
            Xrepr.value = value;
            Xrepr.hasValue = true;

            Xrepr.rehashAll();
            
            final Iterator<Constraint> observers = Xrepr.variableObservers.iterator();
            while (observers.hasNext()) 
                observers.next().reactivate();   /* notify */
        }
    }

    // TODO :: deze extra indirectie kan uiteraard weg in uiteindelijk versie...
    public void tellEqual(int val, LogicalInt X) {
        tellEqual(X, val);
    }
    
    public void tellEqual(LogicalInt X, LogicalInt Y) {
        final LogicalInt Xrepr = X.find();
        final LogicalInt Yrepr = Y.find();
        
        if (Xrepr != Yrepr) {
            final boolean 
                XhasValue = Xrepr.hasValue, 
	        	YhasValue = Yrepr.hasValue;
            
            final int Xrank = Xrepr.rank;
            int Yrank = Yrepr.rank;
            
            if (Xrank >= Yrank) {
                Yrepr.parent = Xrepr;
                if (Xrank == Yrank) Xrepr.rank++;
                
                if (! XhasValue) {
                    if (YhasValue) {
                        Xrepr.value = Yrepr.value;
                        Xrepr.hasValue = true;
                        Xrepr.rehashAll();
                    }
                } else /* XhasValue */ {
                    if (YhasValue && Xrepr.value != Yrepr.value)
                        throw new FailureException("Cannot make equal " 
                                + Xrepr.value + " != " + Yrepr.value);
                }
                
                if (Yrepr.hashObservers != null) {
                    Xrepr.mergeHashObservers(Yrepr.hashObservers);
                    Yrepr.hashObservers = null;
                }
                
                Xrepr.variableObservers.mergeWith(Yrepr.variableObservers);
                Yrepr.variableObservers = null;
                
                final Iterator<Constraint> observers = Xrepr.variableObservers.iterator();
                while (observers.hasNext()) 
                    observers.next().reactivate();   /* notify */
            }
            else {
                Xrepr.parent = Yrepr;
                
                if (! YhasValue) {
                    if (XhasValue) {
                        Yrepr.value = Xrepr.value;
                        Yrepr.hasValue = true;
                        
                        Yrepr.rehashAll();
                    }
                } else /* YhasValue */ {
                    if (XhasValue & Xrepr.value != Yrepr.value)
                        throw new FailureException("Cannot make equal " 
                                + Xrepr.value + " != " + Yrepr.value);
                }
                
                if (Xrepr.hashObservers != null) {
                    Yrepr.mergeHashObservers(Xrepr.hashObservers);
                    Xrepr.hashObservers = null;
                }
                
                Yrepr.variableObservers.mergeWith(Xrepr.variableObservers);                
                Xrepr.variableObservers = null;
                
                final Iterator<Constraint> observers = Yrepr.variableObservers.iterator();
                while (observers.hasNext()) 
                    observers.next().reactivate();   /* notify */
            }
        }
    }

    public boolean askEqual(LogicalInt X, int value) {
        final LogicalInt representative = X.find();
        return representative.hasValue && representative.value == value;
    }
    
    public boolean askEqual(int value, LogicalInt X) {
        final LogicalInt representative = X.find();
        return representative.hasValue && representative.value == value;
    }

    public boolean askEqual(LogicalInt X, LogicalInt Y) {
        final LogicalInt Xrepr = X.find();
        final LogicalInt Yrepr = Y.find();
        
        return (Xrepr == Yrepr) 
            || (Xrepr.hasValue && Yrepr.hasValue && Xrepr.value == Yrepr.value);
    }
}