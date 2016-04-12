package be.kuleuven.jchr.runtime.list;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import be.kuleuven.jchr.util.iterator.IteratorUtilities;


/**
 * Een lijst waarover niet-failfast iteratoren kunnen lopen zonder gevaar.
 * Er wordt enkel vooraan toegevoegd, en verwijderen gebeurt door het
 * loskoppelen van de interne <code>Node</code>s, zodat iteratoren
 * niet verstoord geraken.
 * 
 * @author Peter Van Weert
 */
public class DoubleLinkedList<T> implements Iterable<T> {
    public DoubleLinkedList() {
        // NOP
    }
    
    public <S extends T> DoubleLinkedList(S... values) {
        for (int i = values.length-1; i >= 0; i--)
            addFirst(values[i]);
    }
    
    /**
     * De head van deze lijst.
     * @invar head != null
     */
    protected Node<T> head = new Node<T>();
    
    /**
     * Voegt een element <code>x</code> vooraan toe aan de lijst. Hierdoor
     * hebben iteratoren die over de lijst aan het itereren zijn er geen last
     * van. Het geeft de <code>Node</code> terug waarin de waarde steekt,
     * waarmee het element ten allen tijde uit de lijst kan gehaald worden 
     * zonder te moeten zoeken.
     * 
     * @pre canAdd(x)
     * 
     * @param x
     *  Het toe te voegen element.
     * @return De <code>Node</code> in de lijst waarin <code>x</code> steekt. 
     *  Diens <code>remove()</code> verwijdert met andere woorden dit element.
     */
    public Node<T> addAndReturnNode(T x) {
        final Node<T> result = head; 
        result.value = x;
        head = new Node<T>(result);
        return result;
    }
    
    /**
     * Voegt een element <code>x</code> vooraan toe aan de lijst. Hierdoor
     * hebben iteratoren die over de lijst aan het itereren zijn er geen last
     * van. Ieder element zal telkens maar 1 keer in de lijst zitten.
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
     * Checks whether this list is a valid <code>DoubleLinkedList</code>: i.e.
     * all next and previous pointers are consistent.
     *  
     * @return True if this list is a valid <code>DoubleLinkedList</code>;
     *  false otherwise.
     */
    public boolean isValid() {
        Node<T> current = head;
        while (current.next != null) {
            current = current.next;
            if (current.previous == null || current.previous.next != current)
                return false;
        }
        return true;
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
     * Geeft het aantal elementen terug uit deze lijst.
     */
    public int size() {
        int result = 0;
        for (Node n = head.next; n != null; n = n.next) result++;
        return result;
    }

    /**
     * Geeft een tekstuele representatie weer van deze lijst. 
     */
    @Override
    public String toString() {
        return IteratorUtilities.toString(this);
    }

    /**
     * An iterator that is not fail-fast, meaning structural changes to the
     * list may occur during its iteration. The only time a
     * <code>ConcurrentModificationException</code> <em>can</em> get thrown is when
     * there is a modification between the call to <code>hasNext()</code>
     * and <code>next()</code>.
     * 
     * @author Peter Van Weert
     */
    protected final static class NodeIterator<S> implements Iterator<S> {
        /*
         * @invariant (hasNext ^ (next == null)) 
         */
        private Node<S> next;
        
        private boolean hasNext;
        
        protected NodeIterator() {
            // NOP
        }
        
        NodeIterator(Node<S> head) {
            next = head;
        }

        public boolean hasNext() {
            // Needed when some ogre asks hasNext() several times in a row
            // (that is: before calling the next() method)
            if (hasNext) return true;
            
            try {
                // Isn't this nice Java code!!!
                while ((hasNext = ((next = next.next) != null)) && next.removed);
                return hasNext;
                
                /*
                 * The following code does the same in a more legible but slower way
                 *      next = next.next;
                 *      hasNext = (next != null); 
                 *      if (hasNext && next.removed) return hasNext();
                 *      return hasNext;
                 */
                
            } catch (NullPointerException npe) { 
                // next == null
                // (can occur when hasNext() returns false, but you still
                //  decide to call next(), or you call hasNext() again.
                //  Both can safely be regarded exceptional cases!)
                return false;
            }
        }

        /**
         * @throws ConcurrentModificationException
         *  Even though this iterator recovers from structural changes to the 
         *  underlying list on a best-effort basis (i.e. it is <em>not</em>
         *  fail-fast), it can fail if a removal occurs between the previous
         *  call to {@link #hasNext()} (&quot;previous&quot; is actually not
         *  precise enough: calling the <code>hasNext()</code> more then once 
         *  has no effect: it is the first call ever or after a call to 
         *  <code>next()</code> that counts). Note that even in this class
         *  of removals an unrecoverable removal is an exceptional case! 
         */
        public S next() throws ConcurrentModificationException, NoSuchElementException {
            if (hasNext || hasNext()) {
                // Could occur when there are modifications between the call
                // to hasNext() and next()
                if (next.removed) throw new ConcurrentModificationException();
                hasNext = false;
                return next.value;
            }
            // Can occur when you call this method when there is no next at all
            throw new NoSuchElementException();
        }

        /**
         * This operation is not supported by this iterator.
         * 
         * @throws UnsupportedOperationException
         */
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    public static class Node<S> {
        protected Node() {
            // NOP (just indicating it should be a protected constructor)
        }
        
        protected Node(Node<S> next) {
            this.next = next;
            next.previous = this;
        }
        
        protected Node(S value) {
            this.value = value;
        }
        
        protected Node(S value, Node<S> previous) {
            this.previous = previous;
            this.value = value;
        }
        
        protected Node(S value, Node<S> previous, Node<S> next) {
            this.value = value;
            this.next = next;
            this.previous = previous;
        }
        
        protected Node<S> next;
        
        protected Node<S> previous;
        
        protected boolean removed;
        
        protected S value;
        
        /**
         * Verwijdert deze knoop uit de lijst. Merk op dat de 
         * <code>Node</code> met het element niet vernietigd wordt, 
         * maar enkel losgekoppeld om iteratoren niet te verstoren.
         */
        public void removeSelf() {
            previous.next = next;
            if (next != null) next.previous = previous;
            removed = true;
        }
        
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