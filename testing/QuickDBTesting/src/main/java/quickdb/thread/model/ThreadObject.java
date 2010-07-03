package quickdb.thread.model;

import java.sql.Date;

public class ThreadObject{

    private int id;
    private String name;
    private Date birth;
    private double salary;
    private boolean alive;
    private int age;

    public void setAge(int age) {
        this.age = age;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getAge() {
        return age;
    }

    public boolean getAlive() {
        return alive;
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
