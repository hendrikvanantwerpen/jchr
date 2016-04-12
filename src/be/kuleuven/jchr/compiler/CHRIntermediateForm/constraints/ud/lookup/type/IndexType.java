package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type;

public enum IndexType {
    NONE, HASH_MAP(2);
    
    private IndexType() {
        this(0);
    }
    
    private IndexType(int infoSize) {
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
