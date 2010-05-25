package quickdb.db.dbms.firebird;

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
public class Firebird {

    public static boolean createTable(AdminBase admin, Object entity, 
            Object[] objects, EntityManager manager){
        
        StringBuilder statement = new StringBuilder();
        DataType dataType = new DataType();
        StringBuilder colDefinition = new StringBuilder();
        StringBuilder constraints = new StringBuilder();
        String primaryKey = manager.peekPrimaryKey();
        statement.append(String.format("CREATE TABLE " +
                "%s (", objects[0]));
        String close = String.format(", PRIMARY KEY (%s))", primaryKey);

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
                    statement.append(String.format("%s integer NOT NULL",
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
            colDefinition.append(", parent_id integer NOT NULL");
        }
        
        statement.append(colDefinition.toString());
        statement.append(constraints.toString());

        statement.append(close);

        admin.executeQuery(statement.toString());
        admin.executeQuery("CREATE GENERATOR gen_"+objects[0]+"_id;");
        admin.executeQuery("SET GENERATOR gen_"+objects[0]+"_id TO 0;");
        return admin.executeQuery("CREATE TRIGGER "+objects[0]+"_BI FOR "+objects[0]+" " +
                "ACTIVE BEFORE INSERT POSITION 0 " +
                "AS " +
                "BEGIN " +
                "if (NEW.ID is NULL) then NEW.ID = GEN_ID(GEN_"+objects[0]+"_ID, 1); " +
                "END ");
    }

}
