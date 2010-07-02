package quickdb.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import quickdb.annotation.*;
import quickdb.db.AdminBase;
import quickdb.db.View;
import quickdb.db.dbms.mysql.ColumnDefined;
import quickdb.db.dbms.postgre.ColumnDefinedPostgre;
import quickdb.modelSupport.M2mTable;
import quickdb.modelSupport.PrimitiveCollec;
import quickdb.util.Validations;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Stack;
import quickdb.exception.DictionaryIncompleteException;
import quickdb.modelSupport.CacheManager;

/**
 *
 * @author Diego Sarmentero
 */
public class EntityManager {

    public enum OPERATION {

        SAVE, MODIFY, DELETE, OBTAIN, OTHER
    }
    Stack<String> primaryKey;
    Stack<Integer> primaryKeyValue;
    Stack<ArrayList> collection;
    Stack<Integer> sizeCollection;
    Stack<String> nameCollection;
    Stack<Object> originalChild;
    Stack<String[]> executeAfter;
    boolean hasParent;
    boolean dropDown;
    private ArrayList<String> manyRestore;
    private Hashtable<String, CacheManager> cacheStore;
    private ReflectionUtilities ref;
    private Table action;

    public EntityManager() {
        this.ref = new ReflectionUtilities();
        this.primaryKey = new Stack<String>();
        this.collection = new Stack<ArrayList>();
        this.sizeCollection = new Stack<Integer>();
        this.dropDown = true;
        this.nameCollection = new Stack<String>();
        this.primaryKeyValue = new Stack<Integer>();
        this.originalChild = new Stack<Object>();
        this.executeAfter = new Stack<String[]>();
        this.hasParent = false;
        this.manyRestore = new ArrayList<String>();
        this.action = null;
        this.cacheStore = new Hashtable<String, CacheManager>();
    }

    /**
     * The ArrayList returned contain in the first row the name of the Table
     * and then contain an String Array (Object[]) in the other rows
     * representing the name of the field and the data, and finally if exist a
     * sql statement or an empty string if there isn't a sql statement
     * @param object to be interpreted depending on his annotations
     * @return an ArrayList with the structure explained
     */
    public ArrayList entity2Array(AdminBase admin, Object object,
            OPERATION oper) {

        EntityDictionary dictionary = new EntityDictionary();
        try {
            if (dictionary.contains(object.getClass().getName())) {
                return dictionary.entity2Array(admin, this, object, oper);
            }
        } catch (DictionaryIncompleteException excep) {
        }

        ArrayList array = new ArrayList();
        boolean sql = true;
        boolean ignore = false;
        String statement = "";

        String tableName = this.readClassName(object);
        dictionary.newDictObject(tableName, hasParent, this.action);
        this.action = null;
        array.add(tableName);
        boolean tempParent = this.hasParent;
        this.hasParent = false;

        int primKeyItems = this.primaryKey.size();
        int sizeCollectionInt = 0;
        this.primaryKey.push("id");
        Field fields[] = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            DictionaryBody body = new DictionaryBody();
            Object objs[] = new Object[2];
            String field = fields[i].getName();
            body.setFieldName(field);
            objs[0] = field;
            Annotation ann = null;

            Annotation annotations[] = fields[i].getAnnotations();
            for (Annotation a : annotations) {

                if (a instanceof Column) {
                    if (((Column) a).name().length() != 0) {
                        objs[0] = ((Column) a).name();
                    }

                    ignore = ((Column) a).ignore();

                    ann = a;
                    if (((Column) a).type() == Properties.TYPES.PRIMARYKEY) {
                        this.primaryKey.pop();
                        primaryKey.push(objs[0].toString());
                        body.setDataType(Properties.TYPES.PRIMARYKEY);
                        continue;
                    }
                    if(((Column)a).summary().length() != 0){
                        body.setDataType(Properties.TYPES.PRIMITIVE);
                        Method setter = this.ref.obtainSetter(object.getClass(), field);
                        body.setSet(setter);
                        ignore = true;
                    }
                } else if (a instanceof Validation) {
                    body.setValidation((Validation) a);
                    if (!Validations.isValidField(object, fields[i].getName(), ((Validation)a), ref)) {
                        return null;
                    }
                } else {
                    continue;
                }
            }
            body.setColName(objs[0].toString());

            if (ignore) {
                ignore = false;
                continue;
            }

            try {
                //Assign Data to Array
                if (sql) {
                    if (oper == OPERATION.MODIFY
                            || oper == OPERATION.DELETE) {
                        String nameSta = this.primaryKey.peek();
                        Method getSta = this.ref.obtainGetter(object.getClass(), nameSta);
                        Integer valueId = (Integer) getSta.invoke(object, new Object[0]);
                        statement = nameSta + "=" + valueId;
                    } else {
                        statement = this.primaryKey.peek() + " > 0";
                    }
                    sql = false;
                }

                Method getter = this.ref.obtainGetter(object.getClass(), field);
                Method setter = this.ref.obtainSetter(object.getClass(), field);
                body.setGet(getter);
                body.setSet(setter);
                Object value = getter.invoke(object, new Object[0]);
                boolean wasNull = false;
                if (value == null) {
                    wasNull = true;
                    value = this.ref.emptyInstance(getter.getReturnType().getName());
                }

                if (this.ref.implementsCollection(value.getClass(), ann)) {
                    body.setDataType(Properties.TYPES.COLLECTION);
                    admin.setCollection(true);
                    Class clazz = this.ref.obtainItemCollectionType(object.getClass(), field);
                    body.setCollectionClass(clazz);
                    admin.setCollectionHasName(true);
                    this.nameCollection.push(this.ref.readTableName(clazz)
                            + field.substring(0, 1).toUpperCase() + field.substring(1));

                    if (this.ref.checkPrimitivesExtended(clazz, null)) {
                        Object[] arrayPrimitive = ((Collection) value).toArray();
                        ArrayList primitiveResult = new ArrayList();
                        for (Object prim : arrayPrimitive) {
                            PrimitiveCollec primitive = new PrimitiveCollec(prim);
                            primitiveResult.add(primitive);
                        }
                        switch (oper) {
                            case SAVE:
                            case MODIFY:
                                this.collection.push(primitiveResult);
                                sizeCollectionInt++;
                                break;
                        }
                    } else {
                        switch (oper) {
                            case SAVE:
                                this.collection.push(admin.saveAll(((Collection) value)));
                                sizeCollectionInt++;
                                break;
                            case MODIFY:
                                this.collection.push(admin.modifyAll(((Collection) value)));
                                sizeCollectionInt++;
                                break;
                        }
                    }

                    if (sizeCollectionInt == 0) {
                        this.nameCollection.pop();
                    }
                    admin.setCollectionHasName(false);
                } else if (!this.ref.checkPrimitivesExtended(value.getClass(), ann)) {
                    body.setDataType(Properties.TYPES.FOREIGNKEY);
                    if (this.dropDown && !wasNull) {
                        boolean tempCollectionValue = admin.getCollection();
                        admin.setCollection(false);
                        String nameF = this.ref.checkIndex(value.getClass());
                        Method getF = this.ref.obtainGetter(value.getClass(), nameF);
                        int valueId = (Integer) getF.invoke(value, new Object[0]);
                        switch (oper) {
                            case SAVE:
                                if (valueId == 0) {
                                    objs[1] = admin.saveGetIndex(value);
                                } else {
                                    objs[1] = valueId;
                                }
                                array.add(objs);
                                break;
                            case MODIFY:
                                if (valueId > 0) {
                                    admin.modify(value);
                                    objs[1] = valueId;
                                } else {
                                    objs[1] = admin.saveGetIndex(value);
                                }
                                array.add(objs);
                                break;
                        }
                        admin.setCollection(tempCollectionValue);
                    } else {
                        objs[1] = -1;
                        array.add(objs);
                    }
                } else {
                    body.setDataType(Properties.TYPES.PRIMITIVE);
                    objs[1] = value;
                    array.add(objs);
                }
            } catch (IllegalAccessException e) {
                return null;
            } catch (java.lang.reflect.InvocationTargetException ex) {
                return null;
            }
            dictionary.addData(body);
        }
        dictionary.closeDictObject(object.getClass().getName());

        if (sizeCollectionInt != 0) {
            this.sizeCollection.push(sizeCollectionInt);
        }
        this.hasParent = tempParent;
        boolean tempCollec = admin.getCollection();
        admin.setCollection(false);

        if (this.hasParent && ((oper == OPERATION.SAVE)
                || (oper == OPERATION.MODIFY))) {
            this.hasParent = false;
            int index = this.completeParentData(admin, object, oper);
            array.add(new Object[]{"parent_id", index});
        }
        admin.setCollection(tempCollec);
        array.add(statement);
        for (int i = this.primaryKey.size() - 1; i >= primKeyItems; i--) {
            this.primaryKey.removeElementAt(i);
        }

        this.ref.executeAction(this.executeAfter.pop(), object);
        return array;
    }

    public Object result2Object(AdminBase admin, Object object, ResultSet rs) {
        EntityDictionary dictionary = new EntityDictionary();
        try {
            if (dictionary.contains(object.getClass().toString())) {
                return dictionary.result2Object(admin, this, object, rs);
            }
        } catch (DictionaryIncompleteException excep) {
        }

        String table1 = this.readClassName(object);
        this.executeAfter.pop();
        dictionary.newDictObject(table1, hasParent, null);
        boolean tempParent = this.hasParent;
        this.hasParent = false;

        boolean searchId = true;
        if (View.class.isInstance(object)) {
            searchId = false;
            this.primaryKeyValue.push(1);
        }
        int primKeyItems = this.primaryKey.size();
        this.primaryKey.push("id");
        boolean ignore = false;

        Field fields[] = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            DictionaryBody body = new DictionaryBody();
            String field = fields[i].getName();
            body.setFieldName(field);
            String name = field;
            Annotation ann = null;
            Object value = null;

            Annotation annotations[] = fields[i].getAnnotations();
            try {
                for (Annotation a : annotations) {
                    if (a instanceof Validation) {
                        body.setValidation((Validation) a);
                    }
                    if (!(a instanceof Column)) {
                        continue;
                    }

                    if (((Column) a).name().length() != 0) {
                        name = ((Column) a).name();
                    }
                    ann = a;
                    if (((Column) a).type() == Properties.TYPES.PRIMARYKEY) {
                        body.setDataType(Properties.TYPES.PRIMARYKEY);
                        value = rs.getObject(name);
                        this.primaryKeyValue.push((Integer) value);
                        searchId = false;
                    }
                    ignore = ((Column) a).ignore();
                    if(((Column)a).summary().length() != 0){
                        body.setDataType(Properties.TYPES.PRIMITIVE);
                        body.setSummary(true);
                        Method setter = this.ref.obtainSetter(object.getClass(), field);
                        body.setSet(setter);
                        value = rs.getDouble(name);
                        setter.invoke(object, new Object[]{value});
                        ignore = true;
                    }
                }
                body.setColName(name);

                if (searchId) {
                    searchId = false;
                    value = rs.getObject("id");
                    this.primaryKeyValue.push((Integer) value);
                }
                if (ignore) {
                    ignore = false;
                    continue;
                }

                Method setter = this.ref.obtainSetter(object.getClass(), field);
                Method getter = this.ref.obtainGetter(object.getClass(), field);
                body.setGet(getter);
                body.setSet(setter);
                Object get = getter.invoke(object, new Object[0]);
                //When the object is not initialized
                if (get == null) {
                    get = this.ref.emptyInstance(getter.getReturnType().getName());
                }

                if (this.ref.implementsCollection(get.getClass(), ann)) {
                    body.setDataType(Properties.TYPES.COLLECTION);
                    Class clazz2 = this.ref.obtainItemCollectionType(object.getClass(), field);
                    body.setCollectionClass(clazz2);
                    String table2 = this.ref.readTableName(clazz2);
                    String fieldName = field.substring(0, 1).toUpperCase()
                            + field.substring(1);
                    String tempName = table1 + table2 + fieldName;
                    String tempTableName = table2 + table1 + fieldName;

                    String forColumn = "base";
                    if (!admin.checkTableExist(tempName)
                            && admin.checkTableExist(tempTableName)) {
                        tempName = tempTableName;
                        forColumn = "related";
                    }
                    this.nameCollection.push(tempName);
                    admin.setCollectionHasName(true);

                    //Supposed that "id" was readed before
                    ArrayList results;
                    if (this.ref.checkPrimitivesExtended(clazz2, null)) {
                        results = admin.obtainAll(PrimitiveCollec.class,
                                forColumn + "=" + this.primaryKeyValue.peek());
                        int lengthPrimitives = results.size();
                        for (int q = 0; q < lengthPrimitives; q++) {
                            results.set(q, ((PrimitiveCollec) results.get(q)).getObject());
                        }
                    } else {
                        results = admin.obtainAll(M2mTable.class,
                                forColumn + "=" + this.primaryKeyValue.peek());
                        admin.setCollectionHasName(false);
                        results = this.restoreCollection(admin, results,
                                object, name, forColumn, tempName);
                    }

                    Object valueCollection = this.ref.emptyInstance(get.getClass());
                    ((Collection) valueCollection).addAll(results);
                    admin.setCollectionHasName(false);
                    setter.invoke(object, new Object[]{valueCollection});
                    dictionary.addData(body);
                    continue;
                } else {
                    value = rs.getObject(name);
                }

                //For Foreign Keys
                boolean isForeign = !this.ref.checkPrimitivesExtended(get.getClass(), ann);
                if (this.dropDown && isForeign) {
                    body.setDataType(Properties.TYPES.FOREIGNKEY);
                    Integer index = (Integer) value;
                    String indexName = this.ref.checkIndex(get.getClass());

                    if (admin.obtainWhere(get, indexName + "=" + index)) {
                        setter.invoke(object, new Object[]{get});
                    }
                } else if (!isForeign) {
                    body.setDataType(Properties.TYPES.PRIMITIVE);
                    if (value instanceof Timestamp) {
                        setter.invoke(object, new Object[]{
                                    this.ref.manageTimeData(get.getClass(),
                                    ((Timestamp) value))});
                    } else {
                        setter.invoke(object, new Object[]{value});
                    }
                } else {
                    body.setDataType(Properties.TYPES.FOREIGNKEY);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            dictionary.addData(body);
        }
        dictionary.closeDictObject(object.getClass().getName());
        this.hasParent = tempParent;

        if (this.hasParent) {
            this.hasParent = false;
            this.restoreParent(admin, object, rs);
        }
        for (int i = this.primaryKey.size() - 1; i >= primKeyItems; i--) {
            this.primaryKey.removeElementAt(i);
            this.primaryKeyValue.removeElementAt(i);
        }

        return object;
    }

    private void restoreParent(AdminBase admin, Object child, ResultSet rs) {
        Object parent;
        if ((this.originalChild.size() == 0)
                || (!this.originalChild.peek().getClass().isInstance(child))) {
            this.originalChild.push(child);
        }

        try {
            parent = this.ref.emptyInstance(child.getClass().getSuperclass().getName());
            String sql = "";
            boolean primary = true;

            Field fields[] = parent.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i].getName();
                String name = field;

                Annotation annotations[] = fields[i].getAnnotations();
                for (Annotation a : annotations) {
                    if (!(a instanceof Column)) {
                        continue;
                    }
                    if (((Column) a).name().length() != 0) {
                        name = ((Column) a).name();
                    }

                    if (((Column) a).type() == Properties.TYPES.PRIMARYKEY) {
                        sql = name + "=" + rs.getObject("parent_id");
                        admin.obtainWhere(parent, sql);
                    }
                }

                if (sql.length() == 0) {
                    sql = field + "='" + rs.getObject("parent_id") + "'";
                    admin.obtainWhere(parent, sql);
                }
                if (primary) {
                    primary = false;
                    String indexSon = this.ref.checkIndex(child.getClass());
                    String indexParent = this.ref.checkIndex(parent.getClass());
                    if (indexSon.equals(indexParent)) {
                        continue;
                    }
                }

                Method getter = this.ref.obtainGetter(parent.getClass(), field);
                Object value = getter.invoke(parent, new Object[0]);
                Method setter = this.ref.obtainSetter(parent.getClass(), field);

                if (child.getClass() == this.originalChild.peek().getClass()) {
                    setter.invoke(child, new Object[]{value});
                } else {
                    setter.invoke(this.originalChild.peek(), new Object[]{value});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (child.getClass() == this.originalChild.peek().getClass()) {
                this.originalChild.pop();
            }
        }
    }

    public Object[] mappingDefinition(AdminBase admin, Object object) {
        ArrayList array = new ArrayList();
        Definition def = new Definition(admin.getDB());
        boolean primary = false;
        boolean collectionBool;

        array.add(this.readClassName(object));

        Field fields[] = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Annotation ann[] = fields[i].getAnnotations();
            ColumnDefined colDef;
            switch (admin.getDB()) {
                case POSTGRES:
                    colDef = new ColumnDefinedPostgre();
                    break;
                default:
                    colDef = new ColumnDefined();
                    break;
            }
            String name = fields[i].getName();
            collectionBool = false;

            for (int j = 0; j < ann.length; j++) {
                if (ann[j] instanceof Column) {
                    if ((((Column) ann[j]).type() == Properties.TYPES.COLLECTION)
                            || ((Column) ann[j]).ignore()) {
                        collectionBool = true;
                        continue;
                    }
                    if (((Column) ann[j]).name().length() != 0) {
                        name = ((Column) ann[j]).name();
                    }
                } else if (ann[j] instanceof ColumnDefinition) {
                    colDef.setType(def.obtainDataType(
                            ((ColumnDefinition) ann[j]).type()));
                    colDef.setLength(((ColumnDefinition) ann[j]).length());
                    colDef.setNotNull(((ColumnDefinition) ann[j]).notNull());
                    colDef.setDefaultValue(
                            ((ColumnDefinition) ann[j]).defaultValue());
                    colDef.setAutoIncrement(
                            ((ColumnDefinition) ann[j]).autoIncrement());
                    colDef.setUnique(((ColumnDefinition) ann[j]).unique());
                    colDef.setPrimary(((ColumnDefinition) ann[j]).primary());
                    colDef.setFormat(def.obtainColumnFormat(
                            ((ColumnDefinition) ann[j]).format()));
                    colDef.setStorage(def.obtainStorage(
                            ((ColumnDefinition) ann[j]).storage()));
                }
            }
            colDef.setName(name);

            if (primary) {
                array.add(1, colDef);
                primary = false;
            } else if (!collectionBool) {
                array.add(colDef);
            }
        }

        return array.toArray();
    }

    private int completeParentData(AdminBase admin, Object child, OPERATION oper) {
        Object parent;
        int index = 0;
        if ((this.originalChild.size() == 0)
                || (!child.getClass().isInstance(this.originalChild.peek()))) {
            this.originalChild.push(child);
        }
        try {
            parent = this.ref.emptyInstance(child.getClass().getSuperclass().getName());

            Field fields[] = parent.getClass().getDeclaredFields();
            this.primaryKey.push("id");
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i].getName();

                Annotation annotations[] = fields[i].getAnnotations();
                for (Annotation a : annotations) {
                    if (!(a instanceof Column)) {
                        continue;
                    }

                    if ((((Column) a).name().length() != 0)
                            && (((Column) a).type() == Properties.TYPES.PRIMARYKEY)) {
                        this.primaryKey.pop();
                        this.primaryKey.push(((Column) a).name());
                    }
                }

                Method getter = this.ref.obtainGetter(parent.getClass(), field);
                Object value;
                if (child.getClass() == this.originalChild.peek().getClass()) {
                    value = getter.invoke(child, new Object[0]);
                } else {
                    value = getter.invoke(this.originalChild.peek(), new Object[0]);
                }
                Method setter = this.ref.obtainSetter(parent.getClass(), field);

                setter.invoke(parent, new Object[]{value});
            }

            if (oper == OPERATION.SAVE) {
                index = admin.saveGetIndex(parent);
            } else {
                if (oper == OPERATION.MODIFY) {
                    admin.modify(parent);
                }
                Method getter = this.ref.obtainGetter(parent.getClass(), this.primaryKey.peek());
                index = (Integer) getter.invoke(parent, new Object[0]);
            }
        } catch (Exception e) {
            return -1;
        } finally {
            if (child.getClass() == this.originalChild.peek().getClass()) {
                this.originalChild.pop();
            }
            this.primaryKey.pop();
        }

        return index;
    }

    private String readClassName(Object object) {
        this.hasParent = false;
        Annotation entity[] = object.getClass().getAnnotations();
        String entityName = object.getClass().getSimpleName();
        for (int i = 0; i < entity.length; i++) {
            if (entity[i] instanceof Table){
                if(((Table) entity[i]).value().length() != 0) {
                    entityName = ((Table) entity[i]).value();
                }
                this.ref.executeAction(((Table) entity[i]).before(), object);
                this.executeAfter.push(((Table) entity[i]).after());
                this.action = ((Table) entity[i]);
            } else if (entity[i] instanceof Parent) {
                this.hasParent = true;
            }
        }
        if (entity.length == 0){
            this.executeAfter.push(new String[]{""});
            if(object.getClass().getSuperclass().getPackage()
                == object.getClass().getPackage()) {
                this.hasParent = true;
            }
        }

        return entityName;
    }

    public boolean isCacheable(Class clazz){
        Annotation entity[] = clazz.getAnnotations();
        for (int i = 0; i < entity.length; i++) {
            if (entity[i] instanceof Table){
                return ((Table)entity[i]).cache() || ((Table)entity[i]).cacheUpdate();
            }
        }
        return false;
    }

    public ArrayList obtainCache(String sql, AdminBase admin, Class clazz){
        if(this.cacheStore.containsKey(clazz.getName() + sql)){
            CacheManager cache = this.cacheStore.get(clazz.getName() + sql);
            switch(cache.getCacheType()){
                case 0:
                    return cache.getData();
                case 1:
                    CacheManager cacheUpdate = new CacheManager();
                    admin.setCollectionHasName(true);
                    this.nameCollection.push(cache.getTableName());
                    admin.obtainWhere(cacheUpdate, "id > 0");
                    admin.setCollectionHasName(false);
                    if(cacheUpdate.getId() == cache.getId()){
                        return cache.getData();
                    }
                    this.cacheStore.remove(clazz.getName() + sql);
                    return admin.obtainAll(clazz, sql);
            }
        }

        return null;
    }

    public void makeCacheable(String sql, ArrayList array, Class clazz, AdminBase admin){
        CacheManager cache = new CacheManager(array);
        Annotation entity[] = clazz.getAnnotations();
        for (int i = 0; i < entity.length; i++) {
            if (entity[i] instanceof Table){
                if(((Table)entity[i]).cache()){
                    cache.setCacheType(0);
                }else if(((Table)entity[i]).cacheUpdate()){
                    cache.setCacheType(1);
                    cache.setTableName(clazz.getSimpleName() + "CacheUpdate");
                    admin.setCollectionHasName(true);
                    this.nameCollection.push(cache.getTableName());
                    admin.obtainWhere(cache, "id > 0");
                    admin.setCollectionHasName(false);
                }
                break;
            }
        }
        this.cacheStore.put(clazz.getName() + sql, cache);
    }

    public void updateCache(Class clazz, AdminBase admin){
        Annotation entity[] = clazz.getAnnotations();
        for (int i = 0; i < entity.length; i++) {
            if (entity[i] instanceof Table){
                if(((Table)entity[i]).cacheUpdate()){
                    String tableName = clazz.getSimpleName() + "CacheUpdate";
                    CacheManager cacheUpdate = new CacheManager();
                    if(admin.checkTableExist(tableName)){
                        admin.executeQuery("DELETE FROM " + tableName);
                    }
                    admin.setForceTable(true);
                    admin.setTableForcedName(tableName);
                    admin.save(cacheUpdate);
                    admin.setForceTable(false);
                }
                break;
            }
        }
    }

    ArrayList restoreCollection(AdminBase admin, ArrayList items,
            Object object, String field, String forColumn, String table) {
        ArrayList results = new ArrayList();
        //obtain object type from array
        Class objCollec = this.ref.obtainItemCollectionType(object.getClass(), field);

        //Results size (elements related to this object)
        int size = items.size();
        for (int q = 0; q < size; q++) {
            if (!this.manyRestore.contains(table + "-" + ((M2mTable) items.get(q)).getBase()
                    + "-" + ((M2mTable) items.get(q)).getRelated())) {
                //Add this object to the ArrayList to avoid repetition
                this.manyRestore.add(table + "-" + ((M2mTable) items.get(q)).getBase()
                        + "-" + ((M2mTable) items.get(q)).getRelated());

                Object objC = this.ref.emptyInstance(objCollec.getName());
                int index = ((M2mTable) items.get(q)).getRelated();
                if (forColumn.equalsIgnoreCase("related")) {
                    index = ((M2mTable) items.get(q)).getBase();
                }
                String sql = this.ref.checkIndex(objC.getClass()) + "=" + index;

                admin.obtainWhere(objC, sql);
                results.add(objC);
            }
        }

        if ((size > 0) && (this.manyRestore.indexOf(table + "-"
                + ((M2mTable) items.get(0)).getBase()
                + "-" + ((M2mTable) items.get(0)).getRelated()) == 0)) {
            this.manyRestore.clear();
        }

        return results;
    }

    public boolean completeLazyLoad(AdminBase admin, Object obj) {
        boolean result = true;
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                String foreign = f.getName();
                Annotation ann = null;
                Annotation annotations[] = f.getAnnotations();
                if (annotations.length > 0) {
                    for (Annotation a : annotations) {
                        if ((a instanceof Column)
                                && ((Column) ann).name().length() != 0) {
                            foreign = ((Column) ann).name();
                            ann = a;
                        }
                    }
                }

                Method getter = this.ref.obtainGetter(obj.getClass(), f.getName());
                Method setter = this.ref.obtainSetter(obj.getClass(), f.getName());
                Object type = this.ref.emptyInstance(getter.getReturnType().getName());

                if ((type != null) && !this.ref.checkPrimitivesExtended(type.getClass(), ann)) {
                    Object value = getter.invoke(obj, new Object[0]);
                    if (value == null) {
                        String table1 = this.readClassName(obj);
                        String table2 = this.readClassName(type);
                        String idName1 = this.ref.checkIndex(obj.getClass());
                        String idName2 = this.ref.checkIndex(type.getClass());
                        Method getIdValue = this.ref.obtainGetter(obj.getClass(), idName1);
                        int val = (Integer) getIdValue.invoke(obj, new Object[0]);
                        String sql = "SELECT * FROM " + table1 + ", " + table2
                                + " WHERE " + table1 + "." + idName1 + " = " + String.valueOf(val)
                                + " AND " + table2 + "." + idName2 + " = " + table1 + "." + foreign;
                        admin.obtainSelect(type, sql);
                        value = type;
                    } else {
                        result = admin.lazyLoad(value);
                    }
                    setter.invoke(obj, new Object[]{value});
                }
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<String[]> getCollectionsTableForDelete(Object obj, AdminBase admin) {
        ArrayList<String[]> collec = new ArrayList<String[]>();

        String tableBase = this.ref.readTableName(obj.getClass());

        try {
            Field fields[] = obj.getClass().getDeclaredFields();
            for (Field f : fields) {
                String tableRelated = "";
                Annotation ann = null;
                Annotation annotations[] = f.getAnnotations();
                for (Annotation a : annotations) {
                    if (a instanceof Column) {
                        ann = a;
                    } else {
                        continue;
                    }
                }
                Method getter = this.ref.obtainGetter(obj.getClass(), f.getName());
                Object value = getter.invoke(obj, new Object[0]);
                if (value == null) {
                    value = this.ref.emptyInstance(getter.getReturnType().getName());
                }
                if (this.ref.implementsCollection(value.getClass(), ann)) {
                    String col[] = new String[2];
                    if (tableRelated.length() == 0) {
                        tableRelated = this.ref.readTableName(
                                this.ref.obtainItemCollectionType(
                                obj.getClass(), f.getName()));
                        col[0] = tableBase + tableRelated;
                        col[1] = tableRelated + tableBase;
                    } else {
                        col[0] = tableRelated;
                        col[1] = tableRelated;
                    }

                    collec.add(col);
                }
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }

        return collec;
    }

    public boolean deleteMany2Many(AdminBase admin, Object object, ArrayList<String> manys) {
        boolean value = false;

        try {
            admin.getConex().initTransaction();
            for (String s : manys) {
                Field f = object.getClass().getDeclaredField(s);

                Method getter = this.ref.obtainGetter(object.getClass(), s);
                Object obj = getter.invoke(object, new Object[0]);

                Object array[] = ((Collection) obj).toArray();
                if (array.length > 0) {
                    String fieldName = s.substring(0, 1).toUpperCase()
                            + s.substring(1);
                    String table1 = this.ref.readTableName(object.getClass());
                    String table2 = this.ref.readTableName(array[0].getClass());
                    String relation = table1 + table2 + fieldName;
                    if (!admin.checkTableExist(relation)) {
                        relation = table2 + table1 + fieldName;
                    }
                    for (Object o2 : array) {
                        this.modifyComponents(admin, o2);
                    }
                }

                admin.delete(object);
                String idName = this.ref.checkIndex(object.getClass());
                Method setter = this.ref.obtainSetter(object.getClass(), idName);
                setter.invoke(object, new Object[]{0});
            }
            admin.getConex().confirmTransaction();
            value = true;
        } catch (Exception e) {
            try {
                admin.getConex().cancelTransaction();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return value;
    }

    private void modifyComponents(AdminBase admin, Object object) {
        ArrayList<String> manys = this.ref.isMany2Many(object.getClass());
        if (!manys.isEmpty()) {
            this.deleteMany2Many(admin, object, manys);
        }
    }

    public boolean deleteParent(AdminBase admin, Object object) throws Exception {
        boolean value = false;
        //Complete Parent Data
        if (this.ref.hasParent(object.getClass())) {
            int index = this.completeParentData(admin, object,
                    EntityManager.OPERATION.DELETE);
            Object parent = this.ref.emptyInstance(
                    object.getClass().getSuperclass().getName());
            String idName = this.ref.checkIndex(parent.getClass());
            Method setter = this.ref.obtainSetter(parent.getClass(), idName);
            setter.invoke(parent, new Object[]{index});
            admin.delete(parent);
            value = true;
        }

        return value;
    }

    public boolean checkOptimisticLock(AdminBase admin, Object object){
        boolean value = true;
        try{
            EntityDictionary dictionary = new EntityDictionary();
            if (dictionary.contains(object.getClass().getName())) {
                Object[] data = dictionary.obtainDataOptimisticLock(object);
                if(data != null){
                    ResultSet rs = admin.getConex().select("SELECT optimisticLock FROM " +
                            ((String)data[0]) + " WHERE " + ((String)data[1]) +
                            "=" + data[2].toString());
                    if(rs.next()){
                        long timestamp = rs.getLong(1);
                        value = ( ((Long)data[3]) == timestamp );
                        if(value){
                            ((Method)data[4]).invoke(object, new Object[]{System.currentTimeMillis()});
                        }
                    }
                }
            }
        }catch(Exception e){
        }

        return value;
    }

    public void setDropDown(boolean dropDown) {
        this.dropDown = dropDown;
    }

    public ArrayList getCollection() {
        return collection.pop();
    }

    public int sizeCollectionStack() {
        return this.sizeCollection.pop();
    }

    public String getNameCollection() {
        return this.nameCollection.pop();
    }

    public ReflectionUtilities getRef() {
        return ref;
    }

    public void cleanStack() {
        this.nameCollection.clear();
        this.primaryKey.clear();
        this.primaryKeyValue.clear();
    }

    public void cleanCache(){
        this.cacheStore = new Hashtable<String, CacheManager>();
    }
}
