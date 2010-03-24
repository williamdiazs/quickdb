package cat.quickdb.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    String name() default "";

    Properties.TYPES type() default Properties.TYPES.PRIMITIVE;

    String collectionClass() default "";

    String getter() default "";

    String setter() default "";

    boolean ignore() default false;
}
