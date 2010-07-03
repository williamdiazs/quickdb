package quickdb.complexDataStructure.model;

public class AddressComplex {

    public int id;
    public String street;
    public int number;

    public AddressComplex() {
    }

    public AddressComplex(String street, int number) {
        this.street = street;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getStreet() {
        return street;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
