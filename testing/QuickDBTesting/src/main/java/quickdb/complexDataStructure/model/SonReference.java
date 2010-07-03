package quickdb.complexDataStructure.model;

/**
 *
 * @author Diego Sarmentero
 */
public class SonReference extends SonReferenceParent{

    private int id;
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

}
