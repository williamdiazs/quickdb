package quickdb.query.model;

public class UserParent {

    private int id;
    private String description;
    private ReferenceQuery reference;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReference(ReferenceQuery reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public ReferenceQuery getReference() {
        return reference;
    }

}
