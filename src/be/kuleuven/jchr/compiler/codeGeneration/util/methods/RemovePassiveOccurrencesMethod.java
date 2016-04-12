package be.kuleuven.jchr.compiler.codeGeneration.util.methods;

import java.util.List;

import freemarker.template.SimpleSequence;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;

/**
 * @author Peter Van Weert
 */
public class RemovePassiveOccurrencesMethod implements TemplateMethodModelEx {
    
    private RemovePassiveOccurrencesMethod() {/* SINGLETON */}
    private static RemovePassiveOccurrencesMethod instance;
    public static RemovePassiveOccurrencesMethod getInstance() {
        if (instance == null)
            instance = new RemovePassiveOccurrencesMethod();
        return instance;
    }
    
    /**
     * @see freemarker.template.TemplateMethodModel#exec(java.compiler.util.List)
     */
    public Object exec(List args) throws TemplateModelException {
        if (args.size() != 1)
            throw new TemplateModelException("Wrong arguments (wrong number)");
        
        Object o = args.get(0);
        if (!(o instanceof TemplateSequenceModel))
            throw new TemplateModelException("Wrong argument (not a sequence)");
        
        final TemplateSequenceModel input = (TemplateSequenceModel)o;
        final SimpleSequence result = new SimpleSequence();
        TemplateHashModel hash;
        
        try {
            for (int i = 0; i < input.size(); i++) {
                hash = (TemplateHashModel)input.get(i);
                if (! ((TemplateBooleanModel)hash.get("isPassive")).getAsBoolean())
                    result.add(hash);
            }
        } catch (ClassCastException cce) {
            throw new TemplateModelException("Illegal argument", cce);
        }
        
        return result;
    }
}