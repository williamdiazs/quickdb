package quickdb.modelSupport;

import java.util.ArrayList;
import quickdb.annotation.Column;

/**
 *
 * @author Diego Sarmentero
 */
public class CacheManager {

    private int id;
    @Column(ignore=true)
    private int cacheType;
    @Column(ignore=true)
    private String tableName;
    @Column(ignore=true)
    private ArrayList data;

    public CacheManager() {
    }

    public CacheManager(ArrayList data) {
        this.data = data;
    }

    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCacheType() {
        return cacheType;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    public ArrayList getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

}
