package com.example.mediant;

public class ModelforMedicine {
    String brandName;
    String genericName;

    public ModelforMedicine(String brandName,String genericName){
        this.brandName = brandName;
        this.genericName = genericName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
}
