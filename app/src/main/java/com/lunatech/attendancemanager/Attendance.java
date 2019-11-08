package com.lunatech.attendancemanager;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Attendance extends RealmObject {

    @PrimaryKey
    private String date;
    private boolean present;
    private String dateText;

    public Attendance() {
    }

    public Attendance(String date , boolean present,String dateText) {
        this.date = date;
        this.present = present;
        this.dateText = dateText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }
}
