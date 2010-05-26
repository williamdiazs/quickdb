package cat.quickdb.complexDataStructure.model;

import java.util.ArrayList;

public class Customer extends PersonComplex{

    private int id;
    private String email;
    private AddressComplex address;
    private ArrayList buy;

    public Customer() {
    }

    public Customer(String name, String surname, String email, AddressComplex address,
            ArrayList buy) {
        super(name, surname);
        this.email = email;
        this.address = address;
        this.buy = buy;
    }

    public void setAddress(AddressComplex address) {
        this.address = address;
    }

    public void setBuy(ArrayList buy) {
        this.buy = buy;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressComplex getAddress() {
        return address;
    }

    public ArrayList getBuy() {
        return buy;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
