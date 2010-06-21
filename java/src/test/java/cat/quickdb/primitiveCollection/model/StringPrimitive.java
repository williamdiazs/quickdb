package cat.quickdb.primitiveCollection.model;

import quickdb.annotation.Column;
import java.util.ArrayList;

public class StringPrimitive {

    private int id;
    private String type;
    @Column(collectionClass="java.lang.String")
    private ArrayList strings;

    public void setId(int id) {
        this.id = id;
    }

    public void setStrings(ArrayList strings) {
        this.strings = strings;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public ArrayList getStrings() {
        return strings;
    }

    public String getType() {
        return type;
    }

}
