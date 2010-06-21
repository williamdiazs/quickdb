package quickdb.db;

import quickdb.query.Query;
import quickdb.reflection.ReflectionUtilities;

/**
 *
 * @author Diego Sarmentero
 */
public class AdminBinding {

    private static AdminBase admin;

    public static void initializeAdminBase(AdminBase admin) {
        AdminBinding.admin = admin;
    }

    public boolean save() {
        return AdminBinding.admin.save(this);
    }

    public int saveGetIndex() {
        return AdminBinding.admin.saveGetIndex(this);
    }

    public boolean modify() {
        return AdminBinding.admin.modify(this);
    }

    public boolean delete() {
        return AdminBinding.admin.delete(this);
    }

    public boolean lazyLoad(String sql) {
        boolean value = false;
        if (sql.length() == 0) {
            value = AdminBinding.admin.lazyLoad(this);
        } else {
            value = AdminBinding.admin.lazyLoad(this, sql);
        }

        return value;
    }

    public boolean obtainWhere(String sql) {
        return AdminBinding.admin.obtainWhere(this, sql);
    }

    public boolean obtain(String sql) {
        return AdminBinding.admin.obtain(this, sql);
    }

    public Query obtain() {
        return AdminBinding.admin.obtain(this);
    }

    public boolean obtainSelect(String sql) {
        return AdminBinding.admin.obtainSelect(this, sql);
    }

    public void openAtomicBlock(){
        this.admin.openAtomicBlock();
    }

    public void closeAtomicBlock(){
        this.admin.closeAtomicBlock();
    }

    public void cancelAtomicBlock(){
        this.admin.cancelAtomicBlock();
    }

    public void setAutoCommit(boolean value){
        this.admin.setAutoCommit(value);
    }

    /**
     * Return True if the specified Table exist in the Database
     * @param Table Name
     * @return Boolean
     */
    public boolean checkTableExist() {
        ReflectionUtilities reflec = new ReflectionUtilities();
        String name = reflec.readTableName(this.getClass());
        return this.admin.checkTableExist(name);
    }

}
