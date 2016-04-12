package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments;

import java.util.Iterator;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;


public class ArgumentsDecorator implements IArguments {
    private IArguments arguments;
    
    public IArguments getArguments() {
        return arguments;
    }
    protected void setArguments(IArguments arguments) {
        this.arguments = arguments;
    }
    
    public List<IArgument> getArgumentList() {
        return getArguments().getArgumentList();
    }
    
    public Iterator<IArgument> iterator() {
        return getArguments().iterator();
    }

    public int getArity() {
        return getArguments().getArity();
    }

    public IArgument getArgumentAt(int index) {
        return getArguments().getArgumentAt(index);
    }
    
    public be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType[] getTypes() {
        return getArguments().getTypes();
    }
    
    public be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType getTypeAt(int index) {
        return getArguments().getTypeAt(index);
    }

    public void addArgument(IArgument argument) {
        getArguments().addArgument(argument);
    }
    
    public void replaceArgumentAt(int index,IArgument argument) {
        getArguments().replaceArgumentAt(index, argument);
    }
    
    public void addArgumentAt(int index,IArgument argument) {
        getArguments().addArgumentAt(index, argument);
    }

    public void addImplicitArgument(IImplicitArgument implicitArgument) {
        getArguments().addImplicitArgument(implicitArgument);
    }
    
    public boolean hasImplicitArgument() {
        return getArguments().hasImplicitArgument();
    }
    
    public void markFirstAsImplicitArgument() {
        getArguments().markFirstAsImplicitArgument();
    }
    
    public void removeImplicitArgument() {
        getArguments().removeImplicitArgument();
    }
    
    public void removeImplicitArgumentMark() {
        getArguments().removeImplicitArgumentMark();
    }
    
    public void incorporate(MatchingInfos assignmentInfos, boolean ignoreImplicitArgument) {
        getArguments().incorporate(assignmentInfos, ignoreImplicitArgument);            
    }
    
    public boolean allFixed() {
        return getArguments().allFixed();
    }        
    
    @Override
    public String toString() {
        return getArguments().toString();
    }
}