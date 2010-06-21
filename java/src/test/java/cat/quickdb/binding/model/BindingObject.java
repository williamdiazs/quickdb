package cat.quickdb.binding.model;

import quickdb.db.AdminBinding;
import java.sql.Date;

public class BindingObject extends AdminBinding{

    private int id;
    private String name;
    private Date birth;
    private double salary;

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getBirth() {
        return birth;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

}
