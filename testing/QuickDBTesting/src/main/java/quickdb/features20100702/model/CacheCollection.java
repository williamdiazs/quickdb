package quickdb.features20100702.model;

import quickdb.annotation.Table;

/**
 *
 * @author Diego Sarmentero
 */
@Table(cache=true)
public class CacheCollection {

    private int id;
    private String name;
    private int value;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}
