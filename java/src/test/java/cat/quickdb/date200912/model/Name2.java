package cat.quickdb.date200912.model;

import quickdb.annotation.*;

@Table
public class Name2 {

    @Column(type=Properties.TYPES.PRIMARYKEY)
    @ColumnDefinition(type=Definition.DATATYPE.INT, autoIncrement=true,
    primary=true, length=11)
    private int id2;
    private String myName;

    /**
     * @return the id2
     */
    public int getId2() {
        return id2;
    }

    /**
     * @param id2 the id2 to set
     */
    public void setId2(int id2) {
        this.id2 = id2;
    }

    /**
     * @return the myName
     */
    public String getMyName() {
        return myName;
    }

    /**
     * @param myName the myName to set
     */
    public void setMyName(String myName) {
        this.myName = myName;
    }

}
