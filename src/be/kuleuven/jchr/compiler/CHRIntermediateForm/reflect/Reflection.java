package be.kuleuven.jchr.compiler.CHRIntermediateForm.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.IType;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.TypeFactory;
import be.kuleuven.jchr.util.Arrays;



/**
 * @author Peter Van Weert
 */
public abstract class Reflection {
    private Reflection() {/* not instantiatable */ }
    
    public static IType reflect(Mirror mirror, Type type)
    throws IllegalArgumentException, IndexOutOfBoundsException
    {     
        if (type instanceof TypeVariable)
            return reflect(mirror, (TypeVariable<?>)type);
        
        if (type instanceof Class)
            return TypeFactory.getInstance((Class<?>)type);
        
        if (type instanceof ParameterizedType) {
            GenericType result = GenericType.getInstance(
                (Class<?>)(((ParameterizedType)type).getRawType())
            );
            
            if (result.isParameterizable())
                for (Type t : ((ParameterizedType)type).getActualTypeArguments())                
                    result.addTypeParameter(reflect(mirror, t));
                
            return result;
        }
        
        throw new RuntimeException("Unsupported reflection?");
    }
    
    public static IType reflect(Mirror mirror, TypeVariable<?> typeVariable) 
    throws IllegalArgumentException, IndexOutOfBoundsException {
        
        CacheKey key = new CacheKey(typeVariable, mirror);        
        IType result = cache.get(key);
        if (result != null) return result;
            
        final Class<?> declaror = (Class<?>)typeVariable.getGenericDeclaration();

        ArrayList<Type> chain = new ArrayList<Type>();        
        Class<?> current = mirror.getRawType();
        Type currentT = null;
        
        final boolean testInterfaces = declaror.isInterface() && !current.isInterface();
        
        // Mag normaal niet gebeuren, ...
//        if (! declaror.isAssignableFrom(current))
//            throw new RuntimeException("Reflecting on unknown TypeVariable: " 
//                + typeVariable
//                + " (base is " + base 
//                + ", while declaror is " + declaror 
//                + ")");
        
        // afdalen in inheritance lattice ...:
        if (!current.isInterface())
            while (! (testInterfaces? testInterfaces(current, declaror): current.equals(declaror) )) {
                currentT = current.getGenericSuperclass();
                
                if (currentT instanceof ParameterizedType)
                    current = (Class)((ParameterizedType)currentT).getRawType();
                else
                    current = (Class)currentT;
                
                chain.add(currentT);
            }
        
        if (testInterfaces) {
            final int index = getInterfaceIndex(current, declaror);
            currentT = current.getGenericInterfaces()[index];
            current = current.getInterfaces()[index];            
            chain.add(currentT);
        }   // hierna is current een interface!

        if (current.isInterface()) { // verder afdalen binnen interfaces:
            int index;
            while (!current.equals(declaror)) {
                index = getInterfaceIndex(current, declaror);
                currentT = current.getGenericInterfaces()[index];
                
                if (currentT instanceof ParameterizedType)
                    current = (Class)((ParameterizedType)currentT).getRawType();
                else
                    current = (Class)currentT;
                
                chain.add(currentT);
            }
        }
        
        int typeParameterIndex 
        	= Arrays.firstIndexOf(declaror.getTypeParameters(), typeVariable);
        int chainIndex = chain.size() - 1;
        Type t = null;
        if (chainIndex >= 0)
            t = ((ParameterizedType)currentT).getActualTypeArguments()[typeParameterIndex];
            
        // terug opklimmen 
        while (chainIndex > 0 && t instanceof TypeVariable) {
            currentT = chain.get(--chainIndex);
            typeParameterIndex = Arrays.firstIndexOf(
                ((Class)((ParameterizedType)currentT).getRawType()).getTypeParameters(),
                (TypeVariable)t
            );
            t = ((ParameterizedType)currentT).getActualTypeArguments()[typeParameterIndex];
        }
        
        if (chainIndex < 0)
            result = mirror.getTypeParameterAt(typeParameterIndex);
        else
            result = reflect(mirror, t);
        
        cache.put(key, result);
        
        return result;
    }
    
    private static boolean testInterfaces(Class<?> current, Class<?> declaror) {
        return getInterfaceIndex(current, declaror) >= 0;
    }
    
    private static int getInterfaceIndex(Class<?> current, Class<?> declaror) {
        final Class<?>[] interfaces = current.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (declaror.isAssignableFrom(interfaces[i]))
                return i;
        }
        return -1;
    }
    
    private transient static Map<CacheKey, IType> cache;
    
    static {
        cache = new HashMap<CacheKey, IType>();
    }
    
    protected static class CacheKey {
        private TypeVariable typeVariable;
        private Mirror base;
        
        public CacheKey(TypeVariable typeVariable, Mirror base) {
            this.typeVariable = typeVariable;
            this.base = base;
        }
        
        @Override
        public int hashCode() {
            // based on http://www.javapractices.com/Topic28.cjp

            // Volgende werkt niet vanwege bug in java 1.5 -code:
            // return 37 * (37 * 23 + typeVariable.hashCode()) + base.hashCode();
            return 37 * (37 * (37 * 23 
                    + typeVariable.getName().hashCode()) 
                    + typeVariable.getGenericDeclaration().hashCode())
                    + base.hashCode();
        }
        
        @Override
        public boolean equals(Object other) {
            return this.typeVariable.equals(((CacheKey)other).typeVariable)
            	&& this.base.equals(((CacheKey)other).base);
        }
    }
}