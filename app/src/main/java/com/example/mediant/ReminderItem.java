package com.example.mediant;

public class ReminderItem {
    private String name;
    private String description;
    private Boolean isEnabled;

    public ReminderItem(String name, String description, Boolean isEnabled) {
        this.name = name;
        this.description = description;
        this.isEnabled = isEnabled;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
