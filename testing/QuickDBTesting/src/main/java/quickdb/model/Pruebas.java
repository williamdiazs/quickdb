package quickdb.model;

import java.util.ArrayList;

public class Pruebas {

    private int id;
    private String descripcion;
    private ArrayList prueba2;

    public Pruebas(){

    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the array
     */
    public ArrayList getPrueba2() {
        return prueba2;
    }

    /**
     * @param array the array to set
     */
    public void setPrueba2(ArrayList array) {
        this.prueba2 = array;
    }

}
