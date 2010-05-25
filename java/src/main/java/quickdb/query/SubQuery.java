package quickdb.query;

/**
 *
 * @author Diego Sarmentero
 */
public class SubQuery extends Query{

    private Where whereBase;

    private SubQuery(Where where, String attr, Object obj){
        super(null, obj);
        this.whereBase = where;
        this.select = new StringBuilder(this.table + "." + attr);
    }

    static SubQuery createSubQuery(Where where, String attr, Class clazz){
        Object obj = where.query.reflec.emptyInstance(clazz);
        SubQuery sub = new SubQuery(where, attr, obj);
        return sub;
    }

    public Where closeFor(){
        //hacer algo tipo FIND con addCondition
        StringBuilder sql = new StringBuilder();
        sql.append("(SELECT " + this.select.toString() +
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
        this.whereBase.addSubQueryCondition(sql.toString()+")");

        return this.whereBase;
    }

}
