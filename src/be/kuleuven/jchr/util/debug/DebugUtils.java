package be.kuleuven.jchr.util.debug;

/**
 * @author Peter Van Weert
 */
public abstract class DebugUtils {
    
    protected DebugUtils() {/* non-instantiatable utility class */}

    /**
     * Returns a textual representation of the location where the method this
     * method is called from was called (and now: read this again).
     * 
     * @return a textual representation of the location where the method this
     *  method is called from was called.
     */
    public static String getCaller() {
        return getCaller(3);
    }
    
    /**
     * Prints a textual representation of the location where the method this
     * method is called from was called (and now: read this again)
     * to the "standard" error output stream.
     * 
     * @see System#err
     */
    public static void printCaller() {
        System.err.println(getCaller(3));
    }
    
    /**
     * Returns a textual representation of the element on the call stack going
     * back <code>goBack</code> steps (in fact: <code>goBack + 1</code> since
     * you had to call this method first!)
     * 
     * @return a textual representation of the element on the call stack going
     *  back <code>goBack</code> steps (in fact: <code>goBack + 1</code> since
     *  you had to call this method first!)
     */
    public static String getCaller(int goBack) throws ArrayIndexOutOfBoundsException {
        try {
            throw new Throwable();
        } catch (Throwable thrown){
            return thrown.getStackTrace()[goBack].toString();
        }
    }
    
    /**
     * Prints a textual representation of the element on the call stack going
     * back <code>goBack</code> steps (in fact: <code>goBack + 1</code> since
     * you had to call this method first!) to the "standard" error output stream.
     * 
     * @see System#err
     */
    public static void printCaller(int goBack) {
        System.err.println(getCaller(goBack + 1));
    }
    
    /**
     * Prints a stacktrace to the "standard" error output stream.
     * 
     * @see System#err
     */
    public static void printStackTrace() {
        try {
            throw new Throwable();
        } catch (Throwable thrown){
            System.err.println("Current stack trace is: ");
            StackTraceElement[] trace = thrown.getStackTrace();
            for (int i = 1; i < trace.length; i++)
                System.err.println("\t " + trace[i]);
        }
    }
    
    /**
     * Tries to pause the current thread for <code>millis</code> milli-seconds
     * (if it interrupts, no further attempt is made).
     */
    public static void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            // NOP
        }
    }
    
    /**
     * If it is to much work to turn on assertions, you can
     * just use this method :-)
     */
    public static void assert_(boolean b) {
         if (! b) throw new AssertionError();
    }
    /**
     * If it is to much work to turn on assertions, you can
     * just use this method :-)
     */
    public static void assert_(boolean b, String message) {
        if (! b) throw new AssertionError(message); 
    }
}
