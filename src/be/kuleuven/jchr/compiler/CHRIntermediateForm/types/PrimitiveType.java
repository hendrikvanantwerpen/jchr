package be.kuleuven.jchr.compiler.CHRIntermediateForm.types;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo.EXACT_MATCH;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo.NO_MATCH;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguityException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IDeclarator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.init.IInitialisator;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.CoerceMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.matching.MatchingInfo;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Field;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.Method;
import be.kuleuven.jchr.util.Arrays;



/**
 * @author Peter Van Weert
 */
public class PrimitiveType extends Type {
    private PrimitiveType[] assignableTos;
    
    private CoerceMethod coerceMethod;
    
    private Class<?> primitiveType;
    
    private GenericType wrapperType;
    
    private PrimitiveType() { /* FACTORY */ }
    
//    private PrimitiveType(Class<?> wrapperClass, Class<?> primitiveType) {
//        this(wrapperClass, primitiveType, new PrimitiveType[] {});
//    }
    
    private PrimitiveType(Class<?> wrapperClass, Class<?> primitiveType, PrimitiveType... assignableTos) {        
        setAssignableTos(assignableTos);
        setWrapperType(GenericType.getNonParameterizableInstance(wrapperClass));
        setPrimitiveType(primitiveType);
    }
    
    public final static PrimitiveType
		BOOLEAN_TYPE,
    	BYTE_TYPE, 
    	SHORT_TYPE, 
    	INT_TYPE, 
    	CHAR_TYPE, 
    	LONG_TYPE, 
    	FLOAT_TYPE, 
    	DOUBLE_TYPE;
    
    static {
        BOOLEAN_TYPE  = new PrimitiveType(
            Boolean.class, Boolean.TYPE
        );
        
        DOUBLE_TYPE  = new PrimitiveType(
            Double.class, Double.TYPE
        );
        
        FLOAT_TYPE  = new PrimitiveType(
            Float.class, Float.TYPE, 
            DOUBLE_TYPE
        );
        
        LONG_TYPE  = new PrimitiveType(Long.class, Long.TYPE,
            FLOAT_TYPE, 
            DOUBLE_TYPE 
    	);
        
        INT_TYPE  = new PrimitiveType(Integer.class, Integer.TYPE,
            LONG_TYPE, 
            FLOAT_TYPE, 
            DOUBLE_TYPE 
    	);
        
        CHAR_TYPE  = new PrimitiveType(Character.class, Character.TYPE,
            INT_TYPE, 
            LONG_TYPE, 
            FLOAT_TYPE, 
            DOUBLE_TYPE 
    	);
        
        SHORT_TYPE  = new PrimitiveType(Short.class, Short.TYPE,
            INT_TYPE, 
            LONG_TYPE, 
            FLOAT_TYPE, 
            DOUBLE_TYPE 
    	);

        BYTE_TYPE  = new PrimitiveType(Byte.class, Byte.TYPE,
            SHORT_TYPE,
            INT_TYPE,
            LONG_TYPE, 
            FLOAT_TYPE, 
            DOUBLE_TYPE 
    	);
    }
    
    public boolean isHashObservable() {
        return false;
    }
    public boolean isObservable() {
        return false;
    }
    
    private static Map<String, PrimitiveType> primitiveTypes;    
    static {        
        init();
    }
    
    private static void init() {
        primitiveTypes = new HashMap<String, PrimitiveType>(8);
        primitiveTypes.put("boolean",   BOOLEAN_TYPE);
        primitiveTypes.put("byte"   ,   BYTE_TYPE);
        primitiveTypes.put("short"  ,   SHORT_TYPE);
        primitiveTypes.put("int"    ,   INT_TYPE);
        primitiveTypes.put("char"   ,   CHAR_TYPE);
        primitiveTypes.put("long"   ,   LONG_TYPE);
        primitiveTypes.put("float"  ,   FLOAT_TYPE);
        primitiveTypes.put("double" ,   DOUBLE_TYPE);
    }
    
    public static boolean isPrimitive(IType type) {
        return primitiveTypes.containsValue(type);
    }
    
    public static boolean isPrimitiveType(String id) {
        return primitiveTypes.containsKey(id);
    }
    
    public static PrimitiveType getInstance(String id) {        
        return primitiveTypes.get(id);
    }

    public boolean isInterface() {
        return false;
    }
    public boolean isFixed() {
        return true;
    }
    
    public MatchingInfo isAssignableTo(IType type) {
        if (isDirectlyAssignableTo(type)) return EXACT_MATCH;
        
        if (! isPrimitive(type)) {
            MatchingInfo result = new MatchingInfo(); 
            try {
                result.initInitialisator(type.getInitialisatorFrom(this));
            } catch (AmbiguityException ae) {                
                result.setAmbiguous();
            }
            return result;
        }
        else return NO_MATCH;
    }
    
    public boolean isDirectlyAssignableTo(IType type) {
        return (type == this) || Arrays.identityContains(getAssignableTos(), type);
    }
    
    protected CoerceMethod getCoerceMethod() {
        return coerceMethod;
    }
    
    protected PrimitiveType[] getAssignableTos() {
        return assignableTos;
    }
    protected PrimitiveType getAssignableToAt(int index) {
        return getAssignableTos()[index];
    }
    protected int getNbAssignablesTos() {
        return getAssignableTos().length;
    }
    
    protected void setAssignableTos(PrimitiveType[] assignableTos) {
        this.assignableTos = assignableTos;
    }
    
    public List<CoerceMethod> getCoerceMethods() {
        return Collections.emptyList();
    }
    
    public IInitialisator getInitialisatorFrom(IType type) throws AmbiguityException {
        return null;
    }
    public IDeclarator getInitialisationDeclaratorFrom(IType type) throws AmbiguityException {
        return null;
    }
    public IDeclarator getDeclarator() {
        return null;
    }

    public Field getField(String name) throws NoSuchFieldException {
        throw new NoSuchFieldException("Primitive types don't have fields"); 
    }
    
    public Set<Method> getMethods(String id) {
        return Collections.emptySet();
    }
    
    public Class<?> getPrimitiveType() {
        return primitiveType;
    }
    protected void setPrimitiveType(Class<?> primitiveType) {
        this.primitiveType = primitiveType;
    }
    
    @Override
    public boolean equals(Object other) {
        return (this == other);
    }
    @Override
    public int hashCode() {
        return 37 * 23 + getPrimitiveType().hashCode();
    }
    @Override
    public String toString() {
        return getPrimitiveType().getName();
    }

    public GenericType[] getWrapperTypes() {
        final int nbAssignableTos = getNbAssignablesTos();
        final GenericType[] result = new GenericType[nbAssignableTos + 1];
        result[0] = wrapperType;
        for (int i = 0; i < nbAssignableTos; i++)
            result[i+1] = getAssignableToAt(i).getWrapperType();
        return result;
    }    
    protected GenericType getWrapperType() {
        return wrapperType;
    }
    protected void setWrapperType(GenericType wrapperType) {
        this.wrapperType = wrapperType;
    }
}