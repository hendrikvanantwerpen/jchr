package be.kuleuven.jchr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter Van Weert
 */
public abstract class Annotations {

    public static Method getMarkedMethod(
        Class<?> someClass,
        Class<? extends Annotation> markerAnnotation
        ) {
        
        for (Method m : someClass.getMethods())
            if (m.isAnnotationPresent(markerAnnotation))                
                return m;            
        
        return null;
    }
    
    public static List<Method> getMarkedMethods(
        Class<?> someClass,
        Class<? extends Annotation> markerAnnotation
    	) {
        
        final List<Method> result = new ArrayList<Method>();
        
        for (Method m : someClass.getMethods())
            if (m.isAnnotationPresent(markerAnnotation))
                result.add(m);             
        
        return result;
    }
}