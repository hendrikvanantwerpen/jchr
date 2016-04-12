package be.kuleuven.jchr.util.comparing;

public final class DefaultEqualityChecker<T> implements EqualityChecker<T> {

    private DefaultEqualityChecker() { /* SINGLETON */ }
    private static DefaultEqualityChecker instance;    
    /*
     * Since this is a singleton, it cannot be type safe!
     */
    @SuppressWarnings("unchecked")
    public static <T> DefaultEqualityChecker<T> getInstance() {
        if (instance == null)
            instance = new DefaultEqualityChecker();
        return instance;
    }
    
    public boolean equals(T o1, T o2) {
        return o1.equals(o2);
    }
}
