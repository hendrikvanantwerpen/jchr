package be.kuleuven.jchr.util.comparing;

import java.util.Comparator;

public abstract class ComparatorEqualityChecker<T> implements Comparator<T>, EqualityChecker<T> {

    public boolean equals(T o1, T o2) {
        return compare(o1, o2) == 0;
    }
}