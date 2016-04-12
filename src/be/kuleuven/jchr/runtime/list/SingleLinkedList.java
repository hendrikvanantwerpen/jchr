package be.kuleuven.jchr.runtime.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

import be.kuleuven.jchr.util.IndexOutOfBoundsException;
import be.kuleuven.jchr.util.iterator.IteratorUtilities;


/**
 * Een lijst waarover niet-failfast iteratoren kunnen lopen zonder gevaar.
 * Er wordt enkel vooraan toegevoegd, en verwijderen gebeurt door het
 * loskoppelen van de interne <code>Node</code>s, zodat iteratoren
 * niet verstoord geraken.
 * 
 * @author Peter Van Weert
 */
public class SingleLinkedList<T> implements Iterable<T> {
    public SingleLinkedList() {
        // NOP
    }
    
    public <S extends T> SingleLinkedList(S... values) {
        for (int i = values.length-1; i >= 0; i--)
            addFirst(values[i]);
    }
    
    protected SingleLinkedList(Node<T> firstNode) {
        head.next = firstNode;
    }
    
    /**
     * De head van deze lijst.
     * @invar head != null
     */
    protected Node<T> head = new Node<T>();

    /**
     * Voegt een element <code>x</code> vooraan toe aan de lijst. Hierdoor
     * hebben iteratoren die over de lijst aan het itereren zijn er geen last
     * van.
     * 
     * @pre canAdd(x)
     * 
     * @param x
     *  Het toe te voegen element.
     */
    public void addFirst(T x) {
        head.value = x;
        head = new Node<T>(head);
    }
    
    /**
     * Gaat na of een element kan worden toegevoegd aan deze lijst.
     * 
     * @param x 
     *  Het element waar moet worden van nagegaan of het kan worden
     *  toegevoegd aan deze lijst.
     * @return Als x niet effectief is wordt null teruggegeven. <br/>
     *  <code>if (x == null) result == false</code>
     */
    public boolean canAdd(T x) {
        return (x != null);
    }

    /**
     * Geeft een iterator terug over deze lijst. Deze iterator is <b>NIET</b>
     * fail-fast zoals dat gebruikelijk is bij de Java-Collections implementaties.
     * 
     * @return Een iterator terug over deze lijst. Deze iterator is <b>NIET</b>
     *  fail-fast zoals dat gebruikelijk is bij de Java-Collections implementaties.
     */
    public Iterator<T> iterator() {
        return new NodeIterator<T>(head);
    }

    /**
     * Maakt de lijst van elementen los van dit object. Merk op dat de lijst
     * wel blijft verderbestaan, hij wordt niet vernietigd. Bestaande
     * iteratoren kunnen dus gerust verder itereren.
     * 
     * @post isEmpty()
     */
    public void clear() {
        head.next = null;
    }
    
    /**
     * Geeft <code>true</code> terug als deze lijst geen elementen bevat.
     *  
     * @return getSize() == 0 
     */
    public boolean isEmpty() {
        return (head.next == null);
    }
    
    /**
     * Returns the tail of this list. Inserting elements in the returned
     * object is <em>not</em> reflected in the original list.
     * The result will be <code>null</code> for the empty list. 
     * 
     * @return The tail of this list (<code>null</code> if the list
     *  is empty).
     */
    public SingleLinkedList<T> getTail() {
        final Node<T> node = head.next;
        if (node == null) return null;
        return new SingleLinkedList<T>(node.next);
    }
    
    /**
     * Returns the first element of this list.
     * 
     * @return The first element of this list.
     */
    public T getFirst() {
        return head.next.value;
    }
    
    /**
     * Returns the tail of this list, skipping the first <code>offset</offset> 
     * nodes. Inserting elements in the returned object is <em>not</em> 
     * reflected in the original list.
     * <br/>
     * Note that <code>getTail()</code> is equivalent to 
     * <code>getTail(1)</code>. <code>getTail(0)</code> will return
     * a clone of the current list.
     * <br/>
     * If there is no tail after the first <code>offset</code> elements
     * (i.e. <code>getSize() == offset</code>), null is returned. 
     * 
     * @param offset
     *  The number of nodes to skip.  
     * 
     * @return The tail of this list, skipping the first <code>offset</offset> 
     *  nodes.
     * 
     * @throws IndexOutOfBoundsException
     *  If there are less then offset elements in this list.
     */
    public SingleLinkedList<T> getTail(int offset) throws IndexOutOfBoundsException {
        Node<T> current = head;
        for (int i = 0; i <= offset; i++) {            
            current = current.next;
            if (current == null) return null;
        }
        return new SingleLinkedList<T>(current);
    }
    
    /**
     * Returns the first <code>n</code> elements of this list.
     * 
     * @param n
     *  The number of elements to return.
     *  
     * @return The first <code>n</code> elements of this list.
     * 
     * @throws IndexOutOfBoundsException
     *  Guess when it does this!
     */
    public Object[] getFirst(final int n) throws IndexOutOfBoundsException {
        Object[] result = new Object[n];
        Node<T> current = head;                
        for (int i = 0; i < n; i++) {
            if (current.next == null)
                throw new IndexOutOfBoundsException(n, i);
            current = current.next;
            result[i] = current.value;     
        }
        return result;
    }
    
    /**
     * Returns an array containing the <code>n</code> first elements in this list; 
     * the runtime type of the returned array is that of the specified array.  
     * The length of the array has to be exactly <code>n</code>, otherwise
     * an exception is thrown. 
     * <br/>
     * The elements are returned in the same order.
     * 
     * @param array
     *  a the array into which the elements of the collection are to
     *  be stored, if it is big enough; otherwise, a new array of the
     *  same runtime type is allocated for this purpose.
     * @param n
     *  The number of elements to return.
     * 
     * @return The first <code>n</code> elements of this list.
     * 
     * @throws IndexOutOfBoundsException
     *  When <code>n</code> is to big.
     * @throws IllegalArgumentException
     *  When <code>array.length != n</code>.
     * @throws ArrayStoreException the runtime type of the specified array is
     *  not a supertype of the runtime type of the elements in this
     *  list.
     */
    @SuppressWarnings("unchecked")
    public <S> S[] getFirst(S[] array, final int n)
    throws IndexOutOfBoundsException, IllegalArgumentException, ArrayStoreException {
        if (array.length != n)
            throw new IllegalArgumentException(array.length + " != " + n); 
        
        Node<T> current = head;
        for (int i = 0; i < n; i++) {
            if (current.next == null)
                throw new IndexOutOfBoundsException(n, i);
            current = current.next;            
            array[i] = (S)current.value;
        }
        return array;
    }
    
    /**
     * Checks whether a given object is in this collection.
     * 
     * @param object
     *  The object to look for. 
     * @return
     *  True if and only if <code>object</code> is in this list.
     */
    public boolean contains(Object object) {
        Node<T> current = head;
        while (current.next != null) {
            current = current.next;
            if (current.value.equals(object))
                return true;
        }
        return false;
    }
    
    /**
     * Returns a list containing all the elements in the current list,
     * excluding the given object. The elements are in the same order
     * as in the original list.
     * 
     * @param object
     *  The object to exclude from the resulting list.
     * @return A list containing all the elements in the current list,
     *  excluding the given object.
     */
    public SingleLinkedList<T> without(Object object) {
        SingleLinkedList<T> result = new SingleLinkedList<T>();
        
        Node<T> current = head;
        Node<T> tail = result.head, temp;
        
        while (current.next != null) {
            current = current.next;
            if (! current.value.equals(object)) {
                temp = new Node<T>(current.value);
                tail.next = temp;
                tail = temp;
            }
        }
        
        return result;
    }
    
    /**
     * Geeft het aantal elementen terug uit deze lijst.
     */
    public int size() {
        int result = 0;
        Node<T> current = head;
        while ((current = current.next) != null) result++;
        return result;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        return (obj instanceof SingleLinkedList)
            && equals((SingleLinkedList)obj);             
    }
    
    /**
     * Checks whether the given linked list is equal to this one, in the
     * sense that they contain the same elements (and the same number of
     * elements) in the same order.
     * 
     * @param other
     *  The list to compare with.
     * @return True if and only if the given linked list contains the 
     *  same elements as this list (and only those) in the same order.
     * 
     */
    public <S extends T> boolean equals(SingleLinkedList<S> other) {
        if (other == null) return false;
        if (this == other) return true;
        
        Node<T> thisCurrent = this.head;
        Node<S> otherCurrent = other.head;
        
        while (thisCurrent.next != null) {
            thisCurrent = thisCurrent.next;
            otherCurrent = otherCurrent.next;
            if (otherCurrent == null || !thisCurrent.value.equals(otherCurrent.value)) 
                return false;
        }
        
        return (otherCurrent.next == null);
    }
    
    /**
     * Geeft een tekstuele representatie weer van deze lijst. 
     */
    @Override
    public String toString() {
        return IteratorUtilities.toString(this);
    }
    
    protected final static class NodeIterator<S> implements Iterator<S> {
        /*
         * @invariant current != null
         */
        private Node<S> current;
        
        public NodeIterator(Node<S> head) {
            current = head;
        }

        public boolean hasNext() {
            return (current.next != null);
        }

        public S next() {
            if (current.next == null)
                throw new NoSuchElementException();
            current = current.next;
            return current.value;
        }

        /**
         * This opertation is not supported by this iterator.
         * 
         * @throws UnsupportedOperationException
         */
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    public final static class Node<S> {
        protected Node() {
           // NOP (just indicating the constructor has to be protected) 
        }
        
        protected Node(S value) {
            this.value = value;
        }
        
        protected Node(Node<S> next) {
            this.next = next;
        }
        
        protected Node(Node<S> next, S value) {
            this.next = next;
            this.value = value;
        }
        
        Node<S> next;
        
        S value;
        
        /**
         * Geeft de waarde terug uit deze knoop. Een niet effectieve waarde
         * wordt teruggeven als het gaat om de head van de lijst. Normaal
         * zal dit niet gebeuren, aangezien men daar niet aankan.
         */
        public S getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return '[' + ((value == null)? "HEAD" : value.toString()) + ", " + next + ']';
        }
    }
}