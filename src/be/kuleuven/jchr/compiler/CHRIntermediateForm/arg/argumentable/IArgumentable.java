package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.util.comparing.Comparable;

/**
 * @author Peter Van Weert
 */
public interface IArgumentable<T extends IArgumentable<?>> extends Comparable<IArgumentable<?>> {
    
    public int getArity();
    
    public IType[] getFormalParameters();
    
    public IType getFormalParameterAt(int index);
    
    public MatchingInfos canHaveAsArguments(IArguments arguments);
    
    public MatchingInfo canHaveAsArgumentAt(int index, IArgument argument);
    
    public IArgumented<T> getInstance(IArguments arguments, MatchingInfos infos);
    
    /*
     * @pre Er moet niets meer gebeuren met deze arguments, i.e.
     *  alles ivm coercing, initialisatie en impliciete argumenten is OK!
     */
    public IArgumented<T> getInstance(IArguments arguments);
    
    public boolean haveToIgnoreImplicitArgument();
}
