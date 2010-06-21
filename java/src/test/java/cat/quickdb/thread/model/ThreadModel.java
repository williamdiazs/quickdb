package cat.quickdb.thread.model;

import java.util.ArrayList;

public class ThreadModel {

    private int id;
    private String name;
    private String address;
    private int age;
    private ArrayList threadReference;

    public ThreadModel() {
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThreadReference(ArrayList threadReference) {
        this.threadReference = threadReference;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList getThreadReference() {
        return threadReference;
    }

}
