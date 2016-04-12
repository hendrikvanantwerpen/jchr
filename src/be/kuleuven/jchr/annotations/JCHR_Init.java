package be.kuleuven.jchr.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Peter Van Weert
 */
@Target( {CONSTRUCTOR, METHOD, TYPE} )
@Retention(RUNTIME)
public @interface JCHR_Init {
    String factoryClass() default "";
    int identifier() default -1;
}
