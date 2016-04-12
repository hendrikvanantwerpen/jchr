package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;

/**
 * @author Peter Van Weert
 */
public class SolverTable extends SymbolTable<Solver> {
    public String declareSolver(Solver solver)
            throws DuplicateIdentifierException {

        if (!solver.hasIdentifier())
            solver.setIdentifier(createID());

        declare(solver.getIdentifier(), solver);
        return solver.getIdentifier();
    }

    @Override
    public String createID() {
        return createID("solver");
    }
}
