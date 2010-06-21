package cat.quickdb.primitiveCollection.model;

import quickdb.annotation.Column;
import java.util.ArrayList;

public class FloatPrimitive {

    private int id;
    private String type;
    @Column(collectionClass="java.lang.Float")
    private ArrayList floats;

    public void setFloats(ArrayList floats) {
        this.floats = floats;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList getFloats() {
        return floats;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

}
