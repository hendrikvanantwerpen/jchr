package be.kuleuven.jchr.compiler.codeGeneration;

import static be.kuleuven.jchr.compiler.codeGeneration.TupleArities.getAllTupleArities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.ICHRIntermediateForm;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefinedConstraint;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.DataModel;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.DataModelBuilderFacade;
import be.kuleuven.jchr.compiler.codeGeneration.util.methods.NumberToStringMethod;
import be.kuleuven.jchr.compiler.codeGeneration.util.methods.RemovePassiveOccurrencesMethod;
import be.kuleuven.jchr.compiler.codeGeneration.util.methods.SysoutMethod;
import be.kuleuven.jchr.compiler.codeGeneration.util.transforms.NilTransform;
import be.kuleuven.jchr.util.builder.BuilderException;



import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.utility.StringUtil;

/**
 * @author Peter Van Weert
 */
public abstract class CodeGenerator {
    public final static String 
        CONSTRAINT_FILE_TEMPLATE = "Constraint.ftl",
        HANDLER_TEMPLATE = "Handler.ftl",
        TUPLE_TEMPLATE = "Tuple.ftl",
        GLOBAL_TEMPLATE = "GlobalMacros.ftl",
        TEMPLATES_DIR = "templates";
    
    private final static Configuration configuration;
    protected static Configuration getConfiguration() {
        return configuration;
    }

    static {
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(CodeGenerator.class, TEMPLATES_DIR);
        
        configuration.setSharedVariable("sysout", SysoutMethod.getInstance());
        configuration.setSharedVariable("nil", NilTransform.getInstance());
        configuration.setSharedVariable("num2str", NumberToStringMethod.getInstance());
        configuration.setSharedVariable("removePassiveOccurrences", RemovePassiveOccurrencesMethod.getInstance());
        
        configuration.addAutoImport("global", GLOBAL_TEMPLATE);
    }

    protected static DataModel buildDataModel(ICHRIntermediateForm intermediateForm) throws GenerationException {
        try {
            return DataModelBuilderFacade.buildDataModel(intermediateForm);
        } catch (BuilderException e) {
            throw new GenerationException(e);
        }
    }

    public static void generateAllFiles(ICHRIntermediateForm intermediateForm) throws GenerationException, IOException {
        final File outputDir = getHandlerOutputDirectory(intermediateForm);
        final DataModel model = buildDataModel(intermediateForm);
        generateConstraintFiles(intermediateForm, model, outputDir);
        generateHandlerFile(intermediateForm, model, outputDir);
        generateTupleFiles(intermediateForm);
    }

    public static void generateConstraintFiles(ICHRIntermediateForm intermediateForm) throws GenerationException, IOException {
        generateConstraintFiles(intermediateForm, buildDataModel(intermediateForm), getHandlerOutputDirectory(intermediateForm));
    }
    
    public static void generateConstraintFile(ICHRIntermediateForm intermediateForm, UserDefinedConstraint current) throws GenerationException, IOException {
        generateConstraintFile(current, buildDataModel(intermediateForm), getHandlerOutputDirectory(intermediateForm));
    }
    
    protected static void generateConstraintFiles(ICHRIntermediateForm intermediateForm, DataModel model, File outputDir)
        throws GenerationException, IOException {
        
        for (UserDefinedConstraint current : intermediateForm.getUserDefinedConstraints())
            generateConstraintFile(current, model, outputDir);
    }

    protected static void generateConstraintFile(UserDefinedConstraint current, DataModel model, File outputDir)
    throws GenerationException, IOException {
        Writer writer = null;
        try {
            DataModelBuilderFacade.setCurrentConstraint(current, model);

            final Template template;
            try {
                template = getConfiguration().getTemplate(CONSTRAINT_FILE_TEMPLATE);
            } catch (IOException ioe) {
                throw new GenerationException("...during " + current, ioe);
            }

            final File file = new File(outputDir, getConstraintFileName(current.getIdentifier()));             
            writer = new FileWriter(file);
            // final Writer writer = new OutputStreamWriter(System.out);
            
            System.out.println("Generating file: " +  file);

            template.process(model.getRoot(), writer);
        } catch (BuilderException be) {
            throw new GenerationException("...during " + current, be);
        } catch (TemplateException te) {
            throw new GenerationException("...during " + current, te);
        } finally {
            if (writer != null) writer.close();
        }
    }
    
