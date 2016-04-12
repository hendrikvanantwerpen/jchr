package be.kuleuven.jchr.compiler.codeGeneration.util.builder;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Map;

import be.kuleuven.jchr.util.builder.BuilderException;
import be.kuleuven.jchr.util.builder.IBuilder;

import static be.kuleuven.jchr.compiler.codeGeneration.util.builder.BuilderStack.Node;

public abstract class StackBasedBuilder<Result> implements IBuilder<Result> {
    private BuilderStack stack;
    
    protected BuilderStack getStack() {
        return stack;
    }
    private void setStack(BuilderStack stack) {
        this.stack = stack;
    }
    
    public void init() throws BuilderException {
        setStack(new BuilderStack());
    }
    
    /*
     * Convenience methods: 
     */
    protected final void beginList(final int size, final String key) throws BuilderException { 
        try {
            getStack().beginList(size, key);
        } catch (EmptyStackException ese) {
            throw new BuilderException(ese);
        } catch (NullPointerException npe) {
            throw new BuilderException("Top of stack is not a Map");
        }
    }
    protected final void beginMap(final int size, final String key) throws BuilderException {
        try {
            getStack().beginMap(size, key);
        } catch (EmptyStackException ese) {
            throw new BuilderException(ese);
        } catch (NullPointerException npe) {
            throw new BuilderException("Top of stack is not a Map");
        }
    }
    protected final void beginList(final int size) throws BuilderException { 
        try {
            getStack().beginList(size);
        } catch (EmptyStackException ese) {
            throw new BuilderException(ese);
        } catch (NullPointerException npe) {
            throw new BuilderException("Top of stack is not a List");
        }
    }
    protected final void beginMap(final int size) throws BuilderException {
        try {
            getStack().beginMap(size);
        } catch (EmptyStackException ese) {
            throw new BuilderException(ese);
        } catch (NullPointerException npe) {
            throw new BuilderException("Top of stack is not a List");
        }
    }
    protected Node getRoot() throws BuilderException {
        try {
            return getStack().get(0);
        } catch (EmptyStackException ese) {
            throw new BuilderException(ese);
        }
    }
    protected Node peek() throws BuilderException {
        try {
            return getStack().peek();
        } catch (EmptyStackException ese) {
            throw new BuilderException(ese);
        }
    }
    protected Node pop() throws BuilderException {
        try {
            return getStack().pop();
        } catch (EmptyStackException ese) {
            throw new BuilderException(ese);
        }
    }
    protected void push(List<Object> list) {
        getStack().push(list);
    }
    protected void push(Map<String, Object> map) {
        getStack().push(map);
    }
}
