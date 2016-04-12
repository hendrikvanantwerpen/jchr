package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.IArgumentable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;
import be.kuleuven.jchr.util.comparing.Comparison;



/**
 * @author Peter Van Weert
 */
public class Argumented<T extends IArgumentable<?>> extends BasicArgumented implements IArgumented<T> {    
    
    private T type;
    
    public Argumented(T type, IArguments arguments) {
        super(arguments);
        setType(type);                
    }
    
    @Override
    public void incorporate(MatchingInfos assignmentInfos, boolean ignoreImplicitArgument) {
        throw new UnsupportedOperationException();
    }

    public T getArgumentableType() {
        return type;
    }
    public boolean hasAsArgumentableType(T type) {
        return getArgumentableType().equals(type);
    }
    protected void setType(T type) {
        this.type = type;
    }
    
    @Override
    public Iterator<IArgument> iterator() {
        return getArguments().iterator();
    }
    
    public IType[] getFormalParameters() {
        return getArgumentableType().getFormalParameters();
    }
    
    public IArgumented<T> getInstance(IArguments arguments, MatchingInfos assignmentInfos) {
        throw new UnsupportedOperationException();
    }
    
    public IArgumented<T> getInstance(IArguments arguments) {
        throw new UnsupportedOperationException();
    }
    
    public IType getFormalParameterAt(int index) {
        return getArgumentableType().getFormalParameterAt(index);
    }
    
    public boolean haveToIgnoreImplicitArgument() {
        return getArgumentableType().haveToIgnoreImplicitArgument();
    }
    public IArgument getArgumentIgnoringImplicitAt(int index) {
        return getArgumentAt(index  + (haveToIgnoreImplicitArgument()? 1 : 0));
    }
    public int getArityIgnoringImplicitArgument() {
        return getArity() - (haveToIgnoreImplicitArgument()? 1 : 0);
    }
    
    public Comparison compareTo(IArgumentable other) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return "Argumentable: " + getArgumentableType() 
            + " Arguments: " + super.toString();
    }
    
    public int getNbVariableArguments() {
        return getVariablesInArguments().size();
    }
    
    public Set<Variable> getVariablesInArguments() {
        Set<Variable> result = new HashSet<Variable>();
        for (IArgument argument : getArguments()) {
            switch (argument.getArgumentType()) {
                case VARIABLE: 
                    result.add((Variable)argument);
                break;
                
                case CONSTRUCTOR_INVOCATION:
                case FIELD_ACCESS:
                case METHOD_INVOCATION:
                    result.addAll(((IArgumented<?>)argument).getVariablesInArguments());
                break;
            }
        }
        
        return result;
    }
}
