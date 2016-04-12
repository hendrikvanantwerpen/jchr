package be.kuleuven.jchr.compiler.CHRIntermediateForm.members;


import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.AbstractMethod;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.types.GenericType;

/**
 * @author Peter Van Weert
 */
public class Method extends AbstractMethod<Method> {

    public Method(java.lang.reflect.Method method) {
       super(method);
    }
  
    public Method(GenericType base, java.lang.reflect.Method method) {        
        super(base, method);
    }
    
    public AbstractMethodInvocation<Method> getInstance(IArguments arguments) {
        return new MethodInvocation<Method>(this, arguments);
    }
    
    public static Set<Method> getMethods(GenericType base, String id) {
        Set<Method> result = new HashSet<Method>();
        
        for (java.lang.reflect.Method method : base.getRawType().getMethods())
            if (method.getName().equals(id))
                result.add(new Method(base, method));
        
        return result;
    }
    
    public static Set<Method> getStaticMethods(Class<?> base, String id) {
        final Set<Method> result = new HashSet<Method>();
        
        final GenericType actualBase = 
            GenericType.getClassInstance(base);
        
        for (java.lang.reflect.Method method : base.getMethods())
            if (Modifier.isStatic(method.getModifiers()) && method.getName().equals(id))
                result.add(new Method(actualBase, method));
        
        return result;
    }
    
    public boolean haveToIgnoreImplicitArgument() {
        return isStatic();  // maakt niet zoveel uit denk ik?
    }
}
