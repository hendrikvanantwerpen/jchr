package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IBasicArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.AssignmentConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java.JavaConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.UserDefindedConjunct;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.AbstractMethodInvocation;
import be.kuleuven.jchr.util.builder.BuilderException;


public class ConjunctDataModelDirector<B extends IArgumentedDataModelBuilder<?>> 
extends ArgumentedDataModelDirector<B, IBasicArgumented> {
    
    public ConjunctDataModelDirector(B builder, IBasicArgumented conjunct) {
        super(builder, conjunct);
    }

    @Override
    protected void construct2() throws BuilderException {
        final IBasicArgumented conjunct = getArgumented();
        
        if (conjunct instanceof UserDefindedConjunct)
            getBuilder().beginUserDefinedConjunct(((UserDefindedConjunct)conjunct).getIdentifier());
        else if (conjunct instanceof AbstractMethodInvocation)
            getBuilder().beginMethodInvocation(((AbstractMethodInvocation<?>)conjunct).getMethodName());
        else if (conjunct instanceof AssignmentConjunct && 
                ((AssignmentConjunct)conjunct).isDeclaration())
            getBuilder().beginDeclaration(((AssignmentConjunct)conjunct).getTypeString());
        else if (conjunct instanceof JavaConjunct)
            getBuilder().beginInfixConjunct(((JavaConjunct)conjunct).getIdentifier());
        else throw new BuilderException("Unsupported occurrencetype: " + conjunct.getClass());
            
        constructArguments(conjunct);
        getBuilder().endArgumented();
    }
}
