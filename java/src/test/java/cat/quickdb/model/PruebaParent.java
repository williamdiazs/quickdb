package cat.quickdb.model;

import cat.quickdb.annotation.*;

@Table
public class PruebaParent {
    @Column(type = Properties.TYPES.PRIMARYKEY)
    @ColumnDefinition(type=Definition.DATATYPE.INT, length=11,
    autoIncrement=true, primary=true)
    private int id;
    @Column
    @ColumnDefinition
    private String nameParent;
    @Column
    @ColumnDefinition(type=Definition.DATATYPE.INTEGER)
    private int number;

    public PruebaParent(){
        this.nameParent = "";
    }

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
     * @return the nameParent
     */
    public String getNameParent() {
        return nameParent;
    }

    /**
     * @param nameParent the nameParent to set
     */
    public void setNameParent(String nameParent) {
        this.nameParent = nameParent;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

}
