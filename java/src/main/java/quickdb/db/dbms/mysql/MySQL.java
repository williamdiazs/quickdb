package quickdb.db.dbms.mysql;

import quickdb.annotation.Column;
import quickdb.annotation.ColumnDefinition;
import quickdb.db.AdminBase;
import quickdb.reflection.EntityManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author Diego Sarmentero
 */
public class MySQL {

    public static boolean createTable(AdminBase admin, Object entity, 
            Object[] objects, EntityManager manager){
        
        StringBuilder statement = new StringBuilder();
        DataType dataType = new DataType();
        StringBuilder colDefinition = new StringBuilder();
        StringBuilder constraints = new StringBuilder();
        String primaryKey = manager.peekPrimaryKey();
        statement.append(String.format("CREATE TABLE IF NOT EXISTS " +
                "%s (", objects[0]));
        String close = String.format(", PRIMARY KEY (%s)) " +
                "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1",
                primaryKey);

        Object columns[] = null;
        if (entity.getClass().getDeclaredAnnotations().length != 0){
            columns = manager.mappingDefinition(admin, entity);
        }

        Field[] fields = entity.getClass().getDeclaredFields();
        boolean primary = true;
        int q = 1;
        for (int i = 0; i < fields.length; i++, q++) {
            boolean withAnnotation = false;
            boolean ignore = false;
            String name = fields[i].getName();
            Annotation annotations[] = fields[i].getDeclaredAnnotations();
            for (Annotation a : annotations) {
                if(a instanceof ColumnDefinition){
                    withAnnotation = true;
                }
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
                if(withAnnotation){
                    statement.append(String.format("%s",
                        ((ColumnDefined) columns[1]).toString()));
                }else{
                    statement.append(String.format("%s int(11) NOT NULL AUTO_INCREMENT",
                        primaryKey));
                }
                continue;
            }

            if(withAnnotation){
                colDefinition.append(String.format(", %s",
                    ((ColumnDefined) columns[q]).toString()));
                if (((ColumnDefined) columns[q]).hasConstraints()) {
                    constraints.append(String.format(", %s",
                            ((ColumnDefined) columns[q]).obtainConstraints()));
                }
            }else{
                Object obj[] = (Object[]) objects[q];
                statement.append(String.format(", %s %s NOT NULL",
                        obj[0], dataType.getDataType(obj[1].getClass(),
                        obj[1].toString().length())));
            }
        }

        if (objects[objects.length - 2] instanceof Object[] &&
                ((Object[]) objects[objects.length - 2])[0] == "parent_id") {
            colDefinition.append(", parent_id int(11) NOT NULL");
        }
        
        statement.append(colDefinition.toString());
        statement.append(constraints.toString());

        statement.append(close);

        return admin.executeQuery(statement.toString());
    }

}
