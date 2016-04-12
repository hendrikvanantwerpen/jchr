package be.kuleuven.jchr.runtime.list;

import be.kuleuven.jchr.runtime.Constraint;
import be.kuleuven.jchr.runtime.Handler.MutableStorageKey;

public class HashMapConstraintLinkedList<T extends Constraint> extends ConstraintLinkedList<T> {
    
    MutableStorageKey key;
    
    public HashMapConstraintLinkedList(MutableStorageKey key) {
        this.head = new Node<T>();
        this.key = key;
    }
    
    public MutableStorageKey getKey() {
        return key;
    }
    
    protected class Node<S> extends DoubleLinkedList.Node<S> {
        @Override
        public void removeSelf() {
            super.removeSelf();
            // This is not bulletproof! Keys could remain in the hashmap
            // if e.g. (next != null && (next.removed) && next.next.next = null)
            // Longer chains of removed Nodes are also possible (I think)
            if (next == null && previous.previous == null)
                key.removeSelf();
        }
    }
}
