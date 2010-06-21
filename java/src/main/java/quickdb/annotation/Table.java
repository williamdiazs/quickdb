package quickdb.annotation;

import java.lang.annotation.*;

/**
 *
 * @author Diego Sarmentero
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table{

    String value() default "";

}
