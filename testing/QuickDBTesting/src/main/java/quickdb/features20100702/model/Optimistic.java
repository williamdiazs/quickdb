package quickdb.features20100702.model;

/**
 *
 * @author Diego Sarmentero
 */
public class Optimistic {

    private int id;
    private String name;
    private double value;
    private long optimisticLock;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOptimisticLock(long optimisticLock) {
        this.optimisticLock = optimisticLock;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getOptimisticLock() {
        return optimisticLock;
    }

    public double getValue() {
        return value;
    }

}
