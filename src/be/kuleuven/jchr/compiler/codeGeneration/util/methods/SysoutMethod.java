package be.kuleuven.jchr.compiler.codeGeneration.util.methods;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * A simple FreeMarker method that can be used to print out one or more things
 * to the standard out stream.
 *  
 * @author Peter Van Weert
 */
public class SysoutMethod implements TemplateMethodModelEx {

    private SysoutMethod() {/* SINGLETON */}
    private static SysoutMethod instance;
    public static SysoutMethod getInstance() {
        if (instance == null)
            instance = new SysoutMethod();
        return instance;
    }

    /**
     * @see freemarker.template.TemplateMethodModel#exec(java.compiler.util.List)
     */
    public Object exec(List args) throws TemplateModelException {
        if (args.size() != 1)
            throw new TemplateModelException("Wrong arguments (wrong number)");

        final Object arg = args.get(0);

        if (arg instanceof TemplateBooleanModel)
            System.out.println(arg == TemplateBooleanModel.TRUE);
        else
            System.out.println(args.get(0));

        return new SimpleScalar("");
    }
}