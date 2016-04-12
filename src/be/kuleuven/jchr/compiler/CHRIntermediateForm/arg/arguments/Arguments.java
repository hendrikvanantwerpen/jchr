package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.CoerceMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;


/**
 * @author Peter Van Weert
 */
public class Arguments implements IArguments {
    
    private List<IArgument> arguments;
       
    private boolean hasImplicitArgument;
    
    public Arguments() {
        this(new ArrayList<IArgument>());
    }
    
    public Arguments(int arity) {
        this(new ArrayList<IArgument>(arity));
    }
    
    public Arguments(IArgument... argument) {
        this(new ArrayList<IArgument>(Arrays.asList(argument)));
    }
    
    public Arguments(List<IArgument> arguments) {
        setArgumentList(arguments);
    }
    
    public Arguments(IImplicitArgument implicitArgument) {
        setArgumentList(Collections.singletonList((IArgument)implicitArgument));
        markFirstAsImplicitArgument();
    }
    
    public List<IArgument> getArgumentList() {
        return arguments;
    }
    public Iterator<IArgument> iterator() {
        return arguments.iterator();
    }
    public int getArity() {
        return getArgumentList().size();
    }
    public IArgument getArgumentAt(int index) {
        return getArgumentList().get(index);
    }
    public void replaceArgumentAt(int index,IArgument arguement) {
        setArgumentAt(index, arguement);
    }
    public void addArgumentAt(int index,IArgument argument) {
        getArgumentList().add(index, argument);
    }
    protected void setArgumentAt(int index, IArgument argument) {
        getArgumentList().set(index, argument);
    }
    public IType getTypeAt(int index) {
        return getArgumentAt(index).getType();
    }
    protected void setArgumentList(List<IArgument> arguments) {
        this.arguments = arguments;
    }
    
    public void addArgument(IArgument argument) {
        getArgumentList().add(argument);
    }
    
    public IType[] getTypes() {
        IType[] result = new IType[getArity()];
        for (int i = 0; i < getArity(); i++)
            result[i] = getTypeAt(i);
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        
        final int nbArgs = getArity();
        if (nbArgs > 0)
            builder.append(getArgumentAt(0));
        for (int i = 1; i < nbArgs; i++) {
            builder.append(", ");
            builder.append(getArgumentAt(i));
        }
        
        builder.append(')');
        return builder.toString();
    }
    
    public void addImplicitArgument(IImplicitArgument implicitArgument) {
        if (hasImplicitArgument()) removeImplicitArgument();        
        getArgumentList().add(0, implicitArgument);
        markFirstAsImplicitArgument();
    }
    
    public void removeImplicitArgument() {
        getArgumentList().remove(0);
        removeImplicitArgumentMark();
    }
    
    public boolean hasImplicitArgument() {        
        return hasImplicitArgument;
    }
    
    public void markFirstAsImplicitArgument() {
        hasImplicitArgument = true;
    }
    
    public void removeImplicitArgumentMark() {
        hasImplicitArgument = false;
    }
    
    public void incorporate(MatchingInfos assignmentInfos, boolean ignoreImplicitArgument) {
        if (assignmentInfos.isExactMatch()) return;

        IArgument argument;
        MatchingInfo info;        
        final int arity = getArity();
        for (int i = ignoreImplicitArgument? 1 : 0; i < arity; i++) {
            argument = getArgumentAt(i);
            info = assignmentInfos.getAssignmentInfoAt(i);
            for (CoerceMethod method : info.getCoerceMethods())
                argument = method.getInstance(argument);
            if (info.isInitMatch())
                argument = info.getInitialisator().getInstance(argument);
            setArgumentAt(i, argument);
        }
    }
    
    public boolean allFixed() {
        for (IArgument argument : this)
            if (! argument.isFixed()) return false;
        return true;
    }
    
}