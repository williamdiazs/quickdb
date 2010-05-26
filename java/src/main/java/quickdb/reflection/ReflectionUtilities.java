package quickdb.reflection;

import quickdb.annotation.Column;
import quickdb.annotation.Parent;
import quickdb.annotation.Properties;
import quickdb.annotation.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author Diego Sarmentero
 */
public class ReflectionUtilities {

    /**
     * Return an ArrayList containing the name of the fields from this Object
     * that represent a Many-To-Many Relation with this object.
     * @param Object to be evalutated
     * @return ArrayList with String objects
     */
    public ArrayList<String> isMany2Many(Class clazz) {
        ArrayList<String> manys = new ArrayList<String>();
        //Obtain the Table Name from this Object
        String table1 = this.readTableName(clazz);

        Field fields[] = clazz.getDeclaredFields();
        for (Field f : fields) {
            Annotation ann = null;
            //Search if this field has the Column annotation
            Annotation annotations[] = f.getAnnotations();
            for (Annotation a : annotations) {
                if (a instanceof Column) {
                    ann = a;
                }
            }
            //Create an empty instance from the type of this field
            Class cla1 = f.getType();

            if (this.implementsCollection(cla1, ann)) {
                Class item = this.obtainItemCollectionType(clazz, f.getName());
                Field fields2[] = item.getDeclaredFields();
                for (Field f2 : fields2) {
                    Annotation ann2 = null;
                    Annotation annotations2[] = f2.getAnnotations();
                    for (Annotation a : annotations2) {
                        if (a instanceof Column) {
                            ann2 = a;
                        }
                    }

                    Class cla2 = f2.getType();
                    if (this.implementsCollection(cla2, ann2)) {
                        //Obtain the collection type from the second collection
                        Class secondItem = this.obtainItemCollectionType(item, f2.getName());
                        String table2 = this.readTableName(secondItem);
                        if (table1.equalsIgnoreCase(table2)) {
                            manys.add(f.getName());
                            break;
                        }
                    }
                }
            }

        }

        return manys;
    }

    /**
     * Return the Table Name representation from this Object
     * @param Object to be evaluated
     * @return a String with the Table Name
     */
    public String readTableName(Class clazz) {
        Annotation entity[] = clazz.getAnnotations();
        String entityName = clazz.getSimpleName();
        for (int i = 0; i < entity.length; i++) {
            if ((entity[i] instanceof Table) &&
                    ((Table) entity[i]).value().length() != 0) {
                entityName = ((Table) entity[i]).value();
            }
        }

        return entityName;
    }

    Class obtainItemCollectionType(Class clazz, String field) {
        String className = "";
        try{
            Field f = clazz.getDeclaredField(field);
            Annotation annotations[] = f.getAnnotations();
            for (Annotation a : annotations) {
                if (a instanceof Column) {
                    className = ((Column) a).collectionClass();
                }
            }
        }catch(Exception e){}
        String path = clazz.getName().substring(0,
                clazz.getName().lastIndexOf(".") + 1);

        if (className.length() == 0) {
            className = path + String.valueOf(field.charAt(0)).toUpperCase() +
                    field.substring(1);
        } else if (!className.contains(".")) {
            className = path + className;
        }

        Class objCollec = this.emptyInstance(className).getClass();

        return objCollec;
    }

