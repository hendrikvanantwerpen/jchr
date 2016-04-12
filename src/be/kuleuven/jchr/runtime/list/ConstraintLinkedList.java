package be.kuleuven.jchr.runtime.list;

import java.util.Iterator;

import be.kuleuven.jchr.runtime.Constraint;


public class ConstraintLinkedList<T extends Constraint> extends DoubleLinkedList<T> {

    /**
     * {@inheritDoc}
     * 
     * @return True als en slechts als de <code>ID</code> van de gegeven
     *  constraint kleiner is dan die van het eerste element. 
     */
    @Override
    public boolean canAdd(T x) {
        return isEmpty() || x.isNewerThan(head.next.value);
    }
    
    @Override
    public void addFirst(T x) {
        head.value = x;
        x.addFirst(head);
        head = new Node<T>(head);
    }
    
    /**
     * Checks whether this list is a valid <code>ConstraintLinkedList</code>: i.e.
     * all next and previous pointers are consistent <b>and</b>
     * the constraints in the lists are sorted on ID! Also: all constraints 
     * (returned by an iterator) have to be alive, and know in which node from
     * this list they are stored!
     *  
     * @return True if this list is a valid <code>ConstraintLinkedList</code>;
     *  false otherwise.
     */
    @Override
    public boolean isValid() {
        // First we chech the consistency of the next/previous pointers
        if (! super.isValid()) return false;
        
        // Check whether an iterator returns the constraints
        // in the correct order, and only returns constrains
        // that are alive.
        final Iterator<T> iter = iterator();
        if (! iter.hasNext()) return true;        
        T current = iter.next();
        if (! current.isAlive()) return false;        
        int ID = current.ID;
        while (iter.hasNext()) {
            current = iter.next();
            if (! current.isAlive()) return false;
            if (current.ID >= ID) return false;
            ID = current.ID;
        }
        
        // Next we iterate a final time, checking the constraints back pointers
        // (performance: can be improved by only iterating twice, but who cares)
        Node<T> currentN = head;
        while (currentN.next != null) {
            currentN = currentN.next;
            if (! currentN.value.contains(currentN)) 
                return false;
        }
        
        return true;
    }
    
