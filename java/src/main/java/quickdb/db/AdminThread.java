package quickdb.db;

import java.util.Collection;

/**
 *
 * @author Diego Sarmentero
 */
public class AdminThread implements Runnable{

    private AdminBase admin;
    private Object object;
    private String sqlQuery;
    private OPERATIONS oper;

    private enum OPERATIONS{
        SAVE, MODIFY, DELETE, EXECUTE, LAZYLOAD,
        OBTAINWHERE, OBTAIN, OBTAINSTRING, OBTAINSELECT, SAVEALL, MODIFYALL
    }

    public AdminThread(AdminBase.DATABASE db, String... args){
        admin = AdminBase.initialize(db, args);
    }

    public AdminThread(AdminBase admin){
        this.admin = admin;
    }

    void setProperties(Object obj, String sql, OPERATIONS o){
        this.object = obj;
        this.sqlQuery = sql;
        this.oper = o;
    }

    @Override
    public void run(){
        switch(this.oper){
            case SAVE:
                this.admin.save(object);break;
            case MODIFY:
                this.admin.modify(object);break;
            case DELETE:
                this.admin.delete(object);break;
            case EXECUTE:
                this.admin.executeQuery(sqlQuery);break;
            case LAZYLOAD:
                this.admin.lazyLoad(object, sqlQuery);break;
            case OBTAINWHERE:
                this.admin.obtainWhere(object, sqlQuery);break;
            case OBTAIN:
                this.admin.obtain(object);break;
            case OBTAINSTRING:
                this.admin.obtain(object, sqlQuery);break;
            case OBTAINSELECT:
                this.admin.obtainSelect(object, sqlQuery);break;
            case SAVEALL:
                this.admin.saveAll(((Collection)object));break;
            case MODIFYALL:
                this.admin.modifyAll(((Collection)object));break;
        }
    }

    public void save(Object obj){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(obj, "", OPERATIONS.SAVE);
        Thread t = new Thread(at);
        t.start();
    }

    public void modify(Object obj){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(obj, "", OPERATIONS.MODIFY);
        Thread t = new Thread(at);
        t.start();
    }

    public void delete(Object obj){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(obj, "", OPERATIONS.DELETE);
        Thread t = new Thread(at);
        t.start();
    }

    public void execute(String query){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(null, query, OPERATIONS.EXECUTE);
        Thread t = new Thread(at);
        t.start();
    }

    public void lazyLoad(Object... obj){
        AdminThread at = new AdminThread(this.admin);
        String sql;
        if(obj.length == 2){
            sql = String.valueOf(obj[1]);
        }else{
            sql = "";
        }
        at.setProperties(obj[0], sql, OPERATIONS.LAZYLOAD);
        Thread t = new Thread(at);
        t.start();
    }

    public void obtainWhere(Object obj, String sql){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(obj, sql, OPERATIONS.OBTAINWHERE);
        Thread t = new Thread(at);
        t.start();
    }

    public void obtainSelect(Object obj, String sql){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(obj, sql, OPERATIONS.OBTAINSELECT);
        Thread t = new Thread(at);
        t.start();
    }

    public void obtain(Object obj){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(obj, "", OPERATIONS.OBTAIN);
        Thread t = new Thread(at);
        t.start();
    }

    public void obtain(Object obj, String sql){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(obj, sql, OPERATIONS.OBTAINSTRING);
        Thread t = new Thread(at);
        t.start();
    }

    public void saveAll(Collection array){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(array, "", OPERATIONS.SAVEALL);
        Thread t = new Thread(at);
        t.start();
    }

    public void modifyAll(Collection array){
        AdminThread at = new AdminThread(this.admin);
        at.setProperties(array, "", OPERATIONS.MODIFYALL);
        Thread t = new Thread(at);
        t.start();
    }

    public void setAutoCommit(boolean value){
        this.admin.setAutoCommit(value);
    }

}
