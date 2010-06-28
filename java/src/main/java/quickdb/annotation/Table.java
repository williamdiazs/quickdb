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
    boolean cache() default false;
    boolean cacheUpdate() default false;
    String[] before() default "";
    String[] after() default "";

}
