package cat.quickdb.model;

import quickdb.annotation.*;

@Table
public class Person{

    @Column(type=Properties.TYPES.PRIMARYKEY)
    private int id;
    @Column(name="name")
    private String personName;
    @Column(getter="getPersonAge", setter="setPersonAge")
    private int age;

    public Person(){
        this.personName = "";
    }

    public Person(String name, int age){
        this.id = 0;
        this.personName = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getPersonAge() {
        return age;
    }

    public void setPersonAge(int age) {
        this.age = age;
    }

}