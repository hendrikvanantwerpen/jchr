package be.kuleuven.jchr.compiler.CHRIntermediateForm.solver;

public interface ISolver {

    /**
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.ISolver#getIdentifier()
     */
    public String getIdentifier();

    public final static String JAVA_SOLVER_IDENTIFIER = "$java";
    public final static ISolver JAVA_SOLVER = new ISolver() {
        public String getIdentifier() {
            return JAVA_SOLVER_IDENTIFIER;
        }
        
        @Override
        public String toString() {
            return "Java Internal Solver (" + JAVA_SOLVER_IDENTIFIER + ")";
        }
    };
}