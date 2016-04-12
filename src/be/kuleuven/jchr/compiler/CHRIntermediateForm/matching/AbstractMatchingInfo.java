package be.kuleuven.jchr.compiler.CHRIntermediateForm.matching;


public abstract class AbstractMatchingInfo<T extends IMatchingInfo> 
implements IMatchingInfo<T> {
    
    protected final static byte
        NO_AMB_MASK = 30,                 // 1 1 1 1 0
        MATCH_MASK  = 28;                 // 1 1 1 0 0
    protected final static byte
        AMBIGUOUS_INFO     =  1 << 0,     // 0 0 0 0 1
        NO_MATCH_INFO      =  1 << 1,     // 0 0 0 1 0
        INIT_MATCH_INFO    =  1 << 2,     // 0 0 1 0 0
        COERCED_MATCH_INFO =  1 << 3,     // 0 1 0 0 0               
        EXACT_MATCH_INFO   =  1 << 4;     // 1 0 0 0 0
    protected final static byte
        COERCED_INIT_MATCH_INFO = COERCED_MATCH_INFO | INIT_MATCH_INFO;
    
    protected boolean has(int mask) {
        return (getInfo() & mask) > 0;
    }
    
    protected byte getMatchClass() {
        for (byte i = 2; i <= 4; i++)
            if (has(1 << i)) return i;
        return 0;
    }

    public boolean isMatch() {
        return has(MATCH_MASK);
    }
    
    public boolean isNonAmbiguousMatch() {        
        return isMatch() && !isAmbiguous();
    }
    
    public boolean isCoerceMatch() {
        return has(COERCED_MATCH_INFO);
    }

    public boolean isAmbiguous() {
        return has(AMBIGUOUS_INFO);
    }
    
    public boolean isExactMatch() {
        return has(EXACT_MATCH_INFO);
    }
    
    public boolean isNonExactMatch() {
        return isMatch() && !isExactMatch();
    }
    
    public boolean isInitMatch() {
        return has(INIT_MATCH_INFO);
    }
    
    public boolean isNonInitMatch() {
        return isMatch() && !isInitMatch();
    }

    protected abstract byte getInfo();
    
}
