package quickdb.annotation;

import java.lang.annotation.*;

/**
 *
 * @author Diego Sarmentero
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validation {

    public static final String conditionURL = "www\\.(.+)\\.(.+)"; //IMPROVE
    public static final String conditionMail = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4}$";
    public static final String conditionSecurePassword = "^.*(?=.{8,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(^[a-zA-Z0-9@\\$=!:.#%]+$)";
    public static final String conditionNoEmpty = "((\\w)+(\\s)*(\\w)*)+";

    public static final int EQUAL = 0;
    public static final int LOWER = 1;
    public static final int GREATER = 2;
    public static final int EQUALORGREATER = 3;
    public static final int EQUALORLOWER = 4;

    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;
    /*public static final int HOUR = 3;
    public static final int MINUTE = 4;
    public static final int SECOND = 5;*/

    String[] conditionMatch() default "";

    int maxLength() default -1;

    int[] numeric() default 0;

    int[] date() default 0;

}
