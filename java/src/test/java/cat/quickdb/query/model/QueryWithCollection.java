package cat.quickdb.query.model;

import java.util.ArrayList;

/**
 *
 * @author Diego Sarmentero
 */
public class QueryWithCollection {

    private int id;
    private String description;
    private ArrayList<QueryCollection> queryCollection;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setQueryCollection(ArrayList<QueryCollection> queryCollection) {
        this.queryCollection = queryCollection;
    }

    public ArrayList<QueryCollection> getQueryCollection() {
        return queryCollection;
    }

}
