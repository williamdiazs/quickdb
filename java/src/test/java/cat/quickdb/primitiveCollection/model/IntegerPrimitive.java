package cat.quickdb.primitiveCollection.model;

import cat.quickdb.annotation.Column;
import java.util.ArrayList;

public class IntegerPrimitive {

    private int id;
    private String type;
    @Column(collectionClass="java.lang.Integer")
    private ArrayList integer;

    public void setId(int id) {
        this.id = id;
    }

    public void setInteger(ArrayList integer) {
        this.integer = integer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public ArrayList getInteger() {
        return integer;
    }

    public String getType() {
        return type;
    }

}
