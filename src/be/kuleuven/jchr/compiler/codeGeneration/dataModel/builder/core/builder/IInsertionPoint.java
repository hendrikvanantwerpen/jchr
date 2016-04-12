package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.core.builder;

public interface IInsertionPoint {

    public void insert(Object o);
    
    public void setRootIdentifier(String identifier);
     
    public final static class EchoInsertionPoint implements IInsertionPoint {
        public void insert(Object o) {
            System.out.println("Inserting " + o);
        }
        
        public void setRootIdentifier(String identifier) {
            System.out.println("Inserting-root-identifier set to " + identifier);
        }
    }
}