package cat.quickdb.primitiveCollection.model;

import quickdb.annotation.Column;
import java.util.ArrayList;

public class DoublePrimitive {

    private int id;
    private String type;
    @Column(collectionClass="java.lang.Double")
    private ArrayList doubles;

    public void setDoubles(ArrayList doubles) {
        this.doubles = doubles;
    }

    public ArrayList getDoubles() {
        return doubles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

}
