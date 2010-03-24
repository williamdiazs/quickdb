package cat.quickdb.model;

import java.sql.Date;

public class Primitive {

    private int id;
    private int intNumber;
    private double doubleNumber;
    private float floatNumber;
    private String string;
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDoubleNumber(double doubleNumber) {
        this.doubleNumber = doubleNumber;
    }

    public void setFloatNumber(float floatNumber) {
        this.floatNumber = floatNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIntNumber(int intNumber) {
        this.intNumber = intNumber;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Date getDate() {
        return date;
    }

    public double getDoubleNumber() {
        return doubleNumber;
    }

    public float getFloatNumber() {
        return floatNumber;
    }

    public int getId() {
        return id;
    }

    public int getIntNumber() {
        return intNumber;
    }

    public String getString() {
        return string;
    }

}
