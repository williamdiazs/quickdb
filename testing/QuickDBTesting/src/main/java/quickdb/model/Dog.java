package quickdb.model;

public class Dog{

    private int id;
    private String name;
    private String color;
    private Race idRace;

    public Dog(){
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