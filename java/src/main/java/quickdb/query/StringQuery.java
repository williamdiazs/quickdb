package quickdb.query;

import quickdb.annotation.Column;
import quickdb.annotation.Parent;
import quickdb.annotation.Properties;
import quickdb.exception.QueryException;
import quickdb.reflection.ReflectionUtilities;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author Diego Sarmentero
 */
public final class StringQuery {

    private static final String regObj = "(\\w+\\.\\w+)+|\\w+";
    private static final String tokens = "[=!<>\\&\\|]|( LIKE )|( like )";
    private static final ReflectionUtilities ref = new ReflectionUtilities();

    /**
     * Return a String with the SQL Representation of the query expressed in
     * an Object Oriented way in the Condition containing the fields of the Object
     * @param Object to be evaluated
     * @param condition [String] representing the Query in an Object Oriented way
     * @return String with the SQL Representation of the Query
     */
    public static String parse(Class clazz, String condition) {
        //Replace and, or
        Pattern p = Pattern.compile("( AND )|( \\&{2} )", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(condition);
        condition = m.replaceAll(" & ");
        p = Pattern.compile("( OR )|( \\|{2} )", Pattern.CASE_INSENSITIVE);
        m = p.matcher(condition);
        condition = m.replaceAll(" | ");

        String columns = StringQuery.mainData(clazz);
        String[] eval = condition.split(StringQuery.tokens);

        ArrayList<String> array = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        p = Pattern.compile(StringQuery.regObj);
        for (int i = 0; i < eval.length; i += 2) {
            m = p.matcher(eval[i].trim());
            if (m.matches()) {
                array.add(StringQuery.selectData(clazz, eval[i].trim()));
                values.add(eval[i + 1].trim());
            } else {
                throw new QueryException();
            }
        }

        ArrayList<String> operators = new ArrayList<String>();
        Pattern pToken = Pattern.compile(StringQuery.tokens);
        Matcher mToken = pToken.matcher(condition);
        while (mToken.find()) {
            operators.add(mToken.group());
        }

        StringBuilder tables = new StringBuilder();
        StringBuilder where = new StringBuilder();
        int size = array.size();
        int indexOper = 0;
        tables.append(array.get(0).substring(0, array.get(0).indexOf("|")));
        for (int i = 0; i < size; i++) {
            String[] parts = array.get(i).split("\\|");

            for (int q = 1; q < parts.length - 2; q += 2) {
                if (tables.indexOf(parts[q]) == -1) {
                    tables.append(" JOIN " + parts[q + 1] + " ON " + parts[q]);
                }
            }
            if (i > 0) {
                where.append(" " + StringQuery.separator(operators.get(indexOper++)) + " ");
            }
            where.append(parts[parts.length - 1] + operators.get(indexOper++) + values.get(i));
        }

        return "SELECT " + columns + " FROM " + tables.toString() + " WHERE " + where.toString();
    }

    private static String separator(String evaluator) {
        if (evaluator.trim().equalsIgnoreCase("&")) {
            return "AND";
        } else if (evaluator.trim().equalsIgnoreCase("|")) {
            return "OR";
        } else {
            throw new QueryException();
        }
    }

    private static String selectData(Class clazz, String evaluator) {
        String data;

        String[] refer = evaluator.split("\\.");

        data = StringQuery.evaluatorData(clazz, refer, 0);

        return data;
    }

    static int inheritedAttribute(Class clazz, String attr) {
        int inher = 0;
        boolean inherit = StringQuery.ref.hasParent(clazz);
        
        if (inherit) {
            Class value = clazz.getSuperclass();
            Field[] fields = value.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equalsIgnoreCase(attr)) {
                    inher++;
                }
            }
            if ((inher == 0)) {
                inher = StringQuery.inheritedAttribute(value, attr);
                if (inher > 0) {
                    inher++;
                }
            }
        }

