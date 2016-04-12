package be.kuleuven.jchr.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peter Van Weert
 */
@Target( {METHOD, CONSTRUCTOR, TYPE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface JCHR_Declare {
    String factoryClass() default "";
}