package quickdb.db;

import java.security.InvalidParameterException;
import quickdb.db.connection.ConnectionDB;
import quickdb.db.dbms.DbmsInterpreter;
import quickdb.db.dbms.mysql.ColumnDefined;
import quickdb.db.dbms.mysql.DataType;
import quickdb.modelSupport.M2mTable;
import quickdb.modelSupport.PrimitiveCollec;
import quickdb.query.Query;
import quickdb.query.StringQuery;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Stack;
import quickdb.db.connection.IConnectionDB;
import quickdb.reflection.EntityManager;

/**
 *
 * @author Diego Sarmentero
 */
public class AdminBase {

    protected IConnectionDB conex;
    protected ResultSet rs;
    protected EntityManager manager;
    protected String tableForcedName;
    protected boolean forceTable;
    private Stack<ResultSet> arrayRs;
    private DATABASE db;
    private boolean collectionHasName;
    private boolean collection;
    private boolean startObtainAll;
    private boolean commit;
    private boolean autoCommit;

    public enum DATABASE {

        MYSQL, POSTGRES, SQLSERVER, SQLite, FIREBIRD
    };

    protected AdminBase(DATABASE db, String... args) {

        this.db = db;
        this.commit = true;
        this.autoCommit = false;
        this.startObtainAll = false;
        this.collection = false;
        this.collectionHasName = false;
        this.forceTable = false;
        this.tableForcedName = "";
        this.manager = new EntityManager();
        this.arrayRs = new Stack<ResultSet>();
        this.conex = DbmsInterpreter.connect(db, args);
    }

    /**
     * For MySQL args: MYSQL, host, port, catalog, username, password
     * @param Database enumeration
     * @param Array of String depending on the database selected
     */
    public static AdminBase initialize(DATABASE db, String... args) {
        AdminBase admin;
        switch(db){
            case SQLite:
                admin = new AdminSQLite(db, args);break;
            default:
                admin = new AdminBase(db, args);break;
        }
        
        return admin;
    }

    /**
     * Initialize the AdminBase private attribute from AdminBinding
     * when QuickDB is used in the Data Model through inheritance
     * @param db
     * @param args
     */
    public static void initializeAdminBinding(DATABASE db, String... args) {
        AdminBase admin = new AdminBase(db, args);
        AdminBinding.initializeAdminBase(admin);
    }

    public void initializeAdminBinding(){
        AdminBinding.initializeAdminBase(this);
    }

    public static void initializeViews(DATABASE db, String... args) {
        AdminBase admin = new AdminBase(db, args);
        View.initializeAllViews(admin);
    }

    public void initializeViews(){
        View.initializeAllViews(this);
    }

    /**
     * Store the object in the Database (create the Table if not exist)
     * @param Object
     * @return True if the object could be saved, False in the other case
     */
    public boolean save(Object object) {

        int value = -1;
        try{
            boolean commitValue = this.commit;
            if(this.commit || this.autoCommit){
                conex.initTransaction();
                this.commit = false;
            }

            ArrayList array = this.manager.entity2Array(this, object,
                    EntityManager.OPERATION.SAVE);

            value = this.saveProcess(array, object);

            this.commit = commitValue;
            if(this.commit || this.autoCommit){
                conex.confirmTransaction();
                if(this.conex.getConnection().getAutoCommit()){
                    this.conex.getConnection().setAutoCommit(false);
                }
            }
        }catch(SQLException e){
            this.commit = true;
        }
        return (value > 0);
    }

