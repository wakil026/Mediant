package com.example.mediant;

public class MedicineInfo {
    private String brandName;
    private String genericName;
    private String description;

    public MedicineInfo(String brandName, String genericName, String description) {
        this.brandName = brandName;
        this.genericName = genericName;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}