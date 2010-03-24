package cat.quickdb.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnDefinition {

    Definition.DATATYPE type() default Definition.DATATYPE.VARCHAR;

    int length() default -1;

    boolean notNull() default true;

    String defaultValue() default "";

    boolean autoIncrement() default false;

    boolean unique() default false;

    boolean primary() default false;

    Definition.COLUMN_FORMAT format() default Definition.COLUMN_FORMAT.DEFAULT;

    Definition.STORAGE storage() default Definition.STORAGE.DEFAULT;
}