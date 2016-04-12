package be.kuleuven.jchr.compiler.codeGeneration.dataModel.builder.current.commando;

public enum CommandoType {
    CHECK_FIRED(1), GUARD(1), COMMIT(2), LOOKUP(3);
    
    private CommandoType(int infoSize) {
        setInfoSize(infoSize);
    }
    
    private int infoSize;

    public int getInfoSize() {
        return infoSize;
    }
    protected void setInfoSize(int infoSize) {
        this.infoSize = infoSize;
    }
}