        return inher;
    }

    private static String evaluatorData(Class clazz, String[] ref, int index) {
        StringBuilder statement = new StringBuilder();
        String table = StringQuery.ref.readTableName(clazz);

        Class value = StringQuery.obtainReference(clazz, ref[index]);
        String tableRef = StringQuery.ref.readTableName(value);
        String colMain = StringQuery.columnName(clazz, ref[index]);
        String colId = StringQuery.tableIndex(value);

        try {
            int inherit = 0;
            if(!table.equalsIgnoreCase(tableRef)){
                inherit = StringQuery.inheritedAttribute(clazz, ref[index]);
            }
            if (index < ref.length - 1) {
                if (inherit == 0) {
                    statement.append(table + "|" + table + "." + colMain + " = " + tableRef + "." + colId + "|");
                    index++;
                    statement.append(StringQuery.evaluatorData(value, ref, index));
                }
            } else {
                if (inherit == 0) {
                    colMain = StringQuery.columnName(clazz, ref[index]);

                    statement.append(table + "|" + table + "." + colMain + " |");
                }
            }

            if ((inherit > 0)) {
                Class inher = clazz;
                for (int i = 0; i < inherit; i++) {
                    table = StringQuery.ref.readTableName(inher);
                    value = inher.getSuperclass();
                    tableRef = StringQuery.ref.readTableName(value);
                    colId = StringQuery.tableIndex(value);
                    statement.append(table + "|" + table + ".parent_id = " + tableRef + "." + colId + "|");
                    inher = value;
                }
                statement.append(StringQuery.evaluatorData(value, ref, index));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return statement.toString();
    }

    static Class obtainReference(Class clazz, String reference) {
        try {
            Method getter = StringQuery.ref.obtainGetter(clazz, reference);
            if(getter.getReturnType().isPrimitive() || 
                    StringQuery.ref.checkPrimitivesExtended(getter.getReturnType(), null)){
                return clazz;
            }
            Class value = getter.getReturnType();
            return value;
        } catch (Exception e) {
            int inher = StringQuery.inheritedAttribute(clazz, reference);
            if (inher > 0) {
                Class value = clazz.getSuperclass();
                for (int i = 0; i < inher - 1; i++) {
                    value = value.getSuperclass();
                }
                return StringQuery.obtainReference(value, reference);
            }
        }
        return clazz;
    }

    static Object[] obtainReferenceByReturn(Class clazz, Class ret) {
        Object[] result = new Object[2];
        String field = "";
        Class c = clazz;
        boolean found = false;
        while(StringQuery.ref.hasParent(c)){
            Field[] fields = c.getDeclaredFields();
            for(Field f : fields){
                if(f.getType() == ret){
                    field = StringQuery.columnName(c, f.getName());
                    found = true;
                }
            }
            if(found) break;
            
            c = c.getSuperclass();
        }
        if(!found){
            Field[] fields = c.getDeclaredFields();
            for(Field f : fields){
                if(f.getType() == ret){
                    field = StringQuery.columnName(c, f.getName());
                    found = true;
                }
            }
        }
        result[0] = c;
        result[1] = field;

        return result;
    }

    static String mainData(Class clazz) {
        StringBuilder mainCol = new StringBuilder();

        String table = StringQuery.ref.readTableName(clazz);

        //Attributes
        Field fields[] = clazz.getDeclaredFields();
        boolean ignore = false;
        for (Field f : fields) {
            String fieldName = f.getName();
            String name = fieldName;
            Annotation ann[] = f.getDeclaredAnnotations();

            if (ann.length == 0) {
                name = fieldName;
            } else {
                for (Annotation a : ann) {
                    if (!(a instanceof Column)) {
                        continue;
                    }

                    if (((Column) a).name().length() != 0) {
                        name = ((Column) a).name();
                    }
                    ignore = ((Column) a).ignore();
                    break;
                }
            }

            if (!ignore && !StringQuery.ref.implementsCollection(clazz, fieldName)) {
                mainCol.append(table + "." + name + ", ");
            }
        }
        mainCol.delete(mainCol.length() - 2, mainCol.length() - 1);

        Annotation entity[] = clazz.getDeclaredAnnotations();
        if (entity.length > 0) {
            for (int i = 0; i < entity.length; i++) {
                if (entity[i] instanceof Parent) {
                    mainCol.append(", " + table + ".parent_id");
                    break;
                }
            }
        } else if (clazz.getPackage() ==
                clazz.getSuperclass().getPackage()) {
            mainCol.append(", " + table + ".parent_id");
        }

        return mainCol.toString();
    }

    static String columnName(Class clazz, String field) {
        String name = field;
        try {
            Field f = clazz.getDeclaredField(field);
            Annotation[] ann = f.getDeclaredAnnotations();
            for (Annotation a : ann) {
                if ((a instanceof Column) &&
                        (((Column) a).name().length() != 0)) {
                    name = ((Column) a).name();
                }
            }
        } catch (Exception e) {
            int inher = StringQuery.inheritedAttribute(clazz, field);
            if (inher > 0) {
                Class value = clazz.getSuperclass();
                for (int i = 0; i < inher - 1; i++) {
                    value = value.getSuperclass();
                }
                return StringQuery.columnName(value, field);
            }
            throw new QueryException();
        }

        return name;
    }

    static String tableIndex(Class clazz) {
        String index = "id";
        try {
            Field[] fields = clazz.getDeclaredFields();
            Annotation[] ann = fields[0].getDeclaredAnnotations();
            for (Annotation a : ann) {
                if ((a instanceof Column) &&
                        ((Column) a).type() == Properties.TYPES.PRIMARYKEY) {
                    if (((Column) a).name().length() == 0) {
                        index = fields[0].getName();
                        break;
                    } else {
                        index = ((Column) a).name();
                        break;
                    }
                }
            }
            if (ann.length == 0) {
                index = "id";
            }
        } catch (Exception e) {
            throw new QueryException();
        }

        return index;
    }
}
