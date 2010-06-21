package cat.quickdb.date200912.model;

import java.util.ArrayList;

public class Collection1 {
    
    private int id;
    private String name;
    private ArrayList collection2;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the testCollection2
     */
    public ArrayList getCollection2() {
        return collection2;
    }

    /**
     * @param testCollection2 the testCollection2 to set
     */
    public void setCollection2(ArrayList collection2) {
        this.collection2 = collection2;
    }

}
