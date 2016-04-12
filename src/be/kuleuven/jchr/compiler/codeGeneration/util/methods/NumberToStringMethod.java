package be.kuleuven.jchr.compiler.codeGeneration.util.methods;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

public class NumberToStringMethod implements TemplateMethodModelEx {

    private NumberToStringMethod() {/* SINGLETON */}
    private static NumberToStringMethod instance;
    public static NumberToStringMethod getInstance() {
        if (instance == null) 
            instance = new NumberToStringMethod();
        return instance;
    }
    
    public Object exec(List args) throws TemplateModelException {
        if (args.size() != 1)
            throw new TemplateModelException("Wrong arguments (wrong number)");
        
        final Object o = args.get(0);
        if (!(o instanceof TemplateNumberModel))
            throw new TemplateModelException("Wrong argument type (not a Number)");
        
        final TemplateNumberModel number = (TemplateNumberModel)o;
        return new SimpleScalar(number.getAsNumber().toString());
    }
}
