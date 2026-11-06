package entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    private String city;
    private String state;
    private String street;
    private String zipCode;

    public Address() {}

    public Address(String city, String state, String street, String zipCode) {
        this.city = city;
        this.state = state;
        this.street = street;
        this.zipCode = zipCode;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}