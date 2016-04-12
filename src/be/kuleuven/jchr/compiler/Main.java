package be.kuleuven.jchr.compiler;

import java.io.File;
import java.io.FileInputStream;

import org.kohsuke.args4j.CmdLineParser;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.CHRIntermediateFormBuilder;
import be.kuleuven.jchr.compiler.analysis.Analysor;
import be.kuleuven.jchr.compiler.codeGeneration.CodeGenerator;
import be.kuleuven.jchr.compiler.parser.CHRLexer;
import be.kuleuven.jchr.compiler.parser.CHRParser;
import be.kuleuven.jchr.compiler.parser.Options;


/**
 * @author Peter Van Weert
 */
public class Main {
	public static void main(String[] args) {
		try {
            Options options = new Options();
            
            // Parse the source file and build the intermediate model
		    CHRLexer lexer = new CHRLexer(System.in);
//            CHRLexer lexer = new CHRLexer(new FileInputStream(new File("examples", "leq.jchr")));
            CHRIntermediateFormBuilder builder 
                = new CHRIntermediateFormBuilder();
		    CHRParser director = new CHRParser(lexer, builder, options);		    
		    director.construct();
            
            // Command line arguments override those specified in the source 
            CmdLineParser parser = new CmdLineParser();
            parser.addOptionClass(options);
            parser.parse(args);

            // Optimise through static analysis
            Analysor.analyse(builder.getResult(), options);
            
            // Generate the java files
		    CodeGenerator.generateAllFiles(builder.getResult());
		}
		catch (Exception x) {
			x.printStackTrace();
			System.err.println();
		}
	}
}
