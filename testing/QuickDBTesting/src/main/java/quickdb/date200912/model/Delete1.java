package quickdb.date200912.model;

import java.util.ArrayList;

public class Delete1 {

    private int id;
    private String description;
    private ArrayList<Delete2> delete2;

    public Delete1(){}

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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the many2
     */
    public ArrayList<Delete2> getDelete2() {
        return delete2;
    }

    /**
     * @param many2 the many2 to set
     */
    public void setDelete2(ArrayList<Delete2> many2) {
        this.delete2 = many2;
    }

}
