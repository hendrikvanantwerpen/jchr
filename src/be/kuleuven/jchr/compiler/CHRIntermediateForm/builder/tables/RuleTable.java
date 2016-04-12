package be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.Identifier;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.DuplicateIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalIdentifierException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType;


/**
 * @author Peter Van Weert
 */
public class RuleTable extends SymbolTable<Rule> {
    public Rule declareRule(String id, RuleType type)
            throws IllegalIdentifierException, DuplicateIdentifierException {
        
        if (id == null) 
            id = createID();        
        else 
            Identifier.testUdSimpleIdentifier(id);
        
        return declare(id, Rule.create(id, getNextNbr(), type));
    }

    @Override
    public String createID() {
        return createID("rule");
    }

    /**
     * Geeft een lijst terug van de regels, gesorteerd op nummer. Dit is nodig,
     * want de onderliggende HashMap heeft geen enkele garantie op de volgorde
     * waarin de regels worden bijgehouden. Dit resulteerde in fouten waar
     * <code>Rule.getRuleNbr()</code> niet overeenstemde met de volgorde in
     * deze collectie!
     * 
     * @see be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.tables.SymbolTable#getValues()
     * @see Rule.getRuleNbr()
     */
    @Override
    public List<Rule> getValues() {
        ArrayList<Rule> result = new ArrayList<Rule>(super.getValues());
        Collections.sort(result);
        return result;
    }
}