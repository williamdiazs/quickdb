package quickdb.complexDataStructure.model;

import java.util.ArrayList;
import quickdb.annotation.Column;

/**
 *
 * @author Diego Sarmentero
 */
public class MultipleCollectionsPrimitive {

    private int id;
    private String name;
    @Column(collectionClass="java.lang.Integer")
    private ArrayList<Integer> numbers;
    @Column(collectionClass="java.lang.String")
    private ArrayList<String> words;

    public MultipleCollectionsPrimitive() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getNumbers() {
        return numbers;
    }

    public ArrayList<String> getWords() {
        return words;
    }

}
