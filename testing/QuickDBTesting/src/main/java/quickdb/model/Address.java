package quickdb.model;

import quickdb.annotation.*;

@Table("address")
public class Address{

    @Column(type=Properties.TYPES.PRIMARYKEY)
    private int id;
    @Column
    @ColumnDefinition
    private String street;
    @Column(type=Properties.TYPES.FOREIGNKEY)
    private District idDistrict;

    public Address(){
        this.street = "";
        this.idDistrict = new District("");
    }

    public Address(String street, District district){
        this.street = street;
        this.idDistrict = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public District getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(District idDistrict) {
        this.idDistrict = idDistrict;
    }

}