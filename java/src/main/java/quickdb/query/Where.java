package quickdb.query;

import java.util.ArrayList;
import quickdb.exception.SubQueryException;

/**
 *
 * @author Diego Sarmentero
 */
public class Where implements IQuery {

    Query query;
    private StringBuilder condition;
    private DateQuery date;
    private boolean hasSub;
    private String sub;
    private boolean waitingForSub;

    private Where(Query query) {
        this.query = query;
        this.condition = new StringBuilder();
        this.date = DateQuery.createDate(this);
        this.hasSub = false;
        this.waitingForSub = false;
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

    public SubQuery For(String attr, Class clazz) {
        this.waitingForSub = false;
        SubQuery subQ = SubQuery.createSubQuery(this, attr, clazz);
        return subQ;
    }

    public Query equal(Object... object) {
        this.manageOperation("=", object);

        return this.query;
    }

    public Query greater(Object... object) {
        this.manageOperation(">", object);

        return this.query;
    }

    public Query lower(Object... object) {
        this.manageOperation("<", object);

        return this.query;
    }

    public Query equalORgreater(Object... object) {
        this.manageOperation(">=", object);

        return this.query;
    }

    public Query equalORlower(Object... object) {
        this.manageOperation("<=", object);

        return this.query;
    }

    public Query notEqual(Object... object) {
        this.manageOperation("<>", object);

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

    public Query inRange(Object val1, Object val2) {
        this.condition.append(" BETWEEN '" + val1 + "' AND '" + val2 + "'");

        return this.query;
    }

    @Deprecated
    public Query between(Object val1, Object val2) {
        return this.inRange(val1, val2);
    }

    public Query in(Object... object) {
        if (this.hasSub) {
            this.manageOperation("IN", object);
            this.hasSub = false;
        }else{
            this.condition.append(" IN (");
            for (int i = 0; i < object.length; i++) {
                if (i > 0) {
                    this.condition.append(", ");
                }
                if (object[i] instanceof String) {
                    this.condition.append("'" + object[i] + "'");
                } else {
                    this.condition.append(object[i]);
                }
            }
            this.condition.append(")");
        }

        return this.query;
    }

    public Query match(String value) {
        this.condition.append(" LIKE ");
        if (this.hasSub) {
            this.condition.append(" " + this.sub + " ");
            this.hasSub = false;
        }else{
            if (!value.contains("_") && !value.contains("%")) {
                value = "%" + value + "%";
            }

            this.condition.append("'" + value + "'");
        }

        return this.query;
    }

    void addSubQueryCondition(String sub) {
        this.hasSub = true;
        this.sub = sub;
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

    private void manageOperation(String oper, Object[] object) {
        if(this.waitingForSub){
            throw new SubQueryException();
        }
        if (this.hasSub) {
            this.processObject(object);
            this.condition.append(" " + oper + " ");
            this.condition.append(" " + this.sub + " ");
            this.hasSub = false;
        } else {
            this.condition.append(" " + oper + " ");
            this.processObject(object);
        }
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

    public void waitForSub(){
        this.waitingForSub = true;
    }
}
