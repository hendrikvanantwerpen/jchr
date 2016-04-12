package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable;

import java.lang.reflect.Method;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.ClassNameImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.solver.Solver;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.util.Cloneable;



/**
 * @author Peter Van Weert
 */
public abstract class BuiltInMethod<T extends BuiltInMethod> 
extends AbstractMethod<T> 
implements Cloneable<T> {

    private IImplicitArgument defaultImplicitArgument;
    
    /*
     * base is 
     * 	- solver ==> hasDefaultImplicitArgument !
     * 	- GenericType van de variabele 	==> ! hasDefaultImplicitArgument
     *  - of null als statisch ==> hasDefaultImplicitArgument !
     */
    public BuiltInMethod() {
        // NOP
    }
    
    public BuiltInMethod(Method method) {
        super(method);        
    }
    
    @Override
    public void init(Method method) {
        super.init(method);
        
        if (isStatic())
            setBase((GenericType)getDefaultImplicitArgument().getType());
    }
    
    @Override
    public void init(GenericType base, Method method) {
        super.init(base, method);
        
        if (isStatic())
            setDefaultImplicitArgument(new ClassNameImplicitArgument(method.getDeclaringClass()));
    }
    
    public BuiltInMethod(Solver solver, Method method) {
	    super(solver, method);
        setDefaultImplicitArgument(solver);        
    }
    
    protected BuiltInMethod(GenericType base, Method method) {
        super(base, method);
    }
  
    @Override
    public MatchingInfos canHaveAsArguments(IArguments arguments) {
        setImplicitArgumentOf(arguments);
        MatchingInfos result = super.canHaveAsArguments(arguments);
        unsetImplicitArgumentOf(arguments);
        return result;
    }

    @Override
    public IArgumented<T> getInstance(IArguments arguments, MatchingInfos infos) {
        setImplicitArgumentOf(arguments);
        return super.getInstance(arguments, infos);
    }
    
    protected void setImplicitArgumentOf(IArguments arguments) {
        if (arguments.hasImplicitArgument())
            throw new IllegalStateException("Implicit argument set for builtin-constraint");
        
        if (hasDefaultImplicitArgument())
            arguments.addImplicitArgument(getDefaultImplicitArgument());
        else
            arguments.markFirstAsImplicitArgument();
    }
    
    protected void unsetImplicitArgumentOf(IArguments arguments) {
        if (hasDefaultImplicitArgument())
            arguments.removeImplicitArgument();
        else
            arguments.removeImplicitArgumentMark();
    }
    
    protected IImplicitArgument getDefaultImplicitArgument() {
        return defaultImplicitArgument;
    }
    protected void setDefaultImplicitArgument(IImplicitArgument defaultImplicitArgument) {
        this.defaultImplicitArgument = defaultImplicitArgument;
    }
    protected boolean hasDefaultImplicitArgument() {
        return getDefaultImplicitArgument() != null;
    }
    
    public boolean haveToIgnoreImplicitArgument() {
        return hasDefaultImplicitArgument();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T clone() {
        try {
            // niet type-safe, weet't wel, maar liever lui dan moe ;-)
            return (T)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
