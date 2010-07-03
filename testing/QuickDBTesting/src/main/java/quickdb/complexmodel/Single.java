package quickdb.complexmodel;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Single {

    private int id;
    private String name;
    private Date date;
    private long number;

    public Single(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

}