    public static void generateTupleFiles(ICHRIntermediateForm intermediateForm) 
    throws GenerationException,IOException {
        final File outputDir = getUdOutputDirectory(intermediateForm);
        
        for (Integer arity : getAllTupleArities(intermediateForm))
            generateTupleFile(arity.intValue(), outputDir, intermediateForm.getChrPackage() );
    }
    
    public static void generateTupleFile(int arity,ICHRIntermediateForm intermediateForm)
    throws GenerationException,IOException {
        generateTupleFile(arity, getUdOutputDirectory(intermediateForm), intermediateForm.getChrPackage() );
    }
    
    protected static void generateTupleFile(int arity, File outputDir, String pkg )
    throws GenerationException,IOException {
        
        final DataModel model;
        final Template template;
        try {
            model = DataModelBuilderFacade.buildTupleDataModel(arity,pkg);
            template = getConfiguration().getTemplate(TUPLE_TEMPLATE);
        } catch (IOException ioe) {
            throw new GenerationException("...during tuple with arity" + arity, ioe);
        } catch (BuilderException be) {
            throw new GenerationException("...during tuple with arity" + arity, be);
        }

         final File file = new File(outputDir, getTupleFileName(arity)); 
         final Writer writer = new FileWriter(file);
        // final Writer writer = new OutputStreamWriter(System.out);

         System.out.println("Generating file: " +  file);
                 try {
            template.process(model.getRoot(), writer);
        } catch (TemplateException te) {
            throw new GenerationException("...during tuple with arity" + arity, te);
        } finally {
            writer.close();
        }
    }

    public static String getConstraintFileName(String identifier) {
        StringBuilder builder = new StringBuilder();
        final char first = identifier.charAt(0);
        builder.append(Character.toUpperCase(first));
        builder.append(identifier.substring(1));
        builder.append("Constraint.java");
        return builder.toString();
    }
    
    public static String getTupleFileName(int arity) {
        return new StringBuilder("Tuple").append(arity).append(".java").toString();
    }

    public static void generateHandlerFile(ICHRIntermediateForm intermediateForm, DataModel model)
        throws GenerationException, IOException {
        
        generateHandlerFile(intermediateForm, model, getHandlerOutputDirectory(intermediateForm));
    }
    
    protected static void generateHandlerFile(ICHRIntermediateForm intermediateForm, DataModel model, File outputDir)
            throws GenerationException, IOException {
        
        final Template template;
        try {
            template = getConfiguration().getTemplate(HANDLER_TEMPLATE);
        } catch (IOException ioe) {
            throw new GenerationException("...during generation of handler", ioe);
        }

        final File file = new File(outputDir, getHandlerFileName(intermediateForm));
        final Writer writer = new FileWriter(file);

        try {
            System.out.println("Generating file: " +  file);
            template.process(model.getRoot(), writer);
            
        } catch (TemplateException te) {
            throw new GenerationException("...during generation of handler", te);
        } finally {
            writer.close();
        }
    }

    public static String getHandlerFileName(ICHRIntermediateForm intermediateForm) {
        final String identifier = intermediateForm.getHandlerName();
        final StringBuilder builder = new StringBuilder();        
        for (String s : StringUtil.split(identifier, '_')) {            
            builder.append(Character.toUpperCase(s.charAt(0)));
            builder.append(s.substring(1));
        }
        builder.append("Handler.java");
        return builder.toString();
    }
    
    public static File getHandlerOutputDirectory(ICHRIntermediateForm intermediateForm) throws GenerationException {
        final File result = new File( pkg2dir(intermediateForm) + "/" + intermediateForm.getHandlerName());
        if (result.exists() && !result.isDirectory())
            throw new GenerationException("A file " + result + " exists!");
        if (!result.exists() && !result.mkdirs())
            throw new GenerationException("Couldn't create directory " + result);
        return result;
    }
    
    public static String pkg2dir( ICHRIntermediateForm intermediateForm ){
    	return "./" + intermediateForm.getChrPackage().replace( '.', '/' );
    }
    public static File getUdOutputDirectory(ICHRIntermediateForm intermediateForm) throws GenerationException {
        final File result = new File( pkg2dir(intermediateForm) );
        if (result.exists() && !result.isDirectory())
            throw new GenerationException("A file " + result + " exists!");
        return result;
    }
}