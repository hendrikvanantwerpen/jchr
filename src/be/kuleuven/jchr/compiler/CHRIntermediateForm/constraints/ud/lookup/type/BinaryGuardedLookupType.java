package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.Dummy;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.Arguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.category.ILookupCategory;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfos;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.variables.Variable;


public class BinaryGuardedLookupType extends LookupType {

    private List<BinaryGuardInfo> guards;
    
    public BinaryGuardedLookupType(ILookupCategory lookupCategory, int nbGuards) {        
        this(lookupCategory);
        setGuards(new ArrayList<BinaryGuardInfo>(nbGuards));
    }
    
    public BinaryGuardedLookupType(ILookupCategory lookupCategory) {        
        super(lookupCategory);
        setGuards(new ArrayList<BinaryGuardInfo>());
    }
    
    public void addGuard(IArgumented<?> guard, int otherIndex) {
        getGuards().add(new BinaryGuardInfo(guard, otherIndex));
    }
    
    public List<BinaryGuardInfo> getGuards() {
        return guards;
    }
    public int getNbGuards() {
        return getGuards().size();
    }
    protected void setGuards(List<BinaryGuardInfo> guards) {
        this.guards = guards;
    }
    
    public ILookupType getUniqueInstance() {
        final int index = getCategory().getIndexOf(this);
        if (index >= 0)
            return getCategory().getLookupTypeAt(index);
        else {
            getCategory().addLookupType(this);
            return this;
        }
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof BinaryGuardedLookupType)
            && this.equals((BinaryGuardedLookupType)other);
    }
    
    public boolean equals(BinaryGuardedLookupType other) {
        return super.equals(other)
            && this.getGuards().equals(other.getGuards());
    }
    
    public static class BinaryGuardInfo {
        private int otherIndex;
        
        private IArgumented<?> guard;
        
        public BinaryGuardInfo(IArgumented<?> guard, int otherIndex) {
            setOtherIndex(otherIndex);
            
            // een lekker lange uitdrukking nu:
            setGuard(guard.getArgumentableType().getInstance(
                    new Arguments(
                        (otherIndex == 0)
                            ? Dummy.getOtherInstance(guard.getArgumentIgnoringImplicitAt(0).getType())
                            : Dummy.getOneInstance(((Variable)guard.getArgumentIgnoringImplicitAt(0)).getVariableType()),
                        (otherIndex == 1)
                            ? Dummy.getOtherInstance(guard.getArgumentIgnoringImplicitAt(1).getType())
                            : Dummy.getOneInstance(((Variable)guard.getArgumentIgnoringImplicitAt(1)).getVariableType())
                    ), 
                    MatchingInfos.EXACT_MATCH
                )
            );
        }

        public IArgumented<?> getGuard() {
            return guard;
        }
        protected void setGuard(IArgumented<?> guard) {
            this.guard = guard;
        }
        
        public IType getOtherType() {
            return getGuard().getArgumentIgnoringImplicitAt(getOtherIndex()).getType();
        }
        public String getOtherTypeString() {
            return getOtherType().toTypeString();
        }
        
        protected int getOtherIndex() {
            return otherIndex;
        }
        protected void setOtherIndex(int otherIndex) {
            this.otherIndex = otherIndex;
        }
        
        @Override
        public boolean equals(Object other) {
            return (other instanceof BinaryGuardInfo)
                && this.equals((BinaryGuardInfo)other);
        }
        
        public boolean equals(BinaryGuardInfo other) {
            return this.getOtherType().equals(other.getOtherType());
        }
        
        @Override
        public String toString() {
            return guard.toString() + ':' + getOtherIndex();
        }
    }
}
