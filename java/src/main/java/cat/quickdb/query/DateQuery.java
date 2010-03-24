package cat.quickdb.query;

import cat.quickdb.db.AdminBase;
import java.util.Hashtable;

public class DateQuery {

    private Where where;
    private String whereCondition;

    private enum OPERATIONS{
        DATEDIFF, MONTH, YEAR, DAY
    }

    private Hashtable<OPERATIONS, String> mysql;
    private Hashtable<OPERATIONS, String> postgre;
    private Hashtable<OPERATIONS, String> sqlserver;
    private Hashtable<OPERATIONS, String> firebird;

    private DateQuery(Where where) {
        this.where = where;
        mysql = new Hashtable<OPERATIONS, String>();
        postgre = new Hashtable<OPERATIONS, String>();
        sqlserver = new Hashtable<OPERATIONS, String>();
        firebird = new Hashtable<OPERATIONS, String>();
        mysql.put(OPERATIONS.DAY, "DAY(");
        mysql.put(OPERATIONS.MONTH, "MONTH(");
        mysql.put(OPERATIONS.YEAR, "YEAR(");
        mysql.put(OPERATIONS.DATEDIFF, "DATEDIFF(");

        sqlserver.put(OPERATIONS.DAY, "DAY(");
        sqlserver.put(OPERATIONS.MONTH, "MONTH(");
        sqlserver.put(OPERATIONS.YEAR, "YEAR(");
        sqlserver.put(OPERATIONS.DATEDIFF, "DATEDIFF(dd, ");

        postgre.put(OPERATIONS.DAY, "date_part(day, TIMESTAMP ");
        postgre.put(OPERATIONS.MONTH, "date_part(month, TIMESTAMP ");
        postgre.put(OPERATIONS.YEAR, "date_part(year, TIMESTAMP ");
        postgre.put(OPERATIONS.DATEDIFF, "");

        firebird.put(OPERATIONS.DAY, "EXTRACT(DAY FROM ");
        firebird.put(OPERATIONS.MONTH, "EXTRACT(MONTH FROM ");
        firebird.put(OPERATIONS.YEAR, "EXTRACT(YEAR FROM ");
        firebird.put(OPERATIONS.DATEDIFF, "");
    }

    static DateQuery createDate(Where where) {
        return new DateQuery(where);
    }

    void setWhereCondition(String cond) {
        this.whereCondition = cond;
    }

    public Where differenceWith(String value, Object... clazz) {
        String condition2;
        if (clazz.length == 1) {
            condition2 = this.where.query.processRequest(value, clazz[0]);
        } else {
            condition2 = "'" + value + "'";
        }
        String operation = this.obtainProperFunction(OPERATIONS.DATEDIFF);
        if(operation.equalsIgnoreCase("") &&
                (this.where.query.admin.getDB() == AdminBase.DATABASE.POSTGRES)){
            this.where.addCondition("TIMESTAMP " + condition2 + " - TIMESTAMP " + this.whereCondition);
        }else if(operation.equalsIgnoreCase("") &&
                (this.where.query.admin.getDB() == AdminBase.DATABASE.FIREBIRD)){
            this.where.addCondition(condition2 + " - " + this.whereCondition);
        }else{
            this.where.addCondition(operation + condition2 + ", " + this.whereCondition + ")");
        }

        return this.where;
    }

    public Where month() {
        String operation = this.obtainProperFunction(OPERATIONS.MONTH);
        this.where.addCondition(operation + this.whereCondition + ")");

        return this.where;
    }

    public Where year() {
        String operation = this.obtainProperFunction(OPERATIONS.YEAR);
        this.where.addCondition(operation + this.whereCondition + ")");

        return this.where;
    }

    public Where day() {
        String operation = this.obtainProperFunction(OPERATIONS.DAY);
        this.where.addCondition(operation + this.whereCondition + ")");

        return this.where;
    }

    private String obtainProperFunction(OPERATIONS oper){
        String value;
        switch(this.where.query.admin.getDB()){
            case POSTGRES:
                value = this.postgre.get(oper);break;
            case SQLSERVER:
                value = this.sqlserver.get(oper);break;
            case FIREBIRD:
                value = this.firebird.get(oper);break;
            default:
                value = this.mysql.get(oper);break;
        }

        return value;
    }

}
