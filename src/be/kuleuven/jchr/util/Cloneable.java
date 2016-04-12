package be.kuleuven.jchr.util;

/**
 * A class implements the {@link java.lang.Cloneable} interface to 
 * indicate to the {@link java.lang.Object#clone()} method that it 
 * is legal for that method to make a field-for-field copy of instances 
 * of that class. 
 * <p>
 * Invoking Object's clone method on an instance that does not implement the 
 * {@link java.lang.Cloneable} interface results in the exception 
 * <code>CloneNotSupportedException</code> being thrown.
 * <p>
 * "By convention", classes that implement {@link java.lang.Cloneable}
 * should override <tt>Object.clone</tt> (which is protected) with a 
 * public method. See {@link java.lang.Object#clone()} for details 
 * on overriding this method. 
 * <p>
 * This interface extends the {@link java.lang.Cloneable} and forces
 * classes that implement it to also implement a suited, public 
 * clone()-method. So this is no longer "a convention", but it is
 * mandatory.
 */
public interface Cloneable<T> extends java.lang.Cloneable {

    /**
     * @see java.lang.Object#clone()
     */
    public T clone();
//    public <S extends T> S clone();
}
