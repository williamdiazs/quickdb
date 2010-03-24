package cat.quickdb.query;

import java.util.ArrayList;

public class Where implements IQuery {

    Query query;
    private StringBuilder condition;
    private DateQuery date;

    private Where(Query query) {
        this.query = query;
        this.condition = new StringBuilder();
        this.date = DateQuery.createDate(this);
    }

    static Where createWhere(Query query) {
        Where where = new Where(query);
        return where;
    }

    @Override
    public void dataForViews(String fields, String names, Object obj, Class... clazz) {
        this.query.dataForViews(fields, names, clazz);
    }

    void addCondition(String cond) {
        this.condition.append(" " + cond);
    }

    public Query equal(Object... object) {
        this.condition.append(" = ");
        this.processObject(object);

        return this.query;
    }

    public Query greater(Object... object) {
        this.condition.append(" > ");
        this.processObject(object);

        return this.query;
    }

    public Query lower(Object... object) {
        this.condition.append(" < ");
        this.processObject(object);

        return this.query;
    }

    public Query equalORgreater(Object... object) {
        this.condition.append(" >= ");
        this.processObject(object);

        return this.query;
    }

    public Query equalORlower(Object... object) {
        this.condition.append(" <= ");
        this.processObject(object);

        return this.query;
    }

    public Query notEqual(Object... object) {
        this.condition.append(" <> ");
        this.processObject(object);

        return this.query;
    }

    public Where not() {
        this.condition.append(" NOT ");

        return this;
    }

    public Query isNull() {
        this.condition.append(" IS NULL ");

        return this.query;
    }

    public Query isNotNull() {
        this.condition.append(" IS NOT NULL ");

        return this.query;
    }

    public Query between(Object val1, Object val2) {
        this.condition.append(" BETWEEN '" + val1 + "' AND '" + val2 + "'");

        return this.query;
    }

    public Query in(Object... object) {
        this.condition.append(" IN (");
        for (int i = 0; i < object.length; i++) {
            if(i > 0){
                this.condition.append(", ");
            }
            if (object[i] instanceof String) {
                this.condition.append("'" + object[i] + "'");
            } else {
                this.condition.append(object[i]);
            }
        }
        this.condition.append(")");

        return this.query;
    }

    public Query match(String value) {
        this.condition.append(" LIKE ");
        if (!value.contains("_") && !value.contains("%")) {
            value = "%" + value + "%";
        }

        this.condition.append("'" + value + "'");

        return this.query;
    }

    public DateQuery date() {
        int index = this.condition.lastIndexOf(" ");
        String lastCondition = this.condition.substring(index);
        this.condition.delete(index, this.condition.length());
        this.date.setWhereCondition(lastCondition.trim());

        return this.date;
    }

    @Override
    public boolean find() {
        return this.query.find();
    }

    @Override
    public ArrayList findAll() {
        return this.query.findAll();
    }

    private String stringObject(Object object) {
        return "'" + String.valueOf(object) + "'";
    }

    private void processObject(Object... object) {
        if (object.length == 1) {
            if (object[0] instanceof String) {
                this.condition.append(this.stringObject(object[0]));
            } else {
                this.condition.append(String.valueOf(object[0]));
            }
        } else {
            String whereCondition;
            String field = String.valueOf(object[0]);
            Class clazz = (Class) object[1];
            if (object.length == 3) {
                Object obj = object[2];
                whereCondition = this.query.processRequest(field, clazz, obj);
            } else {
                whereCondition = this.query.processRequest(field, clazz);
            }
            this.addCondition(whereCondition);
        }
    }

    Query returnQuery() {
        return this.query;
    }

    @Override
    public String toString() {
        return this.condition.toString();
    }
}
