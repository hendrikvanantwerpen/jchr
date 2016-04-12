package be.kuleuven.jchr.compiler.CHRIntermediateForm.init;

import static be.kuleuven.jchr.util.comparing.Comparison.BETTER;
import static be.kuleuven.jchr.util.comparing.Comparison.EQUAL;
import static be.kuleuven.jchr.util.comparing.Comparison.WORSE;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.Argumentable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Constructor;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.PrimitiveType;
import be.kuleuven.jchr.util.comparing.Comparison;


public abstract class Initialisator<T extends IInitialisator> 
    extends Argumentable<T> 
    implements IInitialisator<T> {
    
    public static IInitialisator<?> getInitialisatorFrom(GenericType base, IType type)
    throws AmbiguityException {
        if (base.isImmutableJavaWrapper())
            return InitialisatorMethod.getWrapperInitialisatorMethodFrom(base, type);
        
        IInitialisator<?> result;
        result = InitialisatorMethod.getInitialisatorMethodFrom(base, type);
        
        if (result == null) {
            if (PrimitiveType.isPrimitive(type))
                return getInitialisatorFromWrapper(base, (PrimitiveType)type);
            else
                return Constructor.getInitialisatorFrom(base, type);
        }
        
        return null;
    }
    
    protected static IInitialisator<?> getInitialisatorFromWrapper(GenericType base, PrimitiveType primitiveType)
    throws AmbiguityException {
        final GenericType[] wrapperTypes = primitiveType.getWrapperTypes();         
        
        IInitialisator<?> temp;
        for (int i = 0; i < wrapperTypes.length; i++) { 
            temp = getInitialisatorFrom(base, wrapperTypes[i]);
            if (temp != null) {
                @SuppressWarnings("unchecked")                
                final IInitialisator<?> result = new WrapperInitialisator(temp, wrapperTypes[i], primitiveType); 
                return result;
            }
        }
        
        return null;
    }
    
    public static Comparison compare(IInitialisator<?> one, IInitialisator<?> other) {
        // bij initialisatie is het meest specifieke type het beste!
        Comparison comparison
            = other.getType().compareTo(one.getType());
        
        if (comparison == EQUAL) {
            final boolean 
                x = one.getIdentifierIndex() < 0,
                y = other.getIdentifierIndex() < 0;
        
            if (x == y) return EQUAL;
            if (x) return BETTER;
            if (y) return WORSE;
            throw new InternalError("What The Fu..!");
        }
        else return comparison;
    }
    
    public Comparison compareTo(IInitialisator other) {
        return compare(this, other);
    }
    
    public int getArgumentIndex() {
        return getArgumentIndex(this);
    }
    
    public static int getArgumentIndex(IInitialisator<?> initialisator) {
        return initialisator.isConstructor()
            ? initialisator.usesIdentifier()
                ? (initialisator.getIdentifierIndex() + 1) % 2
                : 0
            : initialisator.usesIdentifier()
                ? initialisator.getIdentifierIndex() % 2 + 1
                : 1
            ;
    }
    
    public boolean isValidInitialisatorFrom(IType type) {
        return isValidInitialisatorFrom(this, type);
    }
    
    public static boolean isValidInitialisatorFrom(IInitialisator<?> initialisator, IType type) {
        return initialisator.isValidInitialisator() &&
            type.isDirectlyAssignableTo(initialisator.getFormalParameterAt(getArgumentIndex(initialisator)));
    }
    public static boolean hasValidIdentifierParameter(IInitialisator<?> initialisator) {
        return !initialisator.usesIdentifier() || 
            initialisator.getFormalParameterAt(initialisator.getIdentifierIndex()) 
                == GenericType.getNonParameterizableInstance(String.class);
    }

    
    public boolean usesIdentifier() {
        return getIdentifierIndex() >= 0;
    }    
}