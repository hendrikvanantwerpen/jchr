package be.kuleuven.jchr.compiler.CHRIntermediateForm.types;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo.EXACT_MATCH;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.jchr.annotations.JCHR_Fixed;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.AssignmentInitialisator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IDeclarator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IInitialisator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.Initialisator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.InitialisatorMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.CoerceMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Constructor;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Field;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Method;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.reflect.Reflection;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.parameterizable.BasicParameterizable;
import be.kuleuven.jchr.runtime.Observable;
import be.kuleuven.jchr.runtime.hash.HashObservable;
import be.kuleuven.jchr.util.comparing.Comparison;



/**
 * @author Peter Van Weert
 */
public class GenericType
	extends BasicParameterizable 
    implements IType {

    private transient static Map<Class<?>, GenericType> cache;
    
    static {
        cache = new HashMap<Class<?>, GenericType>();
    }
    
    /**
     * @param value
     */
    protected GenericType(Class<?> rawType) {
        super(rawType);
    }
    
    /**
     * @param nbParameters
     * @param theClass
     */
    protected GenericType(int nbParameters, Class<?> theClass) {
        super(nbParameters, theClass);
    }

    public static GenericType getInstance(Class<?> rawType) {
        final int nbParameters = getNbParameters(rawType);
        if (nbParameters > 0)
            return new GenericType(nbParameters, rawType);
        else
            return getNonParameterizableInstance(rawType);
    }
    
    public static GenericType getNonParameterizableInstance(Class<?> rawType) {
        GenericType result = cache.get(rawType);
        
        if (result == null) {
            result = new GenericType(0, rawType);
            cache.put(rawType, result);
        }

        return result;
    }
    
    public static GenericType getClassInstance(Class<?> someClass) {
        final GenericType classInstance = 
            GenericType.getInstance(Class.class);
        classInstance.addTypeParameter(
            GenericType.getInstance(someClass)
        );
        return classInstance;
    }
    
    public boolean isInterface() {
        return getRawType().isInterface();
    }
    
    public boolean isFixed() {
        return isImmutableJavaWrapper() || getRawType().isAnnotationPresent(JCHR_Fixed.class);
    }
    
    public MatchingInfo isAssignableTo(IType other) {
        if (this.isDirectlyAssignableTo(other))
            return EXACT_MATCH;
        
        MatchingInfo result = new MatchingInfo();
        MatchingInfo info;
        for (CoerceMethod coerce : getCoerceMethods()) {            
            info = coerce.getReturnType().isAssignableTo(other);
            if (info.isMatch() && ! result.initCoerceMethods(info, coerce))
                break;  /* AMBIGUOUS! */
        }
        
        try {
            if (! result.isMatch() || result.isInitMatch()) {
                result.initInitialisator(other.getInitialisatorFrom(this));
            }
        } catch (AmbiguityException ae) {
            result.setAmbiguous();
        }
        
        return result;        
    }    
    public boolean isDirectlyAssignableTo(IType type) {
        if (this.equals(type)) return true;
        
        if (type instanceof GenericType) {
            final GenericType other = (GenericType)type;
                        
            if (! other.getRawType().isAssignableFrom(this.getRawType()))
                return false;
            
            for (int i = 0; i < other.getNbTypeParameters(); i++)
                if (! Reflection.reflect(this, other.getTypeVariableAt(i))
                        .equals(other.getTypeParameterAt(i)))
                  return false;
            
            return true;
            
        } else if (type instanceof PrimitiveType) {
            return false;
        } else if (type instanceof TypeParameter) {
            for (IType other : ((TypeParameter)type).getUpperBounds())
                if (this.isDirectlyAssignableTo(other))
                    return true;

            return false;
        } else throw new RuntimeException();
    }
    
    public boolean isImmutableJavaWrapper() {
        return isPrimitiveWrapper()
            || isImmutableNonPrimitiveNumberWrapper()
            || getRawType().equals(String.class);
    }
    
    public boolean isPrimitiveWrapper() {
        return isComparablePrimitiveWrapper()
            || getRawType().equals(Boolean.class);
    }
    
    public boolean isComparablePrimitiveWrapper() {
        return isPrimitiveNumberWrapper()
            || getRawType().equals(Character.class);
    }
    
    public boolean isPrimitiveNumberWrapper() {
        final Class<?> rawType = getRawType();
        return rawType.equals(Integer.class)
            || rawType.equals(Long.class)
            || rawType.equals(Float.class)
            || rawType.equals(Float.class)
            || rawType.equals(Double.class)
            || rawType.equals(Byte.class)
            || rawType.equals(Short.class);
    }
    
    public boolean isImmutableNumberWrapper() {
        // return Number.class.isAssignableFrom(getRawType());
        
        // better (since there are Number subclasses that are mutable):
        return isPrimitiveNumberWrapper()
            || isImmutableNonPrimitiveNumberWrapper();
    }
    
    protected boolean isImmutableNonPrimitiveNumberWrapper() {
        return getRawType().equals(BigInteger.class)
            || getRawType().equals(BigDecimal.class);
    }
    
	@Override
    public boolean equals(Object other) {
	    return (this == other) ||
            ((other instanceof GenericType)
	          && this.equals((BasicParameterizable)other));
	}
    
	private List<CoerceMethod> coerceMethods;
	
    public List<CoerceMethod> getCoerceMethods() {
        // eens het als variabletype wordt gebruikt is het niet
        // onredelijk te verwachten dat deze info nog wel eens
        // zal worden opgevraagd ==> cachen?
        if (coerceMethods == null) initCoerceMethods();
        return coerceMethods;
    }
    
    public void initCoerceMethods() {
        setCoerceMethods(CoerceMethod.getCoerceMethods(this));
    }
    
    protected void setCoerceMethods(List<CoerceMethod> coerceMethods) {
        this.coerceMethods = coerceMethods;
    }
    
    public IInitialisator getInitialisatorFrom(IType type) 
    throws AmbiguityException {
        return Initialisator.getInitialisatorFrom(this, type);
    }
    public IDeclarator getInitialisationDeclaratorFrom(IType type) throws AmbiguityException {
        IInitialisator initialisator 
            = InitialisatorMethod.getInitialisatorMethodFrom(this, type);
        if (initialisator == null)
            return null;
        return new AssignmentInitialisator(initialisator);
    }
    public IDeclarator getDeclarator() throws AmbiguityException {
        IInitialisator initialisator 
            = InitialisatorMethod.getDeclaratorInitialisatorMethod(this);
        if (initialisator == null)
            initialisator = Constructor.getDeclaratorInitialisator(this);
        if (initialisator == null)
            return null;
        return new AssignmentInitialisator(initialisator);
    }
    
    public Field getField(String name) throws NoSuchFieldException {
        return new Field(this, name);
    }
    
    public Set<Method> getMethods(String id) {
        return Method.getMethods(this, id);
    }
    
    public Comparison compareTo(IType other) {
        return Type.compare(this, other);
    }
    
    public String toTypeString() {
        StringBuilder result = new StringBuilder();
        result.append(getRawType().getCanonicalName());
        if (hasTypeParameters()) {
            result.append('<');
            result.append(getTypeParameterAt(0).toTypeString());
            final int nbTypeParameters = getNbTypeParameters();
            for (int i = 1; i < nbTypeParameters; i++) {
                result.append(',');
                result.append(getTypeParameterAt(i).toTypeString());            
            }
            result.append('>');
        }
        
        return result.toString();
    }    
    
    public boolean isParameterizable() {
        return getNbTypeVariables() > 0;
    }
    
    public static int getNbParameters(Class<?> rawType) {
        return rawType.getTypeParameters().length;
    }
    
    public static boolean isParameterizable(Class<?> rawType) {
        return  getNbParameters(rawType) > 0;
    }
    
    public Constructor[] getConstructors() {
        final java.lang.reflect.Constructor[] constructors = getRawType().getConstructors();
        final Constructor[] result = new Constructor[constructors.length];
        for (int i = 0; i < constructors.length; i++)
            result[i] = new Constructor(this, constructors[i]);
        return result;
    }
    
    public boolean isHashObservable() {
        return HashObservable.class.isAssignableFrom(getRawType());
    }
    public boolean isObservable() {
        return Observable.class.isAssignableFrom(getRawType());
    }
    
}