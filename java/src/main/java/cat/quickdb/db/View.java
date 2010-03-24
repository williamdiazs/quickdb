package cat.quickdb.db;

import cat.quickdb.query.IQuery;
import cat.quickdb.query.Query;
import cat.quickdb.query.Where;
import java.util.ArrayList;

public abstract class View {

    private AdminBase admin;
    private static AdminBase adminAll;
    private boolean dynamic;
    private Object dynQuery;

    public View(){
        if(View.adminAll != null){
            this.admin = adminAll;
        }
        dynamic = false;
    }

    public final void initializeAdminBase(AdminBase admin) {
        this.admin = admin;
    }

    static final void initializeAllViews(AdminBase admin) {
        View.adminAll = admin;
    }

    public final boolean obtain(){
        boolean value = false;
        Object obj;
        if(this.dynamic){
            obj = this.dynQuery;
        }else{
            obj = this.query();
        }
        if(obj instanceof String){
            String sql = String.valueOf(obj);
            value = this.admin.obtainSelect(this, sql);
        }else if((obj instanceof Query) || (obj instanceof Where)){
            ((IQuery) obj).dataForViews(this.columns(),
                    this.renameColumns(), this, this.classes());
            ((IQuery) obj).find();
        }

        return value;
    }

    public final ArrayList obtainAll(){
        ArrayList array = null;
        Object obj;
        if(this.dynamic){
            obj = this.dynQuery;
        }else{
            obj = this.query();
        }
        if(obj instanceof String){
            String sql = String.valueOf(obj);
            array = this.admin.obtainAll(this, sql);
        }else if((obj instanceof Query) || (obj instanceof Where)){
            ((IQuery) obj).dataForViews(this.columns(),
                    this.renameColumns(), this, this.classes());
            array = ((IQuery) obj).findAll();
        }

        return array;
    }

    public final void dynamicQuery(Object query){
        if(query instanceof String){
            this.dynQuery = this.query() + " " + query;
        }else if((query instanceof Query)){
            this.dynQuery = query;
        }
        this.dynamic = true;
    }

    public final AdminBase getAdminBase(){
        return this.admin;
    }

    protected String renameColumns(){
        return "";
    }

    protected String columns(){
        return "";
    }

    protected Class[] classes(){
        return new Class[0];
    }

    protected abstract Object query();

}
