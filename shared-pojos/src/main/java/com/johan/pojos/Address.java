package com.johan.pojos;

public class Address {
    private String street;
    private String apartmentNumber;
    private String city;
    private String zipCode;

    public Address() {
    }

    public Address(String street, String apartmentNumber, String city, String zipCode) {
        this.street = street;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
