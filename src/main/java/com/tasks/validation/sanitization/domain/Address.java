package com.tasks.validation.sanitization.domain;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;


public class Address implements Serializable {
    private static final long serialVersionUID = -1131036911858831307L;
    private UUID relativeId;
    private UUID parentResourceId;
    private List<String> streetAddress;
    private String officeSuite;
    private String apartmentNumber;
    private String postOfficeBoxNumber;
    private String postalCode;
    private String city;
    private String lineOfBusiness;

    public Address() {
    }

    public UUID getRelativeId() {
        return this.relativeId;
    }

    public UUID getParentResourceId() {
        return this.parentResourceId;
    }

    public List<String> getStreetAddress() {
        return this.streetAddress;
    }

    public String getOfficeSuite() {
        return this.officeSuite;
    }

    public String getApartmentNumber() {
        return this.apartmentNumber;
    }

    public String getPostOfficeBoxNumber() {
        return this.postOfficeBoxNumber;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCity() {
        return this.city;
    }


    


    public String getLineOfBusiness() {
        return this.lineOfBusiness;
    }

    public void setRelativeId(UUID relativeId) {
        this.relativeId = relativeId;
    }

    public void setParentResourceId(UUID parentResourceId) {
        this.parentResourceId = parentResourceId;
    }

    

    public void setStreetAddress(List<String> streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setOfficeSuite(String officeSuite) {
        this.officeSuite = officeSuite;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public void setPostOfficeBoxNumber(String postOfficeBoxNumber) {
        this.postOfficeBoxNumber = postOfficeBoxNumber;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    
    public void setLineOfBusiness(String lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }
}