    /**
     * Merges two list: this one and the given argument (<code>other</code>).
     * Both lists of constraints are sorted in descending ID, as will the result.
     * The result will be stored in this list. None of the iterators over
     * either list will be disturbed, nor will they start iterating over
     * constraints that weren't in <code>this</code> or <code>other</code>
     * respectively at the time the iterators were created. 
     * Note that in order to accomplish this, when both lists are non-empty,
     * we <em>have</em> to duplicate the entire lists! 
     * The constraints will be notified that they are added to the new
     * list (node). After the merge a constraint can be in the new list as well 
     * as in both old lists. Only when one of the lists is empty we can
     * reuse the other list.    
     * 
     * @param other
     *  The list of constraints to merge with. 
     */
    public void mergeWith(ConstraintLinkedList<T> other) {
        Node<T> thisCurrent = this.head.next, 
               otherCurrent = other.head.next;
        
        if (thisCurrent != null && otherCurrent != null) {
            // Release the old list (iterators must not be disturbed)
            head = new Node<T>();
            
            int thisID = thisCurrent.value.ID,
               otherID = otherCurrent.value.ID;
            
            Node<T> tail = head;
            Node<T> temp;
            T tempValue;
            
            while (true) {
                while (thisID > otherID) {
                    tempValue = thisCurrent.value;
                    temp = new Node<T>(tempValue, tail);
                    tempValue.addFirst(temp);
                    tail.next = temp;
                    tail = temp;
                    thisCurrent = thisCurrent.next;
                    if (thisCurrent == null) {
                        do {
                            tempValue = otherCurrent.value;
                            temp = new Node<T>(tempValue, tail);
                            tempValue.addFirst(temp);
                            tail.next = temp;
                            tail = temp;
                            otherCurrent = otherCurrent.next;
                        } while (otherCurrent != null);

                        return;
                    }
                    thisID = thisCurrent.value.ID;
                }
                /* thisID <= otherID */
                if (thisID == otherID) {
                    thisCurrent = thisCurrent.next;       // otherCurrent will be added next!
                    if (thisCurrent == null) {
                        do {
                            tempValue = otherCurrent.value;
                            temp = new Node<T>(tempValue, tail);
                            tempValue.addFirst(temp);
                            tail.next = temp;
                            tail = temp;
                            otherCurrent = otherCurrent.next;
                        } while (otherCurrent != null);

                        return;
                    }
                    thisID = thisCurrent.value.ID;
                }
                /* thisID < otherID */
                do {
                    tempValue = otherCurrent.value;
                    temp = new Node<T>(tempValue, tail);
                    tempValue.addFirst(temp);
                    tail.next = temp;
                    tail = temp;
                    otherCurrent = otherCurrent.next;
                    if (otherCurrent == null) {
                        do {
                            tempValue = thisCurrent.value;
                            temp = new Node<T>(tempValue, tail);
                            tempValue.addFirst(temp);
                            tail.next = temp;
                            tail = temp;
                            thisCurrent = thisCurrent.next;
                        } while (thisCurrent != null);

                        return;
                    }
                    otherID = otherCurrent.value.ID;
                } while (thisID < otherID);
                /* thisID >= otherID */
                if (thisID == otherID) {
                    otherCurrent = otherCurrent.next;     // thisCurrent will be added next!
                    if (otherCurrent == null) {
                        do {
                            tempValue = thisCurrent.value;
                            temp = new Node<T>(tempValue, tail);
                            tempValue.addFirst(temp);
                            tail.next = temp;
                            tail = temp;
                            thisCurrent = thisCurrent.next;
                        } while (thisCurrent != null);

                        return;
                    }
                    otherID = otherCurrent.value.ID;
                }
                /* thisID > otherID */
            }            
        }               
        else if (thisCurrent == null && otherCurrent != null) {
            this.head = other.head;
        } /* otherCurrent == null  ==>  nothing to merge! */
    }
    
//    public void mergeWith(ConstraintLinkedList<T> other) {
//        Node<T> thisCurrent = this.head.next, 
//               otherCurrent = other.head.next;
//                
//        if (thisCurrent != null && otherCurrent != null) {
//            int thisID = thisCurrent.value.ID,
//                otherID = otherCurrent.value.ID;
//
//            clear();
//outer:      while (true) {
//                while (thisID < otherID) {
//                    addFirst(thisCurrent.value);
//                    thisCurrent = thisCurrent.next;
//                    if (thisCurrent == null) break outer;
//                    thisID = thisCurrent.value.ID;
//                }
//                /* otherID <= thisID */
//                if (otherID == thisID) {
//                    thisCurrent = thisCurrent.next;
//                    if (thisCurrent == null) break outer;
//                    thisID = thisCurrent.value.ID;            
//                }
//                /* otherID < thisID */
//                while (otherID < thisID) {
//                    addFirst(otherCurrent.value);
//                    otherCurrent = otherCurrent.next;
//                    if (otherCurrent == null) break outer;
//                    otherID = otherCurrent.value.ID;
//                }
//                /* thisID <= otherID */
//                if (thisID == otherID) {                
//                    otherCurrent = otherCurrent.next;
//                    if (otherCurrent == null) break outer;
//                    otherID = otherCurrent.value.ID;            
//                }
//                /* thisID < otherID */
//            }
//            
//            /* thisCurrent == null ^ otherCurrent == null */
//            
//            if (thisCurrent == null) /* && otherCurrent != null */ {
//                if (thisID == otherCurrent.value.ID) 
//                    otherCurrent = otherCurrent.next;
//                
//                while (otherCurrent != null) {                
//                    addFirst(otherCurrent.value);
//                    otherCurrent = otherCurrent.next;
//                }
//            } else /* otherCurrent == null && thisCurrent != null */ {
//                if (otherID == thisCurrent.value.ID) 
//                    thisCurrent = thisCurrent.next;
//                
//                while (thisCurrent != null) {
//                    addFirst(thisCurrent.value);
//                    thisCurrent = thisCurrent.next;
//                }
//            }
//        }
//        
//        /* thisCurrent == null || otherCurrent == null */
//        
//        if (thisCurrent == null) {
//            while (otherCurrent != null) {                
//                addFirst(otherCurrent.value);
//                otherCurrent = otherCurrent.next;
//            }
//        } else /* thisCurrent != null && otherCurrent == null */ {
//            do {
//                addFirst(thisCurrent.value);
//                thisCurrent = thisCurrent.next;
//            } while (thisCurrent != null);
//        }
//    }
}