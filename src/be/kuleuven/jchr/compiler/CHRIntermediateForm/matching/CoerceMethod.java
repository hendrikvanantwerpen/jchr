package be.kuleuven.jchr.compiler.CHRIntermediateForm.matching;

import static be.kuleuven.jchr.util.comparing.Comparison.EQUAL;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.jchr.annotations.JCHR_Coerce;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.BuiltInMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.Arguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.members.MethodInvocation;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;
import be.kuleuven.jchr.util.Annotations;
import be.kuleuven.jchr.util.comparing.Comparison;



/**
 * @author Peter Van Weert
 */
public class CoerceMethod extends BuiltInMethod<CoerceMethod> {
    private static List<CoerceMethod> numberCoerceMethods;
    
    protected CoerceMethod(Method method) {
        super(method);        
    }
    
    public CoerceMethod(GenericType base, Method method) {
        super(base, method);
    }
        
    public static List<CoerceMethod> getCoerceMethods(GenericType base) {
        if (base.isImmutableJavaWrapper())
            return getWrapperCoerceMethods(base);
        else {
            List<CoerceMethod> result = new ArrayList<CoerceMethod>();        
            for (Method method : Annotations.getMarkedMethods(base.getRawType(), JCHR_Coerce.class))
                result.add(new CoerceMethod(base, method));
            return result;
        }
    }
    
    public static List<CoerceMethod> getWrapperCoerceMethods(GenericType base) {
        try {
            if (base.isImmutableNumberWrapper())
                return getNumberWrapperCoerceMethods();
            else if (base == GenericType.getNonParameterizableInstance(Boolean.class))
                return Collections.singletonList(new CoerceMethod(
                    GenericType.getNonParameterizableInstance(Boolean.class),    
                    Boolean.class.getMethod("booleanValue", (Class[])null))
                );
            else if (base == GenericType.getNonParameterizableInstance(Character.class))
                return Collections.singletonList(new CoerceMethod(
                    GenericType.getNonParameterizableInstance(Character.class),
                    Character.class.getMethod("charValue", (Class[])null))
                );
            else if (base == GenericType.getNonParameterizableInstance(String.class))
                return Collections.emptyList();
            
        } catch (NoSuchMethodException e) {
            // NOP
        }
        
        throw new RuntimeException();
    }
            
    public static List<CoerceMethod> getNumberWrapperCoerceMethods() {
        if (numberCoerceMethods == null) {
            final Method[] methods = 
                Number.class.getDeclaredMethods();
            numberCoerceMethods = 
                new ArrayList<CoerceMethod>(methods.length);
            GenericType Number = GenericType.getInstance(Number.class);
            for (Method method : methods)
                numberCoerceMethods.add(new CoerceMethod(Number, method));
        }
        return numberCoerceMethods;
    }
    
    public Comparison compareTo(CoerceMethod other) {
        return this.getReturnType().compareTo(other.getReturnType());
    }
    
    public static Comparison compare(List<CoerceMethod> one, int previous, CoerceMethod other) {
        Comparison comparison = one.get(0).compareTo(other);
        if (comparison == EQUAL)
            return Comparison.get(one.size() - ++previous);
        else
            return comparison;
    }
    
    public static Comparison compare(List<CoerceMethod> one, List<CoerceMethod> other) {
        Comparison comparison = one.get(0).compareTo(other.get(0));
        if (comparison == EQUAL)
            return Comparison.get(one.size() - other.size());
        else
            return comparison;
    }
    
    public static Comparison compare(List<CoerceMethod> one, CoerceMethod other) {
        return compare(one, 0, other);
    }
    
    public boolean isValidCoerceMethod() {
        return getArity() == (isStatic()? 2 : 1) /* vergeet implicit argument niet! */ 
            && ! getMethod().getReturnType().equals(Void.TYPE);         
    }
    
    public MethodInvocation<CoerceMethod> getInstance(IArgument argument) {
        final IArguments arguments 
            = new Arguments(hasDefaultImplicitArgument()? 2 : 1);
        arguments.addArgument(argument);
        setImplicitArgumentOf(arguments);
        return getInstance(arguments);
    }
    
    public MethodInvocation<CoerceMethod> getInstance(IArguments arguments) {
        return new MethodInvocation<CoerceMethod>(this, arguments);
    }
    
}
