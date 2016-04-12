package be.kuleuven.jchr.compiler.codeGeneration.util.transforms;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import be.kuleuven.jchr.util.writers.NilWriter;

import freemarker.template.TemplateModelException;
import freemarker.template.TemplateTransformModel;

/**
 * This singleton class offers the FreeMarker transfor that 
 * simply ignores the input it is given 
 * (i.e. it transforms each given scalar to <i>nil</i>, 
 * the empty scalar).
 * 
 * @author Peter Van Weert
 */
public class NilTransform implements TemplateTransformModel {
    
    private static NilTransform instance;
    private NilTransform() {/* SINGLETON */}
    /**
     * Returns the FreeMarker transformation that simply ignores the
     * input it is given (i.e. it transforms each given scalar
     * to <i>nil</i>, the empty scalar).
     * 
     * @return The FreeMarker transfor that simply ignores the
     *  input it is given (i.e. it transforms each given scalar
     *  to <i>nil</i>, the empty scalar).
     */
    public static NilTransform getInstance() {
        if (instance == null) 
            instance = new NilTransform();
        return instance;
    }
    
    /**
     * @inheritDoc
     */
    public Writer getWriter(final Writer out, Map args) throws TemplateModelException, IOException {
        if (! args.isEmpty()) 
            throw new TemplateModelException("The nil transform has no arguments");
        
        return new NilWriter();
    }
}