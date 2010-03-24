package cat.quickdb.db.dbms.postgre;

import cat.quickdb.annotation.Column;
import cat.quickdb.annotation.ColumnDefinition;
import cat.quickdb.db.AdminBase;
import cat.quickdb.reflection.EntityManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class PostgreSQL {

    public static boolean createTable(AdminBase admin, Object entity,
            Object[] objects, EntityManager manager) {

        StringBuilder statement = new StringBuilder();
        DataTypePostgre dataType = new DataTypePostgre();
        StringBuilder colDefinition = new StringBuilder();
        StringBuilder constraints = new StringBuilder();
        String primaryKey = manager.peekPrimaryKey();
        statement.append(String.format("CREATE TABLE " +
                "%s (", admin.getConex().getSchema() + ".\"" + objects[0] + "\""));
        String close = String.format(", CONSTRAINT pk_" + objects[0] + "_" +
                primaryKey + " PRIMARY KEY (%s)) " +
                "WITH (OIDS=FALSE);", primaryKey);

        Object columns[] = null;
        if (entity.getClass().getDeclaredAnnotations().length != 0) {
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
                if (a instanceof ColumnDefinition) {
                    withAnnotation = true;
                }
                if (a instanceof Column) {
                    ignore = ((Column) a).ignore();
                    if (((Column) a).name().length() != 0) {
                        name = ((Column) a).name();
                    }
                }
            }

            if (ignore) {
                ignore = false;
                q--;
                continue;
            } else if (primary) {
                primary = false;
                if (withAnnotation) {
                    statement.append(String.format("%s DEFAULT nextval('" + objects[0] + "_seq')",
                            ((ColumnDefinedPostgre) columns[1]).toString()));
                } else {
                    statement.append(String.format("%s integer NOT NULL " +
                            "DEFAULT nextval('" + objects[0] + "_seq')",
                            primaryKey));
                }
                continue;
            } else if (!(objects[q] instanceof Object[]) ||
                    !name.equalsIgnoreCase(String.valueOf(((Object[]) objects[q])[0]))) {
                q--;
                continue;
            }

            if (withAnnotation) {
                colDefinition.append(String.format(", %s",
                        ((ColumnDefinedPostgre) columns[q]).toString()));
                if (((ColumnDefinedPostgre) columns[q]).hasConstraints()) {
                    constraints.append(String.format(", %s",
                            ((ColumnDefinedPostgre) columns[q]).obtainConstraints()));
                }
            } else {
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

        admin.executeQuery("CREATE SEQUENCE " + objects[0] + "_seq");
        return admin.executeQuery(statement.toString());
    }
}
