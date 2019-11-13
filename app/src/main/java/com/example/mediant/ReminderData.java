package com.example.mediant;

import java.util.ArrayList;
import java.util.Vector;

public class ReminderData {
    private String medicineName;
    private ArrayList<Integer> times;

    public ReminderData() {

    }

    public ReminderData(String medicineName, ArrayList<Integer> times) {
        this.medicineName = medicineName;
        this.times = times;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public ArrayList<Integer> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Integer> times) {
        this.times = times;
    }
}

