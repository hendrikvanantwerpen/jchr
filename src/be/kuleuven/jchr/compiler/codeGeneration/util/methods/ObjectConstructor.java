package be.kuleuven.jchr.compiler.codeGeneration.util.methods;

import java.util.Collections;
import java.util.List;

import freemarker.template.TemplateModelException;

/**
 * What the heck else would be constructing besides objects? A silly
 * name for sure, taken over from the FreeMarker-people. This version 
 * uses a predefined class for the objects that it constructs, rather
 * then arguments.
 * 
 * @author Peter Van Weert
 */
public class ObjectConstructor extends freemarker.template.utility.ObjectConstructor {
    private String className;
    
    /**
     * Constructs an <code>freemarker.template.utility.ObjectConstructor</code>
     * that constructs objects from the given class.
     * 
     * @param theClass
     *  The class of the objects that this FreeMarker method constructs.
     * 
     * @see freemarker.template.utility.ObjectConstructor#ObjectConstructor()
     */
    public ObjectConstructor(Class theClass) {
        this(theClass.getName());
    }

    /**
     * Constructs an <code>freemarker.template.utility.ObjectConstructor</code>
     * that constructs objects from the given class (in concreto: the fully
     * qualified name is supplied in this constructor).
     * 
     * @param className
     *  The fully qualified name of the class 
     *  of the objects that this FreeMarker method should construct.
     *  
     * @see freemarker.template.utility.ObjectConstructor#ObjectConstructor()
     */
    public ObjectConstructor(String className) {
        setClassName(className);
    }
    
    /**
     * @inheritDoc
     * 
     * @throws TemplateModelException
     *  If arguments are supplied when calling this FreeMarker method.
     */
    @Override
    public Object exec(List args) throws TemplateModelException {
        if (!args.isEmpty())
            throw new TemplateModelException("This method shouldn't have any arguments");

        return super.exec(Collections.singletonList(getClassName()));
    }
    
    /**
     * Returns the fully qualified name of the class of which 
     * the objects that are constructed when calling this FreeMarker
     * method.
     * 
     * @return Returns the the fully qualified name of the class of which 
     *  the objects that are constructed when calling this FreeMarker
     *  method.
     */
    protected String getClassName() {
        return className;
    }
    
    /**
     * Sets the (fully qualified) name of the class of which 
     * the objects that are constructed when calling this FreeMarker
     * method.
     * 
     * @param theClassName 
     *  The (fully qualified) name of the class of which 
     *  the objects that are constructed when calling this FreeMarker
     *  method.
     */
    protected void setClassName(String theClassName) {
        this.className = theClassName;
    }
}