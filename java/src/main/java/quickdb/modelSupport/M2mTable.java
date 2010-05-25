package quickdb.modelSupport;

/**
 *
 * @author Diego Sarmentero
 */
public class M2mTable{

    private int id;
    private int base;
    private int related;

    public M2mTable(){}

    public M2mTable(int b, int r){
        this.base = b;
        this.related = r;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getRelated() {
        return related;
    }

    public void setRelated(int related) {
        this.related = related;
    }

}