package quickdb.query.model;

/**
 *
 * @author Diego Sarmentero
 */
public class QueryWithSubquery {

    private int id;
    private String name;
    private ObjectSubquery subQuery;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setSubQuery(ObjectSubquery subQuery) {
        this.subQuery = subQuery;
    }

    public ObjectSubquery getSubQuery() {
        return subQuery;
    }

}
