package be.kuleuven.jchr.compiler.CHRIntermediateForm.matching;

import static be.kuleuven.jchr.util.comparing.Comparison.AMBIGUOUS;
import static be.kuleuven.jchr.util.comparing.Comparison.BETTER;
import static be.kuleuven.jchr.util.comparing.Comparison.EQUAL;
import static be.kuleuven.jchr.util.comparing.Comparison.WORSE;

import java.util.Arrays;

import be.kuleuven.jchr.util.comparing.Comparison;



/**
 * @author Peter Van Weert
 */
public class MatchingInfos extends AbstractMatchingInfo<MatchingInfos> {

    private MatchingInfo[] assignmentInfos;
    
    private boolean ignoreImplicitArgument;
    
    public final static MatchingInfos 
    	NO_MATCH = new MatchingInfos() {

            @Override
	        protected byte getInfo() {
	            return NO_MATCH_INFO;
	        }

            @Override
	        public boolean isAmbiguous() {
	            return false;
	        }

            @Override
	        public boolean isCoerceMatch() {
	            return false;
	        }

            @Override
	        public boolean isExactMatch() {
	            return false;
	        }

            @Override
	        public boolean isMatch() {
	            return false;
	        }

            @Override
            public boolean isInitMatch() {
                return false;
            }

            @Override
            public boolean isNonAmbiguousMatch() {
                return false;
            }

            @Override
            public boolean isNonExactMatch() {
                return false;
            }

            @Override
            public String toString() {
                return "NO MATCH";
            }

            @Override
            public Comparison compareTo(MatchingInfos other) {
                return other.isMatch()? WORSE : EQUAL;
            }
	    }, 
        EXACT_MATCH = new MatchingInfos() {
                        
            @Override
            public MatchingInfo getAssignmentInfoAt(int index) {
                return MatchingInfo.EXACT_MATCH;
            }
            
            @Override
            protected byte getInfo() {
                return EXACT_MATCH_INFO;
            }

            @Override
            public boolean isAmbiguous() {
                return false;
            }

            @Override
            public boolean isCoerceMatch() {
                return false;
            }

            @Override
            public boolean isExactMatch() {
                return false;
            }

            @Override
            public boolean isMatch() {
                return true;
            }

            @Override
            public boolean isInitMatch() {
                return false;
            }

            @Override
            public boolean isNonAmbiguousMatch() {
                return false;
            }

            @Override
            public boolean isNonExactMatch() {
                return false;
            }

            @Override
            public String toString() {
                return "EXACT MATCH";
            }

            @Override
            public Comparison compareTo(MatchingInfos other) {
                throw new UnsupportedOperationException(
                    "This constant wasn't intended to be used in comparisons!" 
                );
            }
        },
	    AMBIGUOUS_NO_INIT = new MatchingInfos() {
	        @Override
            protected byte getInfo() {
	            return AMBIGUOUS_INFO;
	        }

            @Override
	        public boolean isAmbiguous() {
	            return true;
	        }

            @Override
	        public boolean isCoerceMatch() {
	            return true;
	        }

            @Override
	        public boolean isExactMatch() {
	            return false;
	        }
            
            @Override
	        public boolean isMatch() {
	            return true;
	        }
            
            @Override
            public boolean isInitMatch() {
                return false;
            }

            @Override
            public boolean isNonAmbiguousMatch() {
                return false;
            }

            @Override
            public boolean isNonExactMatch() {
                return true;
            }

            @Override
            public String toString() {
                return "AMBIGUOUS MATCH";
            }
            
            @Override
            public Comparison compareTo(MatchingInfos other) {
                if (other.isInitMatch()) return BETTER;
                else if (other.isCoerceMatch()) return AMBIGUOUS;
                else if (other.isExactMatch()) return WORSE;
                else return BETTER;
            }
	    },
        AMBIGUOUS_INIT = new MatchingInfos() {
            @Override
            protected byte getInfo() {
                return AMBIGUOUS_INFO;
            }

            @Override
            public boolean isAmbiguous() {
                return true;
            }

            @Override
            public boolean isCoerceMatch() {
                return true;    /* hoeft niet echt, maar all? */
            }

            @Override
            public boolean isExactMatch() {
                return false;
            }
            
            @Override
            public boolean isMatch() {
                return true;
            }
            
            @Override
            public boolean isInitMatch() {
                return true;
            }

            @Override
            public boolean isNonAmbiguousMatch() {
                return false;
            }

            @Override
            public boolean isNonExactMatch() {
                return true;
            }

            @Override
            public String toString() {
                return "AMBIGUOUS MATCH";
            }
            
            @Override
            public Comparison compareTo(MatchingInfos other) {
                if (other.isInitMatch()) return AMBIGUOUS;
                else if (other.isCoerceMatch() || other.isExactMatch()) return WORSE;
                else return BETTER;
            }
        };
    
    protected MatchingInfos() {
        // NOP (just declaring this has to be a protected constructor)
    }
        
    public MatchingInfos(int arity, boolean ignoreImplicitArgument) {
        this(new MatchingInfo[arity], ignoreImplicitArgument);
    }
    
    public MatchingInfos(MatchingInfo[] assignmentInfos, boolean ignoreImplicitArgument) {
        setAssignmentInfos(assignmentInfos);
        setIgnoreImplicitArgument(ignoreImplicitArgument);
    }
    
    public MatchingInfos(MatchingInfo assignmentInfo, boolean ignoreImplicitArgument) {
        this(new MatchingInfo[] {assignmentInfo}, ignoreImplicitArgument);
    }
    
    public MatchingInfo[] getAssignmentInfos() {
        return assignmentInfos;
    }
    public int getArity() {
        return getAssignmentInfos().length;        
    }
    protected void setAssignmentInfos(MatchingInfo[] assignmentInfos) {
        this.assignmentInfos = assignmentInfos;
    }
    
    public MatchingInfo getAssignmentInfoAt(int index) {
        return getAssignmentInfos()[index];
    }
    public void setAssignmentInfoAt(MatchingInfo info, int index) {
        getAssignmentInfos()[index] = info;
    }
    
    @Override
    protected byte getInfo() {
        byte result = 0;
        for (MatchingInfo info : getAssignmentInfos())
            result |= EXACT_MATCH_INFO ^ info.getInfo();
        return (byte)(EXACT_MATCH_INFO ^ result);
    }
    
    public Comparison compareTo(MatchingInfos other) {
        if ((other == NO_MATCH) || (other == AMBIGUOUS_INIT) || (other == AMBIGUOUS_NO_INIT))
            return Comparison.flip(other.compareTo(this));
        
        Comparison comparison = 
            Comparison.get(this.getMatchClass() - other.getMatchClass());
        
        if (comparison == EQUAL && isNonExactMatch()) {
            // als beiden coerces zijn, en minstens 1 ervan ambigu, 
            // is vergelijking niet mogelijk 
            if (this.isAmbiguous() || other.isAmbiguous())
                return AMBIGUOUS;
            
            final int arity = this.getArity();
            Comparison temp;
            for (int i = this.getStartIndex(), j = other.getStartIndex(); i < arity; i++, j++) {
                temp = this.getAssignmentInfoAt(i).compareTo(other.getAssignmentInfoAt(j));
                switch (temp) {
                	// als er minstens 1 vergelijking ambigu is, is vergelijking niet mogelijk
                 	case AMBIGUOUS:
                 	    return AMBIGUOUS;
             	    /*break;*/
                 	    
             	    // als er een verschil is moet...
             	    case BETTER:
         	        case WORSE:                    
         	            if (comparison == EQUAL) // ... na de eerste keer ...
         	                comparison = temp;
         	            else if (comparison != temp) // ... de vergelijking steeds hetzelfde zijn
         	                return AMBIGUOUS;
                }
            }
        }
        
        return comparison;
    }
    
    @Override
    public String toString() {
        return Arrays.deepToString(getAssignmentInfos()) + " (" + getInfo() + ")";
    }

    public boolean haveToIgnoreImplicitArgument() {
        return ignoreImplicitArgument;
    }
    protected int getStartIndex() {
        return haveToIgnoreImplicitArgument()? 1 : 0;
    }
    protected void setIgnoreImplicitArgument(boolean ignoreImplicitArgument) {
        this.ignoreImplicitArgument = ignoreImplicitArgument;
    }
    
    public static MatchingInfos fromBoolean(boolean bool) {
        return bool? EXACT_MATCH : NO_MATCH;
    }
}
