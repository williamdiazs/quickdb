package quickdb.thread.model;

public class ThreadReference2 {

    private int id;
    private String description;
    private Referencia referencia;

    public ThreadReference2() {
    }

    public ThreadReference2(String description) {
        this.description = description;
        this.referencia = new Referencia("data", "description referencia");
    }

    public void setReferencia(Referencia referencia) {
        this.referencia = referencia;
    }

    public Referencia getReferencia() {
        return referencia;
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
