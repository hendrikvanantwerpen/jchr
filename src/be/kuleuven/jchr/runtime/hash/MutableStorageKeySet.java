package be.kuleuven.jchr.runtime.hash;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;

import be.kuleuven.jchr.runtime.Handler.MutableStorageKey;
import be.kuleuven.jchr.util.Cloneable;


/*
 * De extends werkt perfect, maar is niet echt de bedoeling. 
 * Vele operaties zijn wel nog steeds inefficient!!
 */
public class MutableStorageKeySet 
    extends AbstractSet<MutableStorageKey>
    implements Cloneable<MutableStorageKeySet> {
    
    private final static Object PRESENT = new Object();
    
    private HashMap<Wrapper, Object> map;
    
    public MutableStorageKeySet(MutableStorageKey key) {
        (map = new HashMap<Wrapper, Object>()).put(new Wrapper(key), PRESENT);
    }
    
    public MutableStorageKeySet() {
        map = new HashMap<Wrapper, Object>();
    }
    
    public MutableStorageKeySet(int capacity) {
        map = new HashMap<Wrapper, Object>(capacity);
    }
    
    protected MutableStorageKeySet(HashMap<Wrapper, Object> map) {
        this.map = map;
    }
    
    @Override
    public boolean add(MutableStorageKey key) {
        return (map.put(new Wrapper(key), PRESENT) == null);
    }
    
    public void remove(MutableStorageKey key) {
        map.remove(new Wrapper(key));
    }
    
    @Override
    public Iterator<MutableStorageKey> iterator() {
        return new WrapperIterator(map.keySet().iterator());
    }    
    protected static class WrapperIterator implements Iterator<MutableStorageKey> {
        public WrapperIterator(Iterator<Wrapper> wrapped) {
            this.wrapped = wrapped;
        }
        
        private Iterator<Wrapper> wrapped;
        
        public boolean hasNext() {
            return wrapped.hasNext();
        }
        
        public MutableStorageKey next() {
            return wrapped.next().key;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    
    public void reset() {
        map.clear();
    }
    
    public void rehashAll() {
        final Iterator<Wrapper> iterator = map.keySet().iterator();
        while (iterator.hasNext()) 
            if (! iterator.next().key.rehash()) iterator.remove();
    }
    
    /**
     * Mengt deze verzameling met de sleutels uit <code>other</code>. 
     * De sleutels waarvan de hash gewijzigd is moeten worden geherpositioneerd 
     * in de hash-map. Hierbij kan het gebeuren dat de oude sleutel en 
     * dus de oude observers overbodig geworden zijn.
     * Merk op dat gegeven de volgende invariant:
     * <div>
     * <pre>
     *  eq(var1, var2) 
     *      ==> hash(var1) == hash(var2) 
     *      ==> hashobserverlist(var1) == hashobserverlist(var2)
     * </pre>
     * </div>
     * geldt dat dit de enige observers zijn die overbodig zijn nu. Dit is
     * een sterkere veronderstelling dan de eerste implicatie (die uiteraard
     * noodzakelijk is), maar is best aanvaardbaar: het tegendeel zou
     * ineffici&euml;nt zijn.
     * 
     * @pre The hash-values of the keys in <em>this</em> set have <em>not</em>
     *  changed: i.e. no rehashing has to be done here!
     */
    @SuppressWarnings("unchecked")
    public void mergeWith(MutableStorageKeySet other) {
        // volgende laat ons later eenvoudig toe om trailing te doen
        // (bovendien zijn de iteratoren fail-fast, en dus moeten we wel ;-) ) 
        map = (HashMap<Wrapper, Object>)map.clone();
        
        for (Wrapper wrapper : other.map.keySet()) 
            if (wrapper.key.rehash())//) && !map.containsKey(wrapper)) 
                map.put(wrapper, PRESENT);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public MutableStorageKeySet clone() {
        return new MutableStorageKeySet((HashMap<Wrapper, Object>)map.clone());
    }
    
    @Override
    public String toString() {
        return map.toString();
    }
    
    /*
     * We need a wrapper here because the equals-method should be based on
     * identity here (keys can come from different hash-maps, i.e. have
     * different types, and there is no type-checking in the equals methods)...
     */
    protected static class Wrapper implements Cloneable<Wrapper> {
        protected MutableStorageKey key;
        
        public Wrapper(MutableStorageKey key) {
            this.key = key;
        }
        
        @Override
        public boolean equals(Object other) {
            // THIS HAS TO BE AN IDENTITY COMPARISON (cf supra)
            return this.key == ((Wrapper)other).key;
        }
        
        @Override
        public int hashCode() {
            return key.hashCode();
        }
        
        @Override
        public String toString() {
            return key.toString();
        }
        
        @Override
        public Wrapper clone() {
            try {
                return (Wrapper)super.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError();
            }
        }
    }

    @Override
    public int size() {        
        return map.size();
    }
}
