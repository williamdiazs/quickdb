package cat.quickdb.model;

import quickdb.annotation.*;

@Table("district")
public class District{

    @Column(type=Properties.TYPES.PRIMARYKEY)
    private int id;
    @Column
    private String name;

    public District(String name){
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