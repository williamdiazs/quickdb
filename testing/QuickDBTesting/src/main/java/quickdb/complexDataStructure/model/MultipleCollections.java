package quickdb.complexDataStructure.model;

import java.util.ArrayList;

/**
 *
 * @author Diego Sarmentero
 */
public class MultipleCollections {

    private int id;
    private String name;
    private ArrayList individualObject1;
    private ArrayList individualObject2;

    public MultipleCollections() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIndividualObject1(ArrayList individualObject1) {
        this.individualObject1 = individualObject1;
    }

    public void setIndividualObject2(ArrayList individualObject2) {
        this.individualObject2 = individualObject2;
    }

    public int getId() {
        return id;
    }

    public ArrayList getIndividualObject1() {
        return individualObject1;
    }

    public ArrayList getIndividualObject2() {
        return individualObject2;
    }

}
