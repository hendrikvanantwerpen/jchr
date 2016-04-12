package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.extender;

import be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder.IInsertionPoint;
import be.kuleuven.jchr.compiler.codeGeneration.util.builder.BuilderStack.Node;

public class InsertionPoint implements IInsertionPoint {

    private Node node;
    
    private String rootIdentifier;
    
    public InsertionPoint(Node node) {
        setNode(node);
    }
    
    public InsertionPoint(Node node, String rootIdentifier) {
        setNode(node);
        setRootIdentifier(rootIdentifier);
    }
    
    public Node getNode() {
        return node;
    }
    protected void setNode(Node node) {
        this.node = node;
    }

    public String getRootIdentifier() {
        return rootIdentifier;
    }
    public boolean hasRootIdentifier() {
        return getRootIdentifier() != null;
    }
    public void setRootIdentifier(String rootIdentifier) {
        this.rootIdentifier = rootIdentifier;
    }
    
    public void insert(Object o) {
        if (getNode().isMap()) {
            if (! hasRootIdentifier())
                throw new IllegalStateException();
            else
                getNode().put(getRootIdentifier(), o);
        }
        else if (getNode().isList()){
            if (hasRootIdentifier())
                throw new IllegalStateException();
            else
                getNode().add(o);
        }
        else
            throw new IllegalStateException();
    }
}
