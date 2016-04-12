package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.argumented;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ClassNameImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.BooleanArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.ByteArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.CharArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.DoubleArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.FloatArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.IntArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.LongArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.ShortArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.constant.StringArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IBasicArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.AbstractMethodInvocation;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.ConstructorInvocation;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.FieldAccess;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.director.DataModelDirector;
import be.kuleuven.jchr.util.builder.BuilderException;


public abstract class ArgumentedDataModelDirector<
    B extends IArgumentedDataModelBuilder<?>, 
    A extends IBasicArgumented
>
    extends DataModelDirector<B> {

    private A argumented;
    
    public ArgumentedDataModelDirector(B builder, A argumented) {
        super(builder);
        setArgumented(argumented);
    }
    
    protected A getArgumented() {
        return argumented;
    }
    protected void setArgumented(A occurrence) {
        this.argumented = occurrence;
    }
    
    protected void constructArguments() throws BuilderException {
        constructArguments(getArgumented());
    }
    
    protected void constructArguments(IBasicArgumented argumented) throws BuilderException {
        final IArguments arguments = argumented.getArguments();
        final int arity = arguments.getArity();
        
        getBuilder().beginArguments(arity);
        
        for (IArgument argument : arguments) {
            getBuilder().beginArgument();
            
            switch (argument.getArgumentType()) {
                case BOOLEAN:                   
                    getBuilder().buildBooleanArgument(((BooleanArgument)argument).getValue());
                break;
                
                case BYTE:
                    getBuilder().buildByteArgument(((ByteArgument)argument).getValue());
                break;
                
                case SHORT:
                    getBuilder().buildShortArgument(((ShortArgument)argument).getValue());
                break;
                
                case INT:
                    getBuilder().buildIntArgument(((IntArgument)argument).getValue());
                break;
                
                case LONG:
                    getBuilder().buildLongArgument(((LongArgument)argument).getValue());
                break;
                
                case FLOAT:
                    getBuilder().buildFloatArgument(((FloatArgument)argument).getValue());
                break;
                
                case DOUBLE:
                    getBuilder().buildDoubleArgument(((DoubleArgument)argument).getValue());
                break;
                
                case CHAR:
                    getBuilder().buildCharArgument(((CharArgument)argument).getValue());
                break;
                
                case ONE_DUMMY:
                    getBuilder().buildOneDummyArgument();
                break;
                
                case OTHER_DUMMY:
                    getBuilder().buildOtherDummyArgument();
                break;
                
                case STRING:
                    getBuilder().buildStringArgument(((StringArgument)argument).getValue());
                break;
                
                case VARIABLE:
                    final Variable variable = (Variable)argument;
                    getBuilder().buildVariableArgument(
                        variable.getIdentifier(),
                        variable.isAnonymous()
                    );
                break;
                
                case NULL:
                    getBuilder().buildNullArgument();
                break;
                
                case METHOD_INVOCATION:
                    getBuilder().beginMethodInvocationArgument(((AbstractMethodInvocation<?>)argument).getMethodName());
                    constructArguments((AbstractMethodInvocation)argument);
                    getBuilder().endMethodInvocationArgument();
                break;
                
                case CONSTRUCTOR_INVOCATION:
                    getBuilder().beginConstructorInvocationArgument(((ConstructorInvocation)argument).getTypeString());
                    constructArguments((ConstructorInvocation)argument);
                    getBuilder().endConstructorInvocationArgument();
                break;
                
                case SOLVER:
                    getBuilder().buildSolverArgument(((Solver)argument).getIdentifier());
                break;
                
                case FIELD_ACCESS:
                    getBuilder().beginFieldAccessArgument(((FieldAccess)argument).getName());
                    constructArguments((FieldAccess)argument);
                    getBuilder().endFieldAccessArgument();
                break;
                
                case CLASS_NAME:
                    getBuilder().buildClassNameArgument(((ClassNameImplicitArgument)argument).getName());
                break;
            }
            
            getBuilder().endArgument();
        }
        
        getBuilder().endArguments();
    }
}