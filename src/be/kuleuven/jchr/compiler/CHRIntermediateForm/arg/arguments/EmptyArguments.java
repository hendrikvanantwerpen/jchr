package be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IImplicitArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.util.iterator.EmptyIterator;



public final class EmptyArguments implements IArguments {
    
    private EmptyArguments() { /* SINGLETON */ }    
    private static EmptyArguments instance;
    public static EmptyArguments getInstance() {
        if (instance == null)
            instance = new EmptyArguments();
        return instance;
    }
    
    public List<IArgument> getArgumentList() {
        return Collections.emptyList();
    }
    public Iterator<IArgument> iterator() {
        return EmptyIterator.getInstance();
    }

    public int getArity() {
        return 0;
    }

    public IArgument getArgumentAt(int index) {
        throw new IndexOutOfBoundsException();
    }

    public void replaceArgumentAt(int index, IArgument arguement) {
        throw new IndexOutOfBoundsException();
    }

    public IType[] getTypes() {
        return new IType[0];
    }

    public IType getTypeAt(int index) {
        throw new IndexOutOfBoundsException();
    }

    public void addArgument(IArgument argument) {
        throw new IndexOutOfBoundsException();
    }

    public void addArgumentAt(int index, IArgument argument) {
        throw new IndexOutOfBoundsException();            
    }

    public void addImplicitArgument(IImplicitArgument implicitArgument) {
        throw new IndexOutOfBoundsException();            
    }

    public void removeImplicitArgument() {
        throw new UnsupportedOperationException();            
    }

    public void markFirstAsImplicitArgument() {
        throw new UnsupportedOperationException();            
    }

    public void removeImplicitArgumentMark() {
        throw new UnsupportedOperationException();            
    }

    public boolean hasImplicitArgument() {
        return false;
    }

    public void incorporate(MatchingInfos assignmentInfos, boolean ignoreImplicitArgument) {
        // NOP
    }

    public boolean allFixed() {
        return true;
    }
}