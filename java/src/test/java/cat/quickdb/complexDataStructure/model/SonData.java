package cat.quickdb.complexDataStructure.model;

import java.util.ArrayList;
import quickdb.annotation.Column;

/**
 *
 * @author Diego Sarmentero
 */
public class SonData extends ParentOfSon{

    private int id;
    private String name;
    private double salary;
    private SonReference reference;
    @Column(collectionClass="java.lang.Integer")
    private ArrayList<Integer> phones;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhones(ArrayList<Integer> phones) {
        this.phones = phones;
    }

    public void setReference(SonReference reference) {
        this.reference = reference;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getPhones() {
        return phones;
    }

    public SonReference getReference() {
        return reference;
    }

    public double getSalary() {
        return salary;
    }

}
