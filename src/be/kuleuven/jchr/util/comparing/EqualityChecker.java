package be.kuleuven.jchr.util.comparing;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.EQ;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.bi.IBuiltInConstraint.EQi;
import be.kuleuven.jchr.annotations.JCHR_Asks;
import be.kuleuven.jchr.annotations.JCHR_Constraint;
import be.kuleuven.jchr.annotations.JCHR_Constraints;

@JCHR_Constraints({
    @JCHR_Constraint(
        identifier = EQ,
        arity = 2,
        infix = EQi
    )
})
public interface EqualityChecker<T> {

    /**
     * Checks whether two given objects are "equal to" eachother.
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     <code>x</code>, <code>equals(x, x)</code> should return
     *     <code>true</code>.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     <code>x</code> and <code>y</code>, <code>equals(x, y)</code>
     *     should return <code>true</code> if and only if
     *     <code>equals(y, x)</code> returns <code>true</code>.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     <code>x</code>, <code>y</code>, and <code>z</code>, if
     *     <code>equals(x, y)</code> returns <code>true</code> and
     *     <code>equals(y, z)</code> returns <code>true</code>, then
     *     <code>equals(x, z)</code> should return <code>true</code>.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     <code>x</code> and <code>y</code>, multiple invocations of
     *     <tt>equals(x, y)</tt> consistently return <code>true</code>
     *     or consistently return <code>false</code>, provided no
     *     information used in <code>equals</code> comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value <code>x</code>,
     *     <code>equals(x, null)</code> and <code>equals(null, x)</code>
     *     should return <code>false</code>.
     * </ul>
     * <p>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return  <code>true</code> if both argumenst are "equal";
     *      <code>false</code> otherwise.
     * @see     java.lang.Object#equals(java.lang.Object)
     */
    @JCHR_Asks(EQ)
    boolean equals(T o1, T o2);
    
    /**
     * Indicates whether some other object is &quot;equal to&quot; this
     * EqualityChecker.  This method must obey the general contract of
     * <tt>Object.equals(Object)</tt>.  Additionally, this method can return
     * <tt>true</tt> <i>only</i> if the specified Object is also a equality-checker
     * and it imposes the same comparison as this checker.  Thus,
     * <code>comp1.equals(comp2)</code> implies that <tt>comp1.compare(o1,
     * o2)==comp2.compare(o1, o2)</tt> for every object reference
     * <tt>o1</tt> and <tt>o2</tt>.<p>
     *
     * Note that it is <i>always</i> safe <i>not</i> to override
     * <tt>Object.equals(Object)</tt>.  However, overriding this method may,
     * in some cases, improve performance by allowing programs to determine
     * that two distinct EqualityChecker impose the same "equality".
     *
     * @param   obj   the reference object with which to compare.
     * @return  <code>true</code> only if the specified object is also
     *      a EqualityChecker and it imposes the same "equality" as this
     *      one.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.Object#hashCode()
     */
    boolean equals(Object obj);
}
