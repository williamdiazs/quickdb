package quickdb.primitiveCollection.model;

import quickdb.annotation.Column;
import java.util.ArrayList;

public class BooleanPrimitive {

    private int id;
    private String type;
    @Column(collectionClass="java.lang.Boolean")
    private ArrayList booleans;

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBooleans(ArrayList booleans) {
        this.booleans = booleans;
    }

    public ArrayList getBooleans() {
        return booleans;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

}
