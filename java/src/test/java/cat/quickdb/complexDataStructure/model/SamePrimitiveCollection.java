package cat.quickdb.complexDataStructure.model;

import java.util.ArrayList;
import quickdb.annotation.Column;

/**
 *
 * @author Diego Sarmentero
 */
public class SamePrimitiveCollection {

    private int id;
    private String name;
    @Column(collectionClass="java.lang.Integer")
    private ArrayList<Integer> numbers1;
    @Column(collectionClass="java.lang.Integer")
    private ArrayList<Integer> numbers2;

    public SamePrimitiveCollection() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumbers1(ArrayList<Integer> numbers1) {
        this.numbers1 = numbers1;
    }

    public void setNumbers2(ArrayList<Integer> numbers2) {
        this.numbers2 = numbers2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getNumbers1() {
        return numbers1;
    }

    public ArrayList<Integer> getNumbers2() {
        return numbers2;
    }

}
