package com.example.mediant;

public class MedicineInfo {
    private String brandName;
    private String genericName;
    private String contains;
    private String type;
    private String companyName;
    private String indications;
    private String sideEffect;

    public MedicineInfo(String brandName, String genericName, String contains, String type, String companyName, String indications, String sideEffect) {
        this.brandName = brandName;
        this.genericName = genericName;
        this.contains = contains;
        this.type = type;
        this.companyName = companyName;
        this.indications = indications;
        this.sideEffect = sideEffect;
    }

    public MedicineInfo(String brandName, String genericName, String contains, String type, String companyName) {
        this.brandName = brandName;
        this.genericName = genericName;
        this.contains = contains;
        this.type = type;
        this.companyName = companyName;
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


    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getSideEffect() {
        return sideEffect;
    }

    public void setSideEffcet(String sideEffect) {
        this.sideEffect = sideEffect;
    }

}