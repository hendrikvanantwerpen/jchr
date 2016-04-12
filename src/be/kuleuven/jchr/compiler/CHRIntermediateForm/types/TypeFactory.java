package be.kuleuven.jchr.compiler.CHRIntermediateForm.types;

/**
 * @author Peter Van Weert
 */
public abstract class TypeFactory {

    private TypeFactory() {/* non-instantiatable FACADE */}
    
    public static IType getInstance(Class<?> someClass) {
        if (someClass.isPrimitive())
            return PrimitiveType.getInstance(someClass.getName());
        else 
            return GenericType.getInstance(someClass);
    }
}
