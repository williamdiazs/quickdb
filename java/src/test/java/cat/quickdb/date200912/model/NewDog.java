package cat.quickdb.date200912.model;

public class NewDog{

    private int id;
    private String name;
    private String color;
    private Race idRace;

    public NewDog(){
        this.idRace = new Race();
        this.name = "";
        this.color = "";
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Race getIdRace() {
        return idRace;
    }

    public void setIdRace(Race idRace) {
        this.idRace = idRace;
    }

}