package cat.quickdb.complexDataStructure.model;

import java.util.ArrayList;

/**
 *
 * @author Diego Sarmentero
 */
public class ParentOfSon {

    private int id;
    private String parentValue;
    private ArrayList parentCollection;

    public void setId(int id) {
        this.id = id;
    }

    public void setParentCollection(ArrayList parentCollection) {
        this.parentCollection = parentCollection;
    }

    public void setParentValue(String parentValue) {
        this.parentValue = parentValue;
    }

    public int getId() {
        return id;
    }

    public ArrayList getParentCollection() {
        return parentCollection;
    }

    public String getParentValue() {
        return parentValue;
    }

}
