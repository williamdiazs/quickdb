package quickdb.query.model;

import quickdb.annotation.Column;

/**
 *
 * @author Diego Sarmentero
 */
public class ObjectSummary {

    private int id;
    private int value; //THIS
    @Column(summary="+value")
    private double summmary;
    @Column(name="accountSalary")
    private double salary; //THIS
    @Column(summary="%salary")
    private double promSalary;
    @Column(summary=">value")
    private double maxValue;
    @Column(summary="<value")
    private double minValue;
    @Column(summary="#value")
    private double countValue;

    public void setCountValue(double countValue) {
        this.countValue = countValue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setPromSalary(double promSalary) {
        this.promSalary = promSalary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setSummmary(double summmary) {
        this.summmary = summmary;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getCountValue() {
        return countValue;
    }

    public int getId() {
        return id;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getPromSalary() {
        return promSalary;
    }

    public double getSalary() {
        return salary;
    }

    public double getSummmary() {
        return summmary;
    }

    public int getValue() {
        return value;
    }

}
