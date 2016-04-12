package be.kuleuven.jchr.runtime;

import java.util.Iterator;

public class EqualitySolverImpl<Type> implements EqualitySolver<Type> {

    public void tellEqual(Logical<Type> X, Type value) {
        final Logical<Type> Xrepr = X.find();
        final Type oldValue = Xrepr.value; 
        
        if (oldValue != null) {
            if (!oldValue.equals(value))
                throw new FailureException("Cannot make equal " + oldValue + " != " + value); // TODO betere exceptie?
        }
        else {
            Xrepr.value = value;

            Xrepr.rehashAll();
            
            final Iterator<Constraint> observers = Xrepr.variableObservers.iterator();
            while (observers.hasNext()) 
                observers.next().reactivate();   /* notify */
        }
    }

    // TODO :: deze extra indirectie kan uiteraard weg in uiteindelijk versie...
    public void tellEqual(Type val, Logical<Type> X) {
        tellEqual(X, val);
    }
    
    public void tellEqual(Logical<Type> X, Logical<Type> Y) {
        final Logical<Type> Xrepr = X.find();
        final Logical<Type> Yrepr = Y.find();
        
        if (Xrepr != Yrepr) {
            final Type 
                Xvalue = Xrepr.value, 
	        	Yvalue = Yrepr.value;
            
            final int Xrank = Xrepr.rank;
            int Yrank = Yrepr.rank;
            
            if (Xrank >= Yrank) {
                Yrepr.parent = Xrepr;
                if (Xrank == Yrank) Xrepr.rank++;
                
                if (Xvalue == null) {
                    if (Yvalue != null) {
                        Xrepr.value = Yvalue;
                        Xrepr.rehashAll();
                    }
                } else /* Xvalue != null */ {
                    if (Yvalue != null && !Xvalue.equals(Yvalue))
                        throw new FailureException("Cannot make equal " + Xvalue + " != " + Yvalue);
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
                
                if (Yvalue == null) {
                    if (Xvalue != null) {
                        Yrepr.value = Xvalue;
                        Yrepr.rehashAll();
                    }
                } else /* Yvalue != null */ {
                    if (Xvalue != null && !Xvalue.equals(Yvalue))
                        throw new FailureException("Cannot make equal " + Xvalue + " != " + Yvalue);
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

    public boolean askEqual(Logical<Type> X, Type value) {
        return value.equals(X.find().value);
    }
    
    public boolean askEqual(Type value, Logical<Type> X) {
        return value.equals(X.find().value);
    }

    public boolean askEqual(Logical<Type> X, Logical<Type> Y) {
        final Logical<Type> Xrepr = X.find();
        final Logical<Type> Yrepr = Y.find();
        
        return (Xrepr == Yrepr) || (Xrepr.value != null && Xrepr.value.equals(Yrepr.value));
    }
}