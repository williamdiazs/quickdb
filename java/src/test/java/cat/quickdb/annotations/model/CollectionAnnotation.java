package cat.quickdb.annotations.model;

import quickdb.annotation.Column;
import quickdb.annotation.ColumnDefinition;
import quickdb.annotation.Definition.DATATYPE;
import quickdb.annotation.Properties.TYPES;
import quickdb.annotation.Table;

@Table("CollecAnnotation")
public class CollectionAnnotation {

    @Column(type=TYPES.PRIMARYKEY)
    @ColumnDefinition(autoIncrement=true, length=11,
    primary=true, type=DATATYPE.INTEGER)
    private int idCollection;
    @Column(name="itemName", setter="setItemName", getter="getItemName")
    private String item;
    @Column(ignore=true)
    private String nothing;

    public CollectionAnnotation(String item) {
        this.item = item;
    }

    public CollectionAnnotation() {
    }

    public int getIdCollection() {
        return idCollection;
    }

    public String getItemName() {
        return item;
    }

    public void setIdCollection(int idCollection) {
        this.idCollection = idCollection;
    }

    public void setItemName(String item) {
        this.item = item;
    }

}