    /**
     * Return an empty instance of the Object received by parameter or
     * the Class represented in the String.
     * @param (An Object) or (a String with the Class name)
     * @return An empty Object from the specified Class
     */
    public Object emptyInstance(Object object) {
        Object obj = null;
        try {
            Class clazz;
            if (object instanceof String) {
                clazz = Class.forName(String.valueOf(object));
            } else if(object instanceof Class){
                clazz = Class.forName(((Class) object).getName());
            }else{
                clazz = Class.forName(object.getClass().getName());
            }
            if (this.isInstanceOfDate(clazz)) {
                obj = this.manageTimeData(clazz, new java.sql.Timestamp(0));
            } else {
                for (java.lang.reflect.Constructor con : clazz.getConstructors()) {
                    if (con.getParameterTypes().length == 0) {
                        obj = con.newInstance();
                        break;
                    }else if(con.getParameterTypes().length == 1 &&
                            con.getParameterTypes()[0] == String.class){
                        obj = con.newInstance("0");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }

        return obj;
    }

    private boolean isInstanceOfDate(Class clazz) {
        if (clazz.getName().substring(clazz.getName().
                lastIndexOf(".") + 1).matches("Date|Time|Timestamp")) {
            return true;
        }
        return false;
    }

    boolean implementsCollection(Class value, Annotation ann) {
        boolean collec = false;
        if (ann == null) {
            for (Class inter : value.getInterfaces()) {
                if (inter.getName().equalsIgnoreCase("java.util.List") ||
                        inter.getName().equalsIgnoreCase("java.util.Collection")) {
                    collec = true;
                    break;
                }
            }
        } else {
            collec = ((((Column) ann).type() == Properties.TYPES.COLLECTION) ||
                    !(((Column) ann).collectionClass().equalsIgnoreCase("")));
        }

        return collec;
    }

    public boolean implementsCollection(Class value, String reference) {
        boolean collec = false;

        try {
            Field f = value.getDeclaredField(reference);
            Annotation[] ann = f.getAnnotations();
            if (ann.length == 0) {
                for (Class inter : f.getType().getInterfaces()) {
                    if (inter.getName().equalsIgnoreCase("java.util.List") ||
                            inter.getName().equalsIgnoreCase("java.util.Collection")) {
                        collec = true;
                        break;
                    }
                }
            } else {
                for (Annotation a : ann) {
                    if (a instanceof Column) {
                        collec = (((Column) a).type() == Properties.TYPES.COLLECTION ||
                                !((Column) a).collectionClass().equalsIgnoreCase(""));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return collec;
    }

    /**
     * Return a Method object that represent the getter method for
     * the specified field in this object
     * @param Object that contain the field
     * @param The field to obtain the Getter Method
     * @return Method Object [java.lang.reflect.Method]
     */
    public Method obtainGetter(Class object, String field) {
        try {
            Field f = object.getDeclaredField(field);
            Annotation ann = null;
            for (Annotation a : f.getAnnotations()) {
                if (a instanceof Column) {
                    ann = a;
                    break;
                }
            }
            String getterName = "get" +
                    String.valueOf(field.charAt(0)).toUpperCase() +
                    field.substring(1);
            if (ann != null && (((Column) ann).getter().length() != 0)) {
                getterName = ((Column) ann).getter();
            }

            Method getter = object.getMethod(getterName);
            return getter;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return a Method object that represent the setter method for
     * the specified field in this object
     * @param Object that contain the field
     * @param The field to obtain the Setter Method
     * @return Method Object [java.lang.reflect.Method]
     */
    public Method obtainSetter(Class object, String field) {
        try {
            Field f = object.getDeclaredField(field);
            Annotation ann = null;
            for (Annotation a : f.getAnnotations()) {
                if (a instanceof Column) {
                    ann = a;
                    break;
                }
            }
            String setterName = "set" +
                    String.valueOf(field.charAt(0)).toUpperCase() +
                    field.substring(1);
            if (ann != null && (((Column) ann).setter().length() != 0)) {
                setterName = ((Column) ann).setter();
            }

            Method setter = object.getMethod(setterName, f.getType());
            return setter;
        } catch (Exception e) {
            return null;
        }
    }

    String checkIndex(Class clazz) {
        Field fieldsForeign[] = clazz.getDeclaredFields();

        for (int q = 0; q < fieldsForeign.length; q++) {
            Annotation annForeign[] = fieldsForeign[q].getAnnotations();

            if (annForeign.length > 0) {
                for (Annotation ann : annForeign) {
                    if (((Column) ann).type() ==
                            Properties.TYPES.PRIMARYKEY) {
                        return fieldsForeign[q].getName();
                    }
                }
            }
        }

        return "id";
    }

    public boolean checkPrimitivesExtended(Class clazz, Annotation a) {
        if (a == null) {
            if (clazz == Integer.class ||
                    clazz == Byte.class ||
                    clazz == Short.class ||
                    clazz == Long.class ||
                    clazz == Float.class ||
                    clazz == Double.class ||
                    clazz == Boolean.class ||
                    clazz == String.class ||
                    clazz == Date.class) {
                return true;
            }
        } else {
            if (((Column) a).type() != Properties.TYPES.FOREIGNKEY) {
                return true;
            }
        }

        return false;
    }

    Object manageTimeData(Class value,
            java.sql.Timestamp time) throws Exception {
        Object obj = null;
        try {
            for (java.lang.reflect.Constructor constructor : value.getConstructors()) {
                if (constructor.getParameterTypes().length == 1 &&
                        constructor.getParameterTypes()[0].getName().equalsIgnoreCase("long")) {
                    obj = constructor.newInstance(time.getTime());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public boolean hasParent(Class clazz) {
        boolean value = false;
        Annotation entity[] = clazz.getAnnotations();
        for (int i = 0; i < entity.length; i++) {
            if (entity[i] instanceof Parent) {
                value = true;
            }
        }
        if (entity.length == 0 &&
                clazz.getSuperclass().getPackage() ==
                clazz.getPackage()) {
            value = true;
        }

        return value;
    }
}
