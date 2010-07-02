package cat.quickdb.features20100702.model;

import quickdb.annotation.Table;

/**
 *
 * @author Diego Sarmentero
 */
@Table(before={"modifyData", "OtherClass", "this"},
after={"modifyData2", "cat.quickdb.features20100702.model.OtherClass", "this"})
public class ExecutionOtherClass {

    private int id;
    private String name;
    private double value;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

}
