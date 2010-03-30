package cat.quickdb.query;

import cat.quickdb.db.AdminBase;
import cat.quickdb.reflection.ReflectionUtilities;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Query implements IQuery {

    AdminBase admin;
    protected StringBuilder select;
    protected StringBuilder from;
    protected StringBuilder groupby;
    protected StringBuilder order;
    protected Where where;
    protected Where having;
    ReflectionUtilities reflec;
    private Object object;
    protected String table;

    protected Query(AdminBase admin, Object object) {
        this.admin = admin;
        this.object = object;
        this.reflec = new ReflectionUtilities();
        this.from = new StringBuilder();
        this.select = new StringBuilder();

        this.table = this.reflec.readTableName(this.object.getClass());
        this.from.append(this.table);
        this.select.append(StringQuery.mainData(object.getClass()));
    }

    public static Query create(AdminBase admin, Object object) {
        return new Query(admin, object);
    }

    @Override
    public void dataForViews(String fields, String names, Object obj, Class... clazz){
        String splitFields[] = fields.split(",");
        if(names.equalsIgnoreCase("")){
            Field[] objFields = obj.getClass().getDeclaredFields();
            StringBuilder strbui = new StringBuilder();
            for(Field f : objFields){
                strbui.append(StringQuery.columnName(obj.getClass(), f.getName())+", ");
            }
            strbui.deleteCharAt(strbui.length()-2);
            names = strbui.toString();
        }
        String splitNames[] = names.split(",");
        this.select.delete(0, this.select.length());
        for(int i = 0; i < splitFields.length; i++){
            String addSelect = this.processRequest(splitFields[i].trim(), clazz[i]);
            if(i > 0){
                this.select.append(", ");
            }

            this.select.append(addSelect+" '"+splitNames[i].trim()+"'");
        }
        
        this.object = obj;
    }

    public Where If(String field, Object... clazz) {
        if (this.where == null) {
            this.where = Where.createWhere(this);
        }
        String whereCondition = this.processRequest(field, clazz);
        this.where.addCondition(whereCondition);

        return this.where;
    }

    public Where and(String field, Object... clazz) {
        this.where.addCondition("AND");
        String whereCondition = this.processRequest(field, clazz);
        if (this.having == null) {
            this.where.addCondition(whereCondition);
            return this.where;
        } else {
            this.having.addCondition(whereCondition);
            return this.having;
        }
    }

    public Where or(String field, Object... clazz) {
        this.where.addCondition("OR");
        String whereCondition = this.processRequest(field, clazz);
        if (this.having == null) {
            this.where.addCondition(whereCondition);
            return this.where;
        } else {
            this.having.addCondition(whereCondition);
            return this.having;
        }
    }

    public Query group(String fields, Class... clazz) {
        if (this.groupby == null) {
            this.groupby = new StringBuilder();
            this.groupby.append("GROUP BY ");
            String[] splitField = fields.split(",");
            if(clazz.length == 0){
                clazz = new Class[splitField.length];
                for(int i = 0; i < splitField.length; i++){
                    clazz[i] = this.obtainClassBase();
                }
            }

            for (int i = 0; i < splitField.length; i++) {
                if (i > 0) {
                    this.groupby.append(", ");
                }
                Class c = clazz[i];
                String whereCondition = this.processRequest(splitField[i].trim(), c);
                if (this.select.indexOf(whereCondition) == -1) {
                    this.select.append(", " + whereCondition);
                }
                this.groupby.append(whereCondition);
            }
        }

        return this;
    }

    public Where ifGroup(String field, Class... clazz) {
        if (this.groupby != null) {
            this.having = Where.createWhere(this);
            this.groupby.append(" HAVING ");
            String whereCondition = this.processRequest(field, ((Object[]) clazz));
            this.having.addCondition(whereCondition);
        }

        return this.having;
    }

    public Query anyElement(){
        if (this.having == null) {
            this.where.addCondition("ANY");
        } else {
            this.having.addCondition("ANY");
        }

        return this;
    }

    public Query allElements(){
        if (this.having == null) {
            this.where.addCondition("ALL");
        } else {
            this.having.addCondition("ALL");
        }

        return this;
    }

    public Query sort(boolean asc, String fields, Class... clazz) {
        if (this.order == null) {
            this.order = new StringBuilder();
            this.order.append("ORDER BY ");
            String[] splitField = fields.split(",");
            if(clazz.length == 0){
                clazz = new Class[splitField.length];
                for(int i = 0; i < splitField.length; i++){
                    clazz[i] = this.obtainClassBase();
                }
            }

            for (int i = 0; i < splitField.length; i++) {
                if (i > 0) {
                    this.order.append(", ");
                }
                Class c = clazz[i];
                String whereCondition = this.processRequest(splitField[i].trim(), c);
                this.order.append(whereCondition);
            }

            if (!asc) {
                this.order.append(" DESC");
            }
        }

        return this;
    }

    @Override
    public boolean find() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " + this.select.toString() +
                " FROM " + this.from.toString());
        if (this.where != null) {
            sql.append(" WHERE " + this.where.toString());
        }
        if (this.groupby != null) {
            sql.append(" " + this.groupby.toString());
        }
        if (this.having != null) {
            sql.append(" " + this.having.toString());
        }
        if (this.order != null) {
            sql.append(" " + this.order.toString());
        }

        return this.admin.obtainSelect(this.object, sql.toString());
    }

    @Override
    public ArrayList findAll() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " + this.select.toString() +
                " FROM " + this.from.toString());
        if (this.where != null) {
            sql.append(" WHERE " + this.where.toString());
        }
        if (this.groupby != null) {
            sql.append(" " + this.groupby.toString());
        }
        if (this.having != null) {
            sql.append(" " + this.having.toString());
        }
        if (this.order != null) {
            sql.append(" " + this.order.toString());
        }
        ArrayList array = this.admin.obtainAll(this.object, sql.toString());

        return array;
    }

    protected String processRequest(String field, Object... clazz) {
        Class c;
        if (clazz.length != 0) {
            c = (Class) clazz[0];
        } else {
            c = this.object.getClass();
        }

        Class value = StringQuery.obtainReference(c, field);
        String tableRef = this.reflec.readTableName(value);
        String colMain = StringQuery.columnName(c, field);
        int inher = 0;
        if(c.isInstance(this.object)){
            inher = StringQuery.inheritedAttribute(this.obtainClassBase(), field);
        }else{

        }
        String whereCondition;
        if (this.obtainClassBase() == value) {
            whereCondition = this.table + "." + colMain;
        } else if ((inher != 0)) {
            if ((inher > 0)) {
                this.addInheritanceRelation(c, inher);
            }
            whereCondition = tableRef + "." + colMain;
        } else {
            Class clazzResult;
            String fieldResult;
            if (clazz.length == 3) {
                fieldResult = String.valueOf(clazz[2]);
                clazzResult = StringQuery.obtainReference(c, fieldResult);
            } else {
                Object[] result = StringQuery.obtainReferenceByReturn(this.object.getClass(), c);
                clazzResult = (Class) result[0];
                fieldResult = String.valueOf(result[1]);
            }
            if (fieldResult.equalsIgnoreCase("")) {
                this.addStartFrom(this.reflec.readTableName(c) + ", ");
            } else if ((clazzResult != this.obtainClassBase())) {
                int inherit = 0;
                value = this.obtainClassBase();
                while (value != clazzResult) {
                    inherit++;
                    value = value.getSuperclass();
                }

                if (inherit > 0) {
                    this.addInheritanceRelation(
                            this.obtainClassBase(), inherit);
                }
                String tableBase = this.reflec.readTableName(clazzResult);
                String tableRefBase = this.reflec.readTableName(c);
                if (this.from.indexOf("JOIN " + tableRefBase + " ") == -1) {
                    String colId = StringQuery.tableIndex(c);
                    this.from.append(" JOIN " +
                            tableRefBase + " ON " + tableBase + "." + fieldResult +
                            " = " + tableRefBase + "." + colId);
                }
            }else{
                String tableBase = this.reflec.readTableName(clazzResult);
                String tableRefBase = this.reflec.readTableName(c);
                if (this.from.indexOf("JOIN " + tableRefBase + " ") == -1) {
                    String colId = StringQuery.tableIndex(c);
                    this.from.append(" JOIN " +
                            tableRefBase + " ON " + tableBase + "." + fieldResult +
                            " = " + tableRefBase + "." + colId);
                }
            }
            int inherit = StringQuery.inheritedAttribute(c, field);
            if ((inherit > 0)) {
                this.addInheritanceRelation(c, inherit);
            }
            whereCondition = tableRef + "." + colMain;
        }

        return whereCondition;
    }

    void addInheritanceRelation(Class clazz, int inherit) {
        Class inher = clazz;
        for (int i = 0; i < inherit; i++) {
            String tableInher = this.reflec.readTableName(inher);
            Class value = inher.getSuperclass();
            String tableRef = this.reflec.readTableName(value);
            if (this.from.indexOf("JOIN " + tableRef + " ") == -1) {
                String colId = StringQuery.tableIndex(value);
                this.from.append(" JOIN " + tableRef + " ON " +
                        tableInher + ".parent_id = " + tableRef + "." + colId);
                inher = value;
            }
        }
    }

    Class obtainClassBase() {
        return this.object.getClass();
    }

    void addStartFrom(String fromTable) {
        if(this.from.indexOf(fromTable) == -1){
            this.from.insert(0, fromTable);
        }
    }

    void addEndFrom(String fromTable) {
        this.from.append(fromTable);
    }
}
