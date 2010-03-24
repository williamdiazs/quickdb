package cat.quickdb.model;

import cat.quickdb.annotation.*;

@Table
public class SuperPrueba{

    @Column(type=Properties.TYPES.PRIMARYKEY)
    @ColumnDefinition(type=Definition.DATATYPE.INT, length=11,
    autoIncrement=true, primary=true)
    private int id;
    @Column
    @ColumnDefinition
    private String name;
    @Column(type=Properties.TYPES.FOREIGNKEY)
    @ColumnDefinition(type=Definition.DATATYPE.INT, length=11)
    private Prueba prueba;

    public SuperPrueba(){
        this.name = "";
        this.prueba = new Prueba();
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

    /**
     * @return the prueba
     */
    public Prueba getPrueba() {
        return prueba;
    }

    /**
     * @param prueba the prueba to set
     */
    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

}