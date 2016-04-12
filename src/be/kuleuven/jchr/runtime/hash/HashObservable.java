package be.kuleuven.jchr.runtime.hash;

import be.kuleuven.jchr.runtime.Handler.MutableStorageKey;


public interface HashObservable {

    void addHashObserver(MutableStorageKey observer);
    
    MutableStorageKeySet getHashObservers();
    
    /**
     * @see HashObserverSet#rehashAll()
     * @see HashObserver#rehash()
     */
    void rehashAll();
    
    /**
     * Merges the hash-observers of the implementing object with those given
     * in <code>set</code>. 
     * During this merge rehashing of the keys in <code>set</code> gets done.
     * Rehashing of our own keys has to be done elsewhere if necessary.
     * 
     * @pre others != null
     *  
     * @param set
     *  The set of <code>MutableStorageKey</code>s to be merged into our 
     *  own set.
     *
     * @see be.kuleuven.jchr.runtime.Handler.MutableStorageKey#rehash()
     */
    void mergeHashObservers(MutableStorageKeySet set);
    
}