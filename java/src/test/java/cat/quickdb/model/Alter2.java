package cat.quickdb.model;

import quickdb.annotation.Table;

@Table("AlterTable")
public class Alter2 {

    private int id;
    private String name;
    private double extend;

    public void setExtend(double extend) {
        this.extend = extend;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExtend() {
        return extend;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
