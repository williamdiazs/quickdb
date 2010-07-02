package cat.quickdb.query.model;

/**
 *
 * @author Diego Sarmentero
 */
public class ObjectSubquery {

    private int id;
    private String value;

    public ObjectSubquery(String value) {
        this.value = value;
    }

    public ObjectSubquery() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

}
