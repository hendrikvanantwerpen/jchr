package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule;

import java.util.Iterator;
import java.util.NoSuchElementException;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.EmptyArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.Lookup;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.DefaultLookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.Rule;


public class DefaultSchedule implements ISchedule {
    private Occurrence occurrence;
    
    private int index;
    
    public DefaultSchedule(Occurrence occurrence) {
        setOccurrence(occurrence);
        setIndex(occurrence.getRuleOccurrenceIndex());
    }

    protected Occurrence getOccurrence() {
        return occurrence;
    }
    protected void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }
    
    protected int getIndex() {
        return index;
    }
    private void setIndex(int index) {
        this.index = index;
    }
    
    protected Rule getRule() {
        return getOccurrence().getRule();
    }
    protected int getNbOccurrences() {
        return getRule().getNbOccurrences();
    }
    
    public int getNbLookups() {
        return getRule().getNbOccurrences() - 1;
    }
    
    public int getNbGuardConstraints() {
        return getRule().getNbGuardConjuncts();
    }
    
    public Iterator<IScheduleComponent> iterator() {
        return new Iterator<IScheduleComponent>() {
            private int 
                currentLookupIndex = -1,
                nextLookupIndex = (getIndex() == 0)? 1 : 0,
                guardConstraintIndex = 0;    
            
            public boolean hasNext() {
                return hasNextLookup() || hasNextGuardConstraint();
            }
            
            protected boolean hasNextLookup() {
                return (nextLookupIndex < getNbOccurrences());
            }
            protected boolean hasNextGuardConstraint() {
                return (guardConstraintIndex < getNbGuardConstraints());
            }

            public IScheduleComponent next() throws NoSuchElementException {
                if (hasNextLookup()) {                
                    currentLookupIndex = nextLookupIndex;
                    final int current = currentLookupIndex;
                    if (++nextLookupIndex == getIndex()) nextLookupIndex++;
                    return new Lookup() {
                        @Override
                        public ILookupType getLookupType() {
                            return DefaultLookupType.getInstance();
                        }
                    
                        @Override
                        public Occurrence getOccurrence() {
                            return getRule().getOccurrenceAt(current);
                        }
                        
                        public IArguments getArguments() {
                            return EmptyArguments.getInstance();
                        }
                    };
                }
                else if (hasNextGuardConstraint())
                    return getRule().getGuardConjunctAt(guardConstraintIndex++);
                else
                    throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
