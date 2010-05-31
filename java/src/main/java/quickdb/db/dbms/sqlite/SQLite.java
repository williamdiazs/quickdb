package quickdb.db.dbms.sqlite;

import quickdb.annotation.Column;
import quickdb.db.AdminBase;
import quickdb.reflection.EntityManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author Diego Sarmentero
 */
public class SQLite {

    public static boolean createTable(AdminBase admin, Object entity,
            Object[] objects, EntityManager manager){

        StringBuilder statement = new StringBuilder();
        String primaryKey = String.valueOf(((Object[])objects[1])[0]);
        statement.append(String.format("CREATE TABLE IF NOT EXISTS " +
                "%s (", objects[0]));

        Field[] fields = entity.getClass().getDeclaredFields();
        boolean primary = true;
        int q = 1;
        for (int i = 0; i < fields.length; i++, q++) {
            boolean ignore = false;
            String name = fields[i].getName();
            Annotation annotations[] = fields[i].getDeclaredAnnotations();
            for (Annotation a : annotations) {
                if (a instanceof Column) {
                    ignore = ((Column) a).ignore();
                    if(((Column)a).name().length() != 0){
                        name = ((Column)a).name();
                    }
                }
            }

            if(ignore){
                ignore = false;
                q--;
                continue;
            }else if(!(objects[q] instanceof Object[]) ||
                    !name.equalsIgnoreCase(String.valueOf(((Object[]) objects[q])[0]))){
                q--;
                continue;
            }else if(primary){
                primary = false;
                statement.append(String.format("%s INTEGER PRIMARY KEY",
                        primaryKey));
                continue;
            }

            statement.append(String.format(", %s", name));
        }

        if (objects[objects.length - 2] instanceof Object[] &&
                ((Object[]) objects[objects.length - 2])[0] == "parent_id") {
            statement.append(", parent_id");
        }

        statement.append(")");

        return admin.executeQuery(statement.toString());
    }

}
