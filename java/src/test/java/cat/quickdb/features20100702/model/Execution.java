package cat.quickdb.features20100702.model;

import quickdb.annotation.Table;

/**
 *
 * @author Diego Sarmentero
 */
@Table(before={"alterValue"}, after={"modify"})
public class Execution {

    private int id;
    private String description;
    private int value;

    public void alterValue(){
        this.value = 999;
    }

    public void modify(){
        this.value = 22;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

}
