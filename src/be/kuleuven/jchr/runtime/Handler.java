package be.kuleuven.jchr.runtime;

import java.util.Iterator;

import be.kuleuven.jchr.util.AbstractUnmodifiableCollection;


public abstract class Handler extends AbstractUnmodifiableCollection<Constraint> {
    /**
     * Returns an iterator over <em>all</em> constraints currently in the store.
     * The <code>Iterator.remove()</code> method is not supported by the iterator
     * that is returned. The guarantees (or lack thereof) about its behavior are completely 
     * analogous to those of iterators returning iterators over individual
     * constraints:
     * <ul>
     *  <li>
     *      There are no guarantees concerning the order in which the constraints 
     *      are returned.
     *  </li>
     *  <li>
     *      The iterators <em>might</em> fail if the constraint store is structurally modified
     *      at any time after the <code>Iterator</code> is created. In the face of concurrent modification
     *      it cannot recover from, the <code>Iterator</code> fails quickly and cleanly (throwing a
     *      <code>ConcurrentModificationException</code>), rather than risking arbitrary, 
     *      non-deterministic behavior at an undetermined time in the future.
     *      <br/>
     *      The <i>fail-fast</i> behavior of the <code>Iterator</code> is not guaranteed,
     *      even for single-threaded applications (this constraint is inherited from the 
     *      <a href="http://java.sun.com/j2se/1.5.0/docs/guide/collections/">Java Collections Framework</a>).
     *      and should only be used to detect bugs. 
     *      <br/>
     *      Important is that, while <code>Iterator</code>s returned by collections of the 
     *      Java Collections Framework generally &quot;fail fast on a best-effort basis&quot;, 
     *      this is not the case with our <code>Iterator</code>s. On the contrary: our
     *      iterators try to recover from structural changes &quot;on a best-effort basis&quot;,
     *      and fail cleanly when this is not possible (or possibly to expensive). So,
     *      in general you can get away with many updates on the constraint store during
     *      iterations (there is no way of telling which will fail though...)
     *  </li>
     *  <li>
     *      As a general note: structural changes between calls of hasNext() and next()
     *      are a bad idea: this easily leads to <code>ConcurrentModificationException</code>s.
     *  </li>
     *  <li>
     *      The failure of the <code>Iterator</code> might only occur some time after
     *      the structural modification was done: this is again because many parts
     *      of the constraint store are iterable in the presence of modification.
     *  </li>
     *  <li>
     *      When a constraint is added to the constraint store after the creation of the
     *      iterator it is possible it appears somewhere later in the iteration, but
     *      it is equally possible it does not.
     *  </li>
     *  <li>
     *      Removal of constraints on the other hand does mean the iterator will never return
     *      this constraint.
     *      Note that it still remains possible that the iterator fails somewhere after
     *      (and because of) this removal.
     *  </li>
     * </ul>
     * This lack of guarantees is intentional. Some <i>Iterator</i>s might behave perfectly 
     * in the presence of constraint store updates, whilst others do not. Some might return
     * constraints in order of their creation (and only iterate over constraints that existed
     * at the time of their creation), others do not. In fact: it is perfectly possible that 
     * their behavior changes between two compilations (certainly when moving to a new version
     * of the compiler). This is the price (and at the same time the bless) of declarative 
     * programming: it is the compiler that chooses the data structures that seem optimal 
     * to him at the time! 
     *
     * @return An iterator over <em>all</em> constraints currently in the store.
     */
    public abstract Iterator<Constraint> lookup();
    
    /**
     * This method is the equivalent of the <code>lookup</code>-method,
     * allowing a handler to be the target of the "foreach" statement.
     * We refer to the former method for more information about the
     * behavior of the <code>Iterator</code> that is returned.
     * 
     * @return An iterator over <em>all</em> constraints currently in the store.
     * 
     * @see #lookup()
     */
    @Override
    public abstract Iterator<Constraint> iterator();
    
// (maybe, someday)
//    /**
//     * Checks whether this handler is in a consistent state, meaning there
//     * are <em>no</em> dead constraints left in the constraint store,
//     * constraintlists iterate their values in the correct order (from new to
//     * old), etc
//     * 
//     * @return true (we hope)
//     */
//    public abstract boolean isValid();
    
    /*                                             *\
       Some simple key types used for hash indices 
    \*                                             */
    
    protected static interface Key {/* empty interface */}
    protected abstract static class StorageKey implements Key {
        public abstract void removeSelf();
    }
    /*
     * The fact that this class has to be public is unfortunate
     */
    public abstract static class MutableStorageKey 
        extends StorageKey {
        
        @Override
        public String toString() {
            return Integer.toHexString(hashCode());
        }
        
        /**
         * Rehashing means recalculating the keys hash-value and making sure
         * it remains in a correct hash-bucket.
         * 
         * @return true if and only if the key is still used after rehashing.
         *  A key will no longer be used if:
         *  <ul>
         *      <li>There are currently no constraints behind it anymore.</li>
         *      <li>
         *          After rehashing (and reinsertion probably) an equal key
         *          (not identical of course, only when we're very unlucky...)
         *          was already present and the current one is not used.
         *      </li>
         *  </ul>
         */
        public abstract boolean rehash();
    }
    protected interface LookupKey extends Key {/* again, an empty interface */}
}