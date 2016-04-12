package be.kuleuven.jchr.util.iterator;

import java.util.ListIterator;

public interface ResettableListIterator<T> 
    extends ListIterator<T>, ResettableIterator<T> {
    
    /* no extra methods needed! */
}