package quickdb.query.model;

/**
 *
 * @author Diego Sarmentero
 */
public class QueryCollection {

    private int id;
    private int value;

    public QueryCollection() {
    }

    public QueryCollection(int value) {
        this.value = value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

}
