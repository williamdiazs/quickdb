package quickdb.modelSupport;

import quickdb.annotation.Column;
import quickdb.annotation.Properties.TYPES;

/**
 *
 * @author Diego Sarmentero
 */
public class PrimitiveCollec {

    private int id;
    private int base;
    @Column(type=TYPES.PRIMITIVE)
    private Object object;

    public PrimitiveCollec(){}

    public PrimitiveCollec(Object o){
        this.object = o;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getBase() {
        return base;
    }

    public int getId() {
        return id;
    }

    public Object getObject() {
        return object;
    }

}
