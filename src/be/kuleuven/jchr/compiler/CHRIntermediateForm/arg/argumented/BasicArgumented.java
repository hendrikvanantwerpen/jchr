package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented;


import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.ArgumentsDecorator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;

/**
 * @author Peter Van Weert
 */
public class BasicArgumented
    extends ArgumentsDecorator
    implements IBasicArgumented {
    
    public BasicArgumented(IArguments arguments) {
        setArguments(arguments);        
    }
    
    public MatchingInfos canHaveAsArguments(IArguments arguments) {
        if (arguments == getArguments()) 
            return MatchingInfos.EXACT_MATCH;
        else
            throw new UnsupportedOperationException();
    }
    public MatchingInfo canHaveAsArgumentAt(int index, IArgument argument) {
        if (argument == getArgumentAt(index))
            return MatchingInfo.EXACT_MATCH;
        else
            throw new UnsupportedOperationException();
    }
}