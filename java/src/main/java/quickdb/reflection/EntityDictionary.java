package quickdb.reflection;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import quickdb.annotation.Table;
import quickdb.db.AdminBase;
import quickdb.db.View;
import quickdb.exception.DictionaryIncompleteException;
import quickdb.modelSupport.M2mTable;
import quickdb.modelSupport.PrimitiveCollec;
import quickdb.reflection.EntityManager.OPERATION;
import quickdb.util.Validations;

/**
 *
 * @author Diego Sarmentero
 */
public class EntityDictionary {

    private static Hashtable<String, DictionaryData> dict = new Hashtable<String, DictionaryData>();
    private DictionaryData dictObject;

    public boolean contains(String key){
        return EntityDictionary.dict.containsKey(key);
    }

    public void newDictObject(String table, boolean hasParent, Table action){
        this.dictObject = new DictionaryData(table, hasParent, action);
    }

    public void addData(DictionaryBody body){
        this.dictObject.addData(body);
    }

    public void closeDictObject(String key){
        if(!key.equalsIgnoreCase("quickdb.modelSupport.M2mTable") &&
                !key.equalsIgnoreCase("quickdb.modelSupport.PrimitiveCollec")){
            EntityDictionary.dict.put(key, dictObject);
        }
    }

    public static void cleanDictionary(){
        EntityDictionary.dict = new Hashtable<String, DictionaryData>();
    }

    public ArrayList entity2Array(AdminBase admin, EntityManager manager,
            Object object, OPERATION oper) {
        DictionaryData data = EntityDictionary.dict.get(object.getClass().getName());

        ArrayList array = new ArrayList();
        array.add(data.getTableName());
        manager.getRef().executeAction(data.getAction().before(), object);
        String statement = "";
        manager.hasParent = false;

        int primKeyItems = manager.primaryKey.size();
        int sizeCollectionInt = 0;
        manager.primaryKey.push(data.getData().get(0).colName());
        try{
            for(DictionaryBody body : data.getData()){
                Object objs[] = new Object[2];
                objs[0] = body.colName();

                if(body.validation() != null){
                    if (!Validations.isValidField(object, body.fieldName(),
                            body.validation(), manager.getRef())) {
                        return null;
                    }
                }

                Object value = body.get().invoke(object, new Object[0]);
                boolean wasNull = false;
                if (value == null) {
                    wasNull = true;
                    value = manager.getRef().emptyInstance(body.get().getReturnType().getName());
                }

                switch(body.dataType()){
                    case COLLECTION:
                        admin.setCollection(true);
                        admin.setCollectionHasName(true);
                        manager.nameCollection.push(manager.getRef().readTableName(body.collectionClass())
                            + body.fieldName().substring(0, 1).toUpperCase() + body.fieldName().substring(1));
                        if (manager.getRef().checkPrimitivesExtended(body.collectionClass(), null)) {
                            Object[] arrayPrimitive = ((Collection) value).toArray();
                            ArrayList primitiveResult = new ArrayList();
                            for (Object prim : arrayPrimitive) {
                                PrimitiveCollec primitive = new PrimitiveCollec(prim);
                                primitiveResult.add(primitive);
                            }
                            switch (oper) {
                                case SAVE:
                                case MODIFY:
                                    manager.collection.push(primitiveResult);
                                    sizeCollectionInt++;
                                    break;
                            }
                        } else {
                            switch (oper) {
                                case SAVE:
                                    manager.collection.push(admin.saveAll(((Collection) value)));
                                    sizeCollectionInt++;
                                    break;
                                case MODIFY:
                                    manager.collection.push(admin.modifyAll(((Collection) value)));
                                    sizeCollectionInt++;
                                    break;
                            }
                        }

                        if (sizeCollectionInt == 0) {
                            manager.nameCollection.pop();
                        }
                        admin.setCollectionHasName(false);
                        break;
                    case FOREIGNKEY:
                        if (manager.dropDown && !wasNull) {
                            boolean tempCollectionValue = admin.getCollection();
                            admin.setCollection(false);
                            if(EntityDictionary.dict.containsKey(value.getClass().getName())){
                                DictionaryData data2 = EntityDictionary.dict.get(value.getClass().getName());
                                int valueId = (Integer) data2.getData().get(0).get().invoke(value, new Object[0]);
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
                            }
                        } else {
                            objs[1] = -1;
                            array.add(objs);
                        }
                        break;
                    case PRIMITIVE:
                        objs[1] = value;
                        array.add(objs);
                        break;
                }

            }
            if (oper == OPERATION.MODIFY
                    || oper == OPERATION.DELETE) {
                String nameSta = data.getData().get(0).colName();
                Integer valueId = (Integer) data.getData().get(0).get().invoke(object, new Object[0]);
                statement = nameSta + "=" + valueId;
            } else {
                statement = manager.primaryKey.peek() + " > 0";
            }
        }catch(Exception e){
            throw new DictionaryIncompleteException();
        }

        if (sizeCollectionInt != 0) {
            manager.sizeCollection.push(sizeCollectionInt);
        }
        boolean tempCollec = admin.getCollection();
        admin.setCollection(false);

        if (data.isHasParent() && ((oper == OPERATION.SAVE)
                || (oper == OPERATION.MODIFY))) {
            int index = this.completeParentData(admin, manager, object, oper);
            array.add(new Object[]{"parent_id", index});
        }
        admin.setCollection(tempCollec);
        array.add(statement);
        for (int i = manager.primaryKey.size() - 1; i >= primKeyItems; i--) {
            manager.primaryKey.removeElementAt(i);
        }

        manager.getRef().executeAction(data.getAction().after(), object);
        return array;
    }

    public Object result2Object(AdminBase admin, EntityManager manager,
            Object object, ResultSet rs) {
        DictionaryData data = EntityDictionary.dict.get(object.getClass().getName());

        ArrayList array = new ArrayList();
        array.add(data.getTableName());
        manager.hasParent = false;
        Object value = null;

        if (View.class.isInstance(object)) {
            manager.primaryKeyValue.push(1);
        }
        int primKeyItems = manager.primaryKey.size();
        manager.primaryKey.push(data.getData().get(0).colName());
        try{
            for(DictionaryBody body : data.getData()){
                Object get = body.get().invoke(object, new Object[0]);
                //When the object is not initialized
                if (get == null) {
                    get = manager.getRef().emptyInstance(body.get().getReturnType().getName());
                }

                switch(body.dataType()){
                    case COLLECTION:
                        String table2 = manager.getRef().readTableName(body.collectionClass());
                        String fieldName = body.fieldName().substring(0, 1).toUpperCase()
                                + body.fieldName().substring(1);
                        String tempName = data.getTableName() + table2 + fieldName;
                        String tempTableName = table2 + data.getTableName() + fieldName;

                        String forColumn = "base";
                        if (!admin.checkTableExist(tempName)
                                && admin.checkTableExist(tempTableName)) {
                            tempName = tempTableName;
                            forColumn = "related";
                        }
                        manager.nameCollection.push(tempName);
                        admin.setCollectionHasName(true);

                        //Supposed that "id" was readed before
                        ArrayList results;
                        if (manager.getRef().checkPrimitivesExtended(body.collectionClass(), null)) {
                            results = admin.obtainAll(PrimitiveCollec.class,
                                    forColumn + "=" + manager.primaryKeyValue.peek());
                            int lengthPrimitives = results.size();
                            for (int q = 0; q < lengthPrimitives; q++) {
                                results.set(q, ((PrimitiveCollec) results.get(q)).getObject());
                            }
                        } else {
                            results = admin.obtainAll(M2mTable.class,
                                    forColumn + "=" + manager.primaryKeyValue.peek());
                            admin.setCollectionHasName(false);
                            results = manager.restoreCollection(admin, results,
                                    object, body.colName(), forColumn, tempName);
                        }

                        Object valueCollection = manager.getRef().emptyInstance(get.getClass());
                        ((Collection) valueCollection).addAll(results);
                        admin.setCollectionHasName(false);
                        body.set().invoke(object, new Object[]{valueCollection});
                        break;
                    case FOREIGNKEY:
                        value = rs.getObject(body.colName());
                        if (manager.dropDown) {
                            Integer index = (Integer) value;
                            if(!this.contains(get.getClass().getName())){
                                throw new DictionaryIncompleteException();
                            }
                            String indexName = EntityDictionary.dict.get(get.getClass().getName()).
                                    getData().get(0).colName();

                            if (admin.obtainWhere(get, indexName + "=" + index)) {
                                body.set().invoke(object, new Object[]{get});
                            }
                        }
                        break;
                    case PRIMITIVE:
                        if (value instanceof Timestamp) {
                            body.set().invoke(object, new Object[]{
                                        manager.getRef().manageTimeData(get.getClass(),
                                        ((Timestamp) value))});
                        } else {
                            body.set().invoke(object, new Object[]{value});
                        }
                        break;
                }
            }
        }catch(Exception e){
            throw new DictionaryIncompleteException();
        }
        if (data.isHasParent()) {
            this.restoreParent(admin, manager, object, rs);
        }
        for (int i = manager.primaryKey.size() - 1; i >= primKeyItems; i--) {
            manager.primaryKey.removeElementAt(i);
            manager.primaryKeyValue.removeElementAt(i);
        }

        return object;
    }

    private int completeParentData(AdminBase admin, EntityManager manager,
            Object child, OPERATION oper) {
        Object parent;
        int index = 0;
        if ((manager.originalChild.size() == 0)
                || (!child.getClass().isInstance(manager.originalChild.peek()))) {
            manager.originalChild.push(child);
        }
        try {
            parent = manager.getRef().emptyInstance(child.getClass().getSuperclass().getName());

            if(!this.contains(parent.getClass().getName())){
                throw new DictionaryIncompleteException();
            }
            DictionaryData data = EntityDictionary.dict.get(parent.getClass().getName());

            manager.primaryKey.push(data.getData().get(0).colName());
            for (DictionaryBody body : data.getData()) {
                Object value;
                if (child.getClass() == manager.originalChild.peek().getClass()) {
                    value = body.get().invoke(child, new Object[0]);
                } else {
                    value = body.get().invoke(manager.originalChild.peek(), new Object[0]);
                }

                body.set().invoke(parent, new Object[]{value});
            }

            if (oper == OPERATION.SAVE) {
                index = admin.saveGetIndex(parent);
            } else {
                if (oper == OPERATION.MODIFY) {
                    admin.modify(parent);
                }
                index = (Integer) data.getData().get(0).get().invoke(parent, new Object[0]);
            }
        } catch (Exception e) {
            return -1;
        } finally {
            if (child.getClass() == manager.originalChild.peek().getClass()) {
                manager.originalChild.pop();
            }
            manager.primaryKey.pop();
        }

        return index;
    }

    private void restoreParent(AdminBase admin, EntityManager manager, Object child, ResultSet rs) {
        Object parent;
        if ((manager.originalChild.size() == 0)
                || (!manager.originalChild.peek().getClass().isInstance(child))) {
            manager.originalChild.push(child);
        }

        try {
            parent = manager.getRef().emptyInstance(child.getClass().getSuperclass().getName());
            
            if(!this.contains(parent.getClass().getName())){
                throw new DictionaryIncompleteException();
            }
            DictionaryData data = EntityDictionary.dict.get(parent.getClass().getName());

            String sql = data.getData().get(0).colName() + "=" + rs.getObject("parent_id");
            admin.obtainWhere(parent, sql);
            boolean primary = true;

            for (DictionaryBody body : data.getData()) {
                if (primary) {
                    primary = false;
                    if (data.getData().get(0).colName().equals(
                        EntityDictionary.dict.get(parent.getClass().getName()).
                        getData().get(0).colName())) {
                        continue;
                    }
                }

                Object value = body.get().invoke(parent, new Object[0]);

                if (child.getClass() == manager.originalChild.peek().getClass()) {
                    body.set().invoke(child, new Object[]{value});
                } else {
                    body.set().invoke(manager.originalChild.peek(), new Object[]{value});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (child.getClass() == manager.originalChild.peek().getClass()) {
                manager.originalChild.pop();
            }
        }
    }
}


class DictionaryData{

    private String tableName;
    private boolean hasParent;
    private Table action;
    private ArrayList<DictionaryBody> data;

    public DictionaryData(String tableName, boolean hasParent, Table action) {
        this.tableName = tableName;
        this.hasParent = hasParent;
        this.action = action;
        this.data = new ArrayList<DictionaryBody>();
    }

    public void addData(DictionaryBody bodyData){
        this.data.add(bodyData);
    }

    public Table getAction() {
        return action;
    }

    public ArrayList<DictionaryBody> getData() {
        return data;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    public String getTableName() {
        return tableName;
    }

}