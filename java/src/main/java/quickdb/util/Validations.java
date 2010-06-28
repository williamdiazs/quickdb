package quickdb.util;

import quickdb.annotation.Validation;
import quickdb.reflection.ReflectionUtilities;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Diego Sarmentero
 */
public class Validations {

    public static boolean isValidField(Object obj, String f,
            Validation ann, ReflectionUtilities reflec){
        Method getter = reflec.obtainGetter(obj.getClass(), f);
        Object fieldValue = null;
        try{
            fieldValue = getter.invoke(obj, new Object[0]);
        }catch(Exception e){}
        boolean value = true;

        if(ann.maxLength() != -1){
            value &= ( String.valueOf(fieldValue).length() <= ann.maxLength() );
        }
        if(ann.conditionMatch().length != 0 &&
                !ann.conditionMatch()[0].equalsIgnoreCase("")){
            for(String s : ann.conditionMatch()){
                Pattern p = Pattern.compile(s);
                Matcher m = p.matcher(String.valueOf(fieldValue));
                value &= m.matches();
            }
        }
        if(ann.numeric().length > 1){
            double val = Double.parseDouble(String.valueOf(fieldValue));
            int[] numeric = ann.numeric();
            for(int i = 0; i < numeric.length; i += 2){
                value &= Validations.checkCondition(numeric[i], numeric[i+1], val);
            }
        }
        if(ann.date().length > 2){
            if(fieldValue instanceof Date){
                Date date = (Date) fieldValue;
                int[] dates = ann.date();
                for(int i = 0; i < dates.length; i += 3){
                    int check = dates[i+2];
                    int val = 0;
                    switch(dates[i]){
                        //IMPROVE (erasa deprecated implementation)
                        case Validation.YEAR:
                            val = date.getYear() + 1900;break;
                        case Validation.MONTH:
                            val = date.getMonth() + 1;break;
                        case Validation.DAY:
                            val = date.getDate();break;
                        /*case Validation.HOUR:
                            val = date.getHours();break;
                        case Validation.MINUTE:
                            val = date.getMinutes();break;
                        case Validation.SECOND:
                            val = date.getSeconds();break;*/
                    }
                    value &= Validations.checkCondition(dates[i+1], check, val);
                }
            }
        }

        return value;
    }

    private static boolean checkCondition(int op, double check, double val){
        boolean value = false;
        switch(op){
            case Validation.EQUAL:
                value = (val == check);break;
            case Validation.EQUALORGREATER:
                value = (val >= check);break;
            case Validation.EQUALORLOWER:
                value = (val <= check);break;
            case Validation.GREATER:
                value = (val > check);break;
            case Validation.LOWER:
                value = (val < check);break;
        }

        return value;
    }

}
