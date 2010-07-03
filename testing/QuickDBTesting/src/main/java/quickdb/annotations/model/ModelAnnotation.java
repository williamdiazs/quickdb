package quickdb.annotations.model;

import quickdb.annotation.Column;
import quickdb.annotation.ColumnDefinition;
import quickdb.annotation.Definition.DATATYPE;
import quickdb.annotation.Parent;
import quickdb.annotation.Properties.TYPES;
import quickdb.annotation.Table;
import quickdb.annotation.Validation;
import java.sql.Date;
import java.util.ArrayList;

@Parent
@Table("AnnotationModel")
public class ModelAnnotation extends ModelParent{

    @Column(type=TYPES.PRIMARYKEY)
    @ColumnDefinition(primary=true, autoIncrement=true,
    length=11, unique=true, type=DATATYPE.INTEGER)
    private int idModel;
    @Column(name="modelName")
    @ColumnDefinition(length=300, defaultValue="test")
    private String name;
    @Validation(numeric={Validation.GREATER, 18})
    @ColumnDefinition(type=DATATYPE.INTEGER)
    private int age;
    @ColumnDefinition(type=DATATYPE.DATETIME)
    private Date birth;
    @Column(getter="getterSalary")
    private double salary;
    @Column(type=TYPES.COLLECTION,
    collectionClass="quickdb.annotations.model.CollectionAnnotation")
    private ArrayList array;
    @Column(type=TYPES.FOREIGNKEY, name="foreignCollec")
    @ColumnDefinition(type=DATATYPE.INTEGER)
    private CollectionAnnotation collec;

    public void setArray(ArrayList array) {
        this.array = array;
    }

    public ArrayList getArray() {
        return array;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setCollec(CollectionAnnotation collec) {
        this.collec = collec;
    }

    public void setIdModel(int idModel) {
        this.idModel = idModel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public Date getBirth() {
        return birth;
    }

    public CollectionAnnotation getCollec() {
        return collec;
    }

    public int getIdModel() {
        return idModel;
    }

    public String getName() {
        return name;
    }

    public double getterSalary() {
        return salary;
    }

}
