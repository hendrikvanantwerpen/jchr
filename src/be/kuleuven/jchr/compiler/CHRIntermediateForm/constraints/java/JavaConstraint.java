package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.java;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.Argumentable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.util.IndexOutOfBoundsException;

/**
 * @author Peter Van Weert
 */
public class JavaConstraint
    extends Argumentable<JavaConstraint>
    implements IJavaConstraint<JavaConstraint> {
    
    private IType argumentType;
    
    private String identifier;
    
    private boolean askConstraint;
    
    protected JavaConstraint() {
        // NOP
    }
    
    public JavaConstraint(IType argumentType, String infix, boolean askConstraint) {
        super();
        init(argumentType, infix, askConstraint);
    }
    
    protected void init(IType argumentType, String infix, boolean askConstraint) {
        setArgumentType(argumentType);
        setIdentifier(infix);
        setAskConstraint(askConstraint);
    }
    
    @Override
    public IType[] getFormalParameters() {
        return new IType[] {getArgumentType(), getArgumentType()};
    }
    
    public IType getFormalParameterAt(int index) {
        if (index < 0 || index > 2) 
            throw new IndexOutOfBoundsException(index, 2);
        return getArgumentType();
    }
    public int getArity() {
        return 2;
    }
    
    protected IType getArgumentType() {
        return argumentType;
    }
    protected void setArgumentType(IType argumentType) {
        this.argumentType = argumentType;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    protected void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isAskConstraint() {
        return askConstraint;
    }
    protected void setAskConstraint(boolean askConstraint) {
        this.askConstraint = askConstraint;
    }
  
    @Override
    public String toString() {        
        return getArgumentType() + " " + getIdentifier() + " " + getArgumentType();
    }
    
    public boolean isEquality() {
        return isAskConstraint() && getIdentifier().equals("==");
    }
    
    public JavaConjunct getInstance(IArguments arguments) {
        return new JavaConjunct(this, arguments);
    }
    
    public boolean haveToIgnoreImplicitArgument() {
        return false;   // !!!!
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof JavaConstraint)
            && this.equals((JavaConstraint)obj);  
    }
    
    public boolean equals(JavaConstraint other) {
        return this == other || ( other != null               
            && this.isAskConstraint() == other.isAskConstraint()
            && this.getIdentifier().equals(other.getIdentifier())
            && this.getArgumentType().equals(other.getArgumentType())
        );
    }
}