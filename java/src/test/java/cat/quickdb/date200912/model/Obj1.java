/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.quickdb.date200912.model;

/**
 *
 * @author gato
 */
public class Obj1 {

    private int id;
    private String name;
    private Obj4 obj4;

    public void setObj4(Obj4 obj4) {
        this.obj4 = obj4;
    }

    public Obj4 getObj4() {
        return obj4;
    }

    public Obj1() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    

}
