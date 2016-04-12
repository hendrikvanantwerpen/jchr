package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments;

import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;


/**
 * @author Peter Van Weert
 */
public interface IArguments extends Iterable<IArgument> {
    public List<IArgument> getArgumentList();

    public int getArity();

    public IArgument getArgumentAt(int index);
    
    public void replaceArgumentAt(int index, IArgument arguement);
    
    public IType[] getTypes();
    
    public IType getTypeAt(int index);

    public void addArgument(IArgument argument);
    
    public void addArgumentAt(int index, IArgument argument);
    
    public void addImplicitArgument(IImplicitArgument implicitArgument);
    
    public void removeImplicitArgument();
    
    public void markFirstAsImplicitArgument();
    
    public void removeImplicitArgumentMark();
    
    public boolean hasImplicitArgument();
    
    public void incorporate(MatchingInfos matchingInfos, boolean ignoreImplicitArgument);

    public boolean allFixed();
}