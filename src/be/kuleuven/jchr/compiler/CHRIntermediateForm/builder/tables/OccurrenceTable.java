package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.Identifier;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalIdentifierException;

public class OccurrenceTable extends SymbolTable<Occurrence>{
    
    public void identify(String identifier, Occurrence occurrence)
        throws DuplicateIdentifierException, IllegalIdentifierException {

        Identifier.testUdSimpleIdentifier(identifier, true);
        if (getValues().contains(occurrence))
            throw new DuplicateIdentifierException(
                "The occurrence " + occurrence + " has already received an identifier!"
            );
        declare(identifier, occurrence);
    }
}
