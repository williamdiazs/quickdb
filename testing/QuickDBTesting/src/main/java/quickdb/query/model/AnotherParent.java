package quickdb.query.model;

public class AnotherParent {

    private int id;
    private double mount;
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMount(double mount) {
        this.mount = mount;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public double getMount() {
        return mount;
    }

}
