package quickdb.query.model;

import java.sql.Date;

public class CompleteQuery {

    private int id;
    private String name;
    private double salary;
    private int age;
    private Date birth;
    private boolean cond;

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setCond(boolean cond) {
        this.cond = cond;
    }

    public boolean getCond() {
        return cond;
    }

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
