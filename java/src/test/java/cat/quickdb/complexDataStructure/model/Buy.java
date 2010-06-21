package cat.quickdb.complexDataStructure.model;

import java.util.ArrayList;
import quickdb.annotation.Column;
import quickdb.annotation.Table;

@Table
public class Buy {

    private int id;
    private String article;
    private double price;
    @Column(collectionClass="java.lang.Integer")
    private ArrayList<Integer> codes;

    public Buy() {
    }

    public Buy(String article, double price, ArrayList<Integer> codes) {
        this.article = article;
        this.price = price;
        this.codes = codes;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setCodes(ArrayList<Integer> codes) {
        this.codes = codes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getArticle() {
        return article;
    }

    public ArrayList<Integer> getCodes() {
        return codes;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

}
