package quickdb.thread.model;

public class ThreadReference {

    private int id;
    private String description;

    public ThreadReference() {
    }

    public ThreadReference(String description) {
        this.description = description;
    }

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
