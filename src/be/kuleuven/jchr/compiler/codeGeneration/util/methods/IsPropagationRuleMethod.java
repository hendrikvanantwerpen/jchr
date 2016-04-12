package be.kuleuven.jchr.compiler.codeGeneration.util.methods;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.PROPAGATION;

import java.util.List;

import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

/**
 * @author Peter Van Weert
 */
public class IsPropagationRuleMethod implements TemplateMethodModelEx {
    
    private IsPropagationRuleMethod() {/* SINGLETON */}
    private static IsPropagationRuleMethod instance;
    public static IsPropagationRuleMethod getInstance() {
        if (instance == null)
            instance = new IsPropagationRuleMethod();
        return instance;
    }
    
    /**
     * @see freemarker.template.TemplateMethodModel#exec(java.compiler.util.List)
     */
    public Object exec(List args) throws TemplateModelException {
        if (args.size() != 1)
            throw new TemplateModelException("Wrong arguments (wrong number)");
        
        Object o = args.get(0);
        if (!(o instanceof TemplateHashModel))
            throw new TemplateModelException("Wrong arguments (not a hash)");
        
        final TemplateHashModel hash = (TemplateHashModel)o;
        o = hash.get("type");
        
        if (o == null || !(o instanceof TemplateNumberModel))
            throw new TemplateModelException("Wrong arguments (not a rule)");
                
        return (((TemplateNumberModel)o).getAsNumber().intValue() == PROPAGATION.ordinal())
            ? TemplateBooleanModel.TRUE
            : TemplateBooleanModel.FALSE;
    }
}