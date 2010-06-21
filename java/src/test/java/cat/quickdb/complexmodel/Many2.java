package cat.quickdb.complexmodel;

import java.util.ArrayList;

public class Many2 {

    private int id;
    private String name;
    private ArrayList<Many1> many1;

    public Many2(){}

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
     * @return the many1
     */
    public ArrayList<Many1> getMany1() {
        return many1;
    }

    /**
     * @param many1 the many1 to set
     */
    public void setMany1(ArrayList<Many1> many1) {
        this.many1 = many1;
    }

}
