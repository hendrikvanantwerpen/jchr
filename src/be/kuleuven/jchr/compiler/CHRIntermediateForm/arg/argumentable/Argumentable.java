package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable;

import static be.kuleuven.jchr.util.comparing.Comparison.AMBIGUOUS;
import static be.kuleuven.jchr.util.comparing.Comparison.EQUAL;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.util.comparing.Comparison;


/**
 * @author Peter Van Weert
 */
public abstract class Argumentable<T extends IArgumentable<?>> 
implements IArgumentable<T> {
    
    public IType[] getFormalParameters() {
        final IType[] result = new IType[getArity()];
        for (int i = 0; i < result.length; i++)
            result[i] = getFormalParameterAt(i);
        return result;
    }
    
    public static MatchingInfos canHaveAsArguments(final IArgumentable<?> argumentable, final IArguments arguments) {
        final int arity = argumentable.getArity();
        if (arity != arguments.getArity())
            return MatchingInfos.NO_MATCH;

        MatchingInfos result = new MatchingInfos(
            arity, 
            argumentable.haveToIgnoreImplicitArgument()
        );
        MatchingInfo temp;
        
        for (int i = 0; i < arity; i++) {
            temp = argumentable.canHaveAsArgumentAt(i, arguments.getArgumentAt(i));
                       
            if (temp.isAmbiguous())
                return temp.isInitMatch()
                    ? MatchingInfos.AMBIGUOUS_INIT
                    : MatchingInfos.AMBIGUOUS_NO_INIT;
            else if (!temp.isMatch()) 
                return MatchingInfos.NO_MATCH;
            else
                result.setAssignmentInfoAt(temp, i);
        }
                
        return result;
    }
    
    public MatchingInfos canHaveAsArguments(IArguments arguments) {        
        return canHaveAsArguments(this, arguments);
    }
        
    public MatchingInfo canHaveAsArgumentAt(int index, IArgument argument) {
        return argument.isAssignableTo(getFormalParameterAt(index));
    }
    
    @Override
    public String toString() {
        return toString(this);
    }
    
    public IArgumented<T> getInstance(IArguments arguments, MatchingInfos infos) {
        arguments.incorporate(infos, haveToIgnoreImplicitArgument());
        return getInstance(arguments);
    }
    
    public static String toString(IArgumentable<?> argumentable) {
        StringBuilder result = new StringBuilder().append('(');
        final int arity = argumentable.getArity();
        if (arity > 0)
            result.append(argumentable.getFormalParameterAt(0).toTypeString());
        for (int i = 1; i < arity; i++)
            result.append(", ").append(argumentable.getFormalParameterAt(i).toTypeString());
        return result.append(')').toString();
    }
    
    public static Comparison compare(IArgumentable<?> one, IArgumentable<?>  other) {
        final int arity = one.getArity();
        Comparison comparison = EQUAL, temp;
        
        for (int i = 0; i < arity; i++) {
            temp = other.getFormalParameterAt(i).compareTo(one.getFormalParameterAt(i));
            switch (temp) {
            	// als er minstens 1 vergelijking ambigu is, is vergelijking niet mogelijk
            	case AMBIGUOUS:
            	    return AMBIGUOUS;
        	    /*break;*/
            
        	    // als er een verschil is moet...
        	    case BETTER:
    	        case WORSE:
    	            if (comparison == EQUAL) // ... na de eerste keer ...
    	                comparison = temp;
	            	else if (comparison != temp) // ... de vergelijking steeds hetzelfde zijn
	            	    return AMBIGUOUS;
            	break;
            }
        }
        
        return comparison;
    }
    
    public Comparison compareTo(IArgumentable<?> other) {
        return compare(this, other);
    }
    
    @Override
    public int hashCode() {
        int result = 23;
        final int arity = getArity();
        for (int i = 0; i < arity; i++)            
            result = 37 * result + getFormalParameterAt(i).hashCode();            
        return result;
    }
    
    @Override
    public abstract boolean equals(Object obj);
}