    protected int saveProcess(ArrayList array, Object object) {
        int index = -1;
        String table = "";
        int i = 2;
        try {
            if (this.forceTable) {
                table = tableForcedName;
                array.set(0, table);
            } else {
                table = String.valueOf(array.get(0));
            }

            conex.openBlock(table);

            int size = array.size() - 1;
            for (; i < size; i++) {
                Object[] obj = (Object[]) array.get(i);

                String column = (String) obj[0];
                Object value = obj[1];

                conex.insertField(column, value);
            }

            index = conex.closeBlock();
            if (this.collection) {
                this.saveMany2Many(((String) array.get(0)), true, index);
            }
            this.manager.updateCache(object.getClass(), this);
        } catch (SQLException ex) {
            int value = -1;
            try{
                this.conex.getConnection().setAutoCommit(true);
                if (!this.checkTableExist(table)) {
                    if (this.createTable(object, array.toArray())) {
                        value = this.saveProcess(array, object);
                    }
                } else {
                    if (this.addColumn(table, object, array, i)) {
                        value = this.saveProcess(array, object);
                    }
                }
            }catch(Exception e){}
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

    /**
     * Store the object in the Database (create the Table if not exist) and
     * return the value of the PrimaryKey.
     * @param Object
     * @return The value of the PrimaryKey, or -1 if the object couldn't be
     * saved.
     */
    public int saveGetIndex(Object object) {
        int index = -1;
        try {
            boolean commitValue = this.commit;
            if(this.commit || this.autoCommit){
                conex.initTransaction();
                this.commit = false;
            }

            ArrayList array = this.manager.entity2Array(this, object,
                    EntityManager.OPERATION.SAVE);

            index = this.saveProcess(array, object);
            this.commit = commitValue;
            if(this.commit || this.autoCommit){
                conex.confirmTransaction();
                if(this.conex.getConnection().getAutoCommit()){
                    this.conex.getConnection().setAutoCommit(false);
                }
            }
        } catch (SQLException e) {
            this.commit = true;
        }

        return index;
    }

    protected boolean saveMany2Many(String table, boolean saveNotModify, int index) {
        ArrayList many = new ArrayList();
        try {
            String where = "";

            int sizeCollection = this.manager.sizeCollectionStack();
            for (int i = 0; i < sizeCollection; i++) {
                ArrayList collec = this.manager.getCollection();
                int size = collec.size();
                if(size > 0 && collec.get(0) instanceof PrimitiveCollec){
                    for (int q = 0; q < size; q++) {
                        ((PrimitiveCollec) collec.get(q)).setBase(index);
                        many.add(collec.get(q));
                    }
                }else{
                    for (int q = 0; q < size; q++) {
                        many.add(new M2mTable(index,
                                ((Integer) collec.get(q))));
                    }
                }

                this.collection = false;
                this.forceTable = true;
                if (this.collectionHasName) {
                    this.tableForcedName = this.manager.getNameCollection();
                } else {
                    String nameCollection = this.manager.getNameCollection();
                    //Consult if the table name or its inverse exists and asign match
                    this.tableForcedName = table + nameCollection;
                    where = "base = " + index;
                    String tempTableName = nameCollection + table;
                    if (!this.checkTableExist(this.tableForcedName) &&
                            this.checkTableExist(tempTableName)) {
                        this.tableForcedName = tempTableName;
                        where = "related = " + index;
                        for (M2mTable m2m : ((ArrayList<M2mTable>) many)) {
                            int temp = m2m.getBase();
                            m2m.setBase(m2m.getRelated());
                            m2m.setRelated(temp);
                        }
                    }
                }

                if (!saveNotModify) {
                    String sql = "DELETE FROM " + this.tableForcedName + " WHERE " + where;
                    this.executeQuery(sql);
                }
                this.saveAll(many);
                many.clear();
            }
        } catch (Exception e) {
            return false;
        } finally {
            this.collection = false;
            this.forceTable = false;
            this.collectionHasName = false;
        }

        return true;
    }

    /**
     * For this method only is the object to be modify.
     * It is necessary to execute obtain previously to recover
     * the proper object.
     * @param Object
     * @return True if the modification was successfull, False in the other case
     */
    public boolean modify(Object object) {
        boolean value = false;
        try{
            boolean commitValue = this.commit;
            if(this.commit || this.autoCommit){
                conex.initTransaction();
                this.commit = false;
            }

            ArrayList<String> manys = this.manager.getRef().isMany2Many(object.getClass());
            if (!manys.isEmpty()) {
                if (this.manager.deleteMany2Many(this, object, manys)) {
                    this.manager.cleanStack();
                    value = this.save(object);
                }
            } else {
                value = this.modifyProcess(object);
            }
            
            this.commit = commitValue;
            if(this.commit || this.autoCommit) conex.confirmTransaction();
        }catch(SQLException e){
            this.commit = true;
        }

        return value;
    }

    protected boolean modifyProcess(Object object) {
        ArrayList array = this.manager.entity2Array(this, object,
                EntityManager.OPERATION.MODIFY);

        try {
            this.rs = conex.updateField(((String) array.get(0)),
                    String.valueOf(array.get(array.size() - 1)));
            rs.next();

            do {
                int size = array.size() - 1;
                for (int i = 1; i < size; i++) {
                    Object[] obj = (Object[]) array.get(i);

                    String column = (String) obj[0];
                    Object value = obj[1];

                    rs.updateObject(column, value);
                    rs.updateRow();
                }
            } while (rs.next());

            int index = Integer.parseInt(String.valueOf(array.get(array.size() - 1)).
                    substring(String.valueOf(array.get(array.size() - 1)).indexOf("=")+1));

            if (this.collection) {
                this.saveMany2Many(((String) array.get(0)), false, index);
            }
            this.manager.updateCache(object.getClass(), this);
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

    private int modifyGetIndex(Object object) {
        int index = this.manager.getRef().checkIndexValue(object);
        /*If the index is greater than 0 it means that the Object already
        exist in the Database and has to be modify, if index is lower than
        0 it means that this is a new Object that has to be added to the
        database*/
        if (index > 0) {
            this.modify(object);
        } else {
            index = this.saveGetIndex(object);
        }

        return index;
    }

    /**
     * Delete the object received as parameter from the Database
     * @param Object
     * @return True if the operation complete successfully, False in the other case.
     */
    public boolean delete(Object object) {

        try {
            boolean commitValue = this.commit;
            if(this.commit || this.autoCommit){
                conex.initTransaction();
                this.commit = false;
            }

            ArrayList array = this.manager.entity2Array(this, object,
                EntityManager.OPERATION.DELETE);

            conex.deleteRows(((String) array.get(0)),
                    ((String) array.get(array.size() - 1)));

            this.manager.deleteParent(this, object);

            //Obtain the attributes from this object that are collections
            //and delete their relations in the Relational Table.
            ArrayList<String[]> strings = this.manager.getCollectionsTableForDelete(object, this);
            int index = (Integer) ((Object[]) array.get(1))[1];
            for (String[] s : strings) {
                if (this.checkTableExist(s[0])) {
                    this.executeQuery("DELETE FROM " + s[0] + " WHERE base=" + index);
                } else if (this.checkTableExist(s[1])) {
                    this.executeQuery("DELETE FROM " + s[1] + " WHERE related=" + index);
                }
            }

            this.commit = commitValue;
            if(this.commit || this.autoCommit) conex.confirmTransaction();
        } catch (Exception e) {
            this.commit = true;
            try {
                conex.cancelTransaction();
            } catch (Exception ex) {
                return false;
            }
            return false;
        }

        return true;

    }

    /**
     * Execute the SQL query received as parameter
     * @param object
     * @return
     */
    public boolean executeQuery(String statement) {
        try {
            conex.executeQuery(statement);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean obtainNext(Object object) {
        try {
            if (rs.next() && (this.manager.result2Object(this,
                    object, rs) != null)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            this.rs = this.arrayRs.peek();
        }

        return false;
    }

    /**
     * Receive an Object and a query and return a Collection of object
     * from the same type as was received, that represent the result of
     * that query.
     * @param Object
     * @param Query [String]
     * @return ArrayList [Collection of Object]
     */
    public ArrayList obtainAll(Class clazz, String sql) {
        boolean cacheable = this.manager.isCacheable(clazz);
        if(cacheable){
            ArrayList array = this.manager.obtainCache(sql, this, clazz);
            if(array != null){
                return array;
            }
        }
        this.startObtainAll = true;
        ArrayList results = new ArrayList();
        try {
            boolean value = false;
            Object object = this.manager.getRef().emptyInstance(clazz);
            if(sql.startsWith("SELECT ")){
                value = this.obtainSelect(object, sql);
            }else{
                value = this.obtainWhere(object, sql);
            }
            if (value) {
                this.rs = this.arrayRs.peek();
                results.add(object);
                Object obj = this.manager.getRef().emptyInstance(object);
                while (this.obtainNext(obj)) {
                    results.add(obj);
                    obj = this.manager.getRef().emptyInstance(object);
                }
            }
            if(cacheable){
                this.manager.makeCacheable(sql, results, clazz, this);
            }
        } catch (Exception e) {
            return null;
        } finally {
            this.startObtainAll = false;
            this.arrayRs.pop();
            if (!this.arrayRs.isEmpty()) {
                this.rs = this.arrayRs.peek();
            }
        }

        return results;
    }

    /**
     * Load the attributes in the object gradually.
     * If this method received 2 parameters[Object, StringQuery] is
     * the first time that the object is going to be loaded, the next
     * time is only needed to receive the Object.
     * @param Object, [String only for the first time]
     * @return Object
     */
    public boolean lazyLoad(Object... object) {
        this.manager.setDropDown(false);
        boolean value = false;
        if (object.length == 2) {
            String sql = String.valueOf(object[1]);
            value = this.obtain(object[0], sql);
        } else {
            value = this.manager.completeLazyLoad(this, object[0]);
        }

        this.manager.setDropDown(true);

        return value;
    }

    /**
     * This method can receive an object to determine the type of 
     * object that is going to be manage and a String with the query.
     * (Faster than 'obtain', this method doesnt has to parse the expression)
     * @param Object, String
     * @return True if the object could be obtained, False in the other case.
     */
    public boolean obtainWhere(Object object, String sql) {
        ArrayList array = this.manager.entity2Array(this, object,
                EntityManager.OPERATION.OBTAIN);

        try {
            if (this.collectionHasName) {
                this.rs = conex.selectWhere(this.manager.getNameCollection(), sql);
            } else {
                this.rs = conex.selectWhere(((String) array.get(0)), sql);
            }
            this.arrayRs.push(this.rs);

            if (rs.next() && (this.manager.result2Object(this, object, rs) != null)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (!this.startObtainAll) {
                this.arrayRs.pop();
                if (!this.arrayRs.isEmpty()) {
                    this.rs = this.arrayRs.peek();
                }
            } else {
                this.startObtainAll = false;
            }
        }

        return false;
    }

    /**
     * This Method return an Object[] containing String[] in each item,
     * that represent the result of the specified query as a Matrix of Strings
     * @param SQL Query [String]
     * @param Number of Columns involved [int]
     * @return Object[] or null
     */
    public Object[] obtainTable(Object... prop) {
        try {
            this.rs = conex.select((String)prop[0]);
            rs.next();
            int cols = 0;
            String[] names = new String[0];
            boolean hash = false;
            if(prop.length > 1 && prop[1].getClass().getSimpleName().equalsIgnoreCase("Integer")){
                cols = (Integer) prop[1];
            }else if(prop.length > 1 && prop[1].getClass().getSimpleName().equalsIgnoreCase("Boolean")){
                if((Boolean)prop[1]){
                    String sql = ((String) prop[0]).toLowerCase();
                    names = sql.substring(
                            sql.indexOf("select ")+7, sql.indexOf(" from")).split(",");
                    cols = names.length;
                    hash = true;
                }else{
                    String sql = ((String) prop[0]).toLowerCase();
                    cols = sql.substring(
                            sql.indexOf("select ")+7, sql.indexOf(" from")).split(",").length;
                }
            }else{
                throw new InvalidParameterException();
            }

            if(hash){
                ArrayList<Hashtable<String, Object>> array = new ArrayList<Hashtable<String, Object>>();
                do {
                    Hashtable<String, Object> table = new Hashtable<String, Object>(cols);
                    for (int i = 1; i < cols + 1; i++) {
                        table.put(names[i-1].trim(), rs.getObject(i));
                    }
                    array.add(table);
                } while (rs.next());
                return array.toArray();
            }else{
                ArrayList<String[]> array = new ArrayList<String[]>();
                do {
                    String[] objs = new String[cols];
                    for (int i = 1; i < cols + 1; i++) {
                        objs[i - 1] = rs.getObject(i).toString();
                    }
                    array.add(objs);
                } while (rs.next());
                return array.toArray();
            }

        } catch (Exception e) {
            return null;
        }
    }

    @Deprecated
    public Object[] obtainJoin(String sql, int cols){
        return this.obtainTable(sql, cols);
    }

    public Query obtain(Object obj){
        return Query.create(this, obj);
    }

    /**
     * Return the object restore with the information from the
     * database, based on the Object Oriented Query that was received.
     * @param Object
     * @param Query statement
     * @return True if the Object could be restored, False in the other case.
     */
    public boolean obtain(Object obj, String sql) {
        String query = StringQuery.parse(obj.getClass(), sql);

        return this.obtainSelect(obj, query);
    }

    /**
     * Return the Object from the Database that represent
     * the [completely] specified query
     * @param Object
     * @param SQL Query [String]
     * @return True if the object could be restored, False in the other case.
     */
    public boolean obtainSelect(Object object, String sql) {
        try {
            this.rs = conex.select(sql);
            this.arrayRs.push(this.rs);

            if (rs.next() && (this.manager.result2Object(this,
                    object, rs) != null)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (!this.startObtainAll) {
                this.arrayRs.pop();
                if (!this.arrayRs.isEmpty()) {
                    this.rs = this.arrayRs.peek();
                }
            } else {
                this.startObtainAll = false;
            }
        }

        return false;
    }

    /**
     * Save all the items in the Collection in the Database
     * @param An Object that implement java.util.Collection
     * @return An ArrayList with the number of elements inserted or
     * an ArrayList with each item index if the array received
     * represent a Collection Reference.
     */
    public ArrayList<Integer> saveAll(Collection array) {
        ArrayList<Integer> results = new ArrayList<Integer>();
        try{
            boolean commitValue = this.commit;
            if(this.commit || this.autoCommit){
                conex.initTransaction();
                this.commit = false;
            }
            
            Object objects[] = array.toArray();
            if (!this.collection) {
                for (int i = 0; i < objects.length; i++) {
                    if (!this.save(objects[i])) {
                        results.add(i); //throw exception
                        break;
                    }
                }
            } else {
                this.collection = false;
                for (Object obj : objects) {
                    results.add(this.saveGetIndex(obj));
                }
                this.collection = true;
            }

            this.commit = commitValue;
            if(this.commit || this.autoCommit){
                conex.confirmTransaction();
                if(this.conex.getConnection().getAutoCommit()){
                    this.conex.getConnection().setAutoCommit(false);
                }
            }
        }catch(SQLException e){
            this.commit = true;
        }

        return results;
    }

    /**
     * Modify in the Database the objects in the array received as
     * parameter.
     * @param An Object that implements java.util.Collection with the items
     * @return An ArrayList with the index of each item if collection if from
     * a Reference Collection or an ArrayList with the number of item that
     * couldnt be updated if this is not a Collection Reference
     */
    public ArrayList<Integer> modifyAll(Collection array) {
        ArrayList<Integer> results = new ArrayList<Integer>();
        try{
            boolean commitValue = this.commit;
            if(this.commit || this.autoCommit){
                conex.initTransaction();
                this.commit = false;
            }

            Object objects[] = array.toArray();
            if (!this.collection) {
                for (int i = 0; i < objects.length; i++) {
                    if (!this.modify(objects[i])) {
                        results.add(i);
                        break;
                    }
                }
            } else {
                this.collection = false;
                for (Object obj : objects) {
                    int index = this.modifyGetIndex(obj);
                    results.add(index);
                }
                this.collection = true;
            }

            this.commit = commitValue;
            if(this.commit || this.autoCommit) conex.confirmTransaction();
        }catch(SQLException e){
            this.commit = true;
        }

        return results;
    }

    protected boolean createTable(Object entity, Object[] objects) {
        return DbmsInterpreter.createTable(db, this, entity, objects, manager);
    }

    protected boolean addColumn(String table, Object object,
            ArrayList array, int pos) throws Exception{
        String statement = "";
        if ((object.getClass().getDeclaredFields()[pos-1].getDeclaredAnnotations().length != 0)) {
            Object columns[] = this.manager.mappingDefinition(this, object);

            statement = String.format("ALTER TABLE %s ADD %s", table,
                    ((ColumnDefined) columns[pos]).toString());
        } else {
            DataType dataType = new DataType();
            statement = String.format("ALTER TABLE %s ADD %s %s", table,
                    String.valueOf(((Object[]) array.get(pos))[0]),
                    dataType.getDataType(((Object[]) array.get(pos))[1].getClass(),
                    ((Object[]) array.get(pos))[1].toString().length()));
        }

        return this.executeQuery(statement);
    }

    public void openAtomicBlock(){
        this.commit = false;
        try{
            this.conex.initTransaction();
        }catch(Exception e){
            this.commit = true;
        }
    }

    public void closeAtomicBlock(){
        this.commit = true;
        try{
            this.conex.confirmTransaction();
        }catch(Exception e){
            
        }
    }

    public void cancelAtomicBlock(){
        this.commit = true;
        try{
            this.conex.cancelTransaction();
        }catch(Exception e){

        }
    }

    public void setAutoCommit(boolean value){
        this.autoCommit = value;
    }

    /**
     * Return True if the specified Table exist in the Database
     * @param Table Name
     * @return Boolean
     */
    public boolean checkTableExist(String table) {
        return this.conex.existTable(table);
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public boolean getCollection() {
        return this.collection;
    }

    public void setCollectionHasName(boolean collectionHasName) {
        this.collectionHasName = collectionHasName;
    }

    public IConnectionDB getConex() {
        return this.conex;
    }

    public DATABASE getDB(){
        return this.db;
    }

    public void close(){
        this.conex.closeConnection();
    }

    public void setForceTable(boolean forceTable) {
        this.forceTable = forceTable;
    }

    public void setTableForcedName(String tableForcedName) {
        this.tableForcedName = tableForcedName;
    }

    public void activateLogging(Object... values){
        this.close();
        if((Boolean) values[0]){
            if(values.length < 2) throw new InvalidParameterException();
            this.conex = DbmsInterpreter.connectLogging(db, ((String) values[1]), DbmsInterpreter.properties);
        }else{
            this.conex = DbmsInterpreter.connect(db, DbmsInterpreter.properties);
        }
    }
    
}
