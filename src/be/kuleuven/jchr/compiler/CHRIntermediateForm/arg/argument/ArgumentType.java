package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument;

/**
 * @author Peter Van Weert
 */
public enum ArgumentType {
    BOOLEAN, 
    BYTE, SHORT, INT, LONG, FLOAT, DOUBLE,
    CHAR,
    STRING,
    VARIABLE, SOLVER,
    NULL,
    CONSTRUCTOR_INVOCATION, METHOD_INVOCATION, FIELD_ACCESS,
    CLASS_NAME,
    ONE_DUMMY, OTHER_DUMMY
}
