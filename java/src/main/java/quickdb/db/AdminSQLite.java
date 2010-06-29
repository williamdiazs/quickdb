package quickdb.db;

import quickdb.reflection.EntityManager;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Diego Sarmentero
 */
public class AdminSQLite extends AdminBase {

    AdminSQLite(DATABASE db, String... args) {
        super(db, args);
    }

    @Override
    protected int saveProcess(ArrayList array, Object object) {
        int index = -1;
        String table = "";
        int i = 1;
        try {
            if (this.forceTable) {
                table = tableForcedName;
                array.set(0, table);
            } else {
                table = String.valueOf(array.get(0));
            }

            ((Object[]) array.get(1))[1] = "NULL";
            StringBuilder insert = new StringBuilder("INSERT INTO " +
                    table + "(");
            StringBuilder values = new StringBuilder("VALUES(");

            for (; i < array.size() - 1; i++) {
                Object[] obj = (Object[]) array.get(i);

                String column = (String) obj[0];
                Object value = obj[1];
                if (i > 1) {
                    insert.append(", ");
                    values.append(", ");
                }

                insert.append(column);
                if ((i > 1) && ((value instanceof String) || (value instanceof Date))) {
                    values.append("'" + value + "'");
                } else {
                    values.append(value.toString());
                }
            }
            insert.append(")");
            values.append(")");

            index = this.conex.insertRow(insert.toString() + " " + values.toString());

            if (this.getCollection()) {
                this.saveMany2Many(((String) array.get(0)), true, index);
            }
        } catch (SQLException ex) {
            int value = -1;
            try {
                this.conex.getConnection().setAutoCommit(true);
                if (!this.checkTableExist(table)) {
                    if (this.createTable(object, array.toArray())) {
                        value = this.saveProcess(array, object);
                    }
                }
            } catch (Exception e) {
            }
            return value;
        } catch (Exception e) {
            conex.connectMySQL();
            try {
                conex.cancelTransaction();
            } catch (Exception ex) {
                return -1;
            }
        }

        return index;
    }

    @Override
    protected boolean modifyProcess(Object object) {
        ArrayList array = this.manager.entity2Array(this, object,
                EntityManager.OPERATION.MODIFY);

        try {
            StringBuilder update = new StringBuilder("UPDATE " +
                    ((String) array.get(0)) + " SET ");

            for (int i = 1; i < array.size() - 1; i++) {
                Object[] obj = (Object[]) array.get(i);

                String column = (String) obj[0];
                Object value = obj[1];
                if (i > 1) {
                    update.append(", ");
                }

                update.append(column + " = ");
                if ((i > 1) && ((value instanceof String) || (value instanceof Date))) {
                    update.append("'" + value + "'");
                } else {
                    update.append(value.toString());
                }
            }
            update.append(" WHERE " + String.valueOf(array.get(array.size() - 1)));
            
            this.conex.insertRow(update.toString());
            int index = Integer.parseInt(String.valueOf(array.get(array.size() - 1)).
                    substring(String.valueOf(array.get(array.size() - 1)).indexOf("=")+1));

            if (this.getCollection()) {
                this.saveMany2Many(((String) array.get(0)), false, index);
            }

        } catch (Exception e) {
            try {
                conex.cancelTransaction();
            } catch (Exception ex) {
                return false;
            }
            return false;
        }

        return true;
    }
}
