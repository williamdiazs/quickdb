package cat.quickdb.complexmodel;

import java.util.ArrayList;

public class Many1 {

    private int id;
    private String description;
    private ArrayList<Many2> many2;

    public Many1(){}

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
    public ArrayList<Many2> getMany2() {
        return many2;
    }

    /**
     * @param many2 the many2 to set
     */
    public void setMany2(ArrayList<Many2> many2) {
        this.many2 = many2;
    }

}
