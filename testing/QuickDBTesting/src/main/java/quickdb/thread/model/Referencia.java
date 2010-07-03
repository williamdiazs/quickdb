package quickdb.thread.model;

public class Referencia {

    private int id;
    private String data;
    private String description;

    public Referencia(String data, String description) {
        this.data = data;
        this.description = description;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

}
