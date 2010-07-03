package quickdb.date200912.model;

public class Race{

    private int id;
    private String name;

    public Race(){
        this.name = "";
    }

    public Race(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}