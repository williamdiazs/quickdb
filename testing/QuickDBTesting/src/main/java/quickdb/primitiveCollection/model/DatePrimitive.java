package quickdb.primitiveCollection.model;

import quickdb.annotation.Column;
import java.util.ArrayList;

public class DatePrimitive {

    private int id;
    private String type;
    @Column(collectionClass="java.sql.Date")
    private ArrayList dates;

    public void setDates(ArrayList dates) {
        this.dates = dates;
    }

    public ArrayList getDates() {
        return dates;
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
