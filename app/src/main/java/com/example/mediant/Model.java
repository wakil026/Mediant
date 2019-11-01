package com.example.mediant;

public class Model {
    String name;
    String contactNumber;
    String serviceArea;

    public Model(String name,String contactNumber){
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public Model(String name,String contactNumber,String serviceArea){
        this.name = name;
        this.contactNumber = contactNumber;
        this.serviceArea = serviceArea;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
