package com.example.medicare;

public class Appointment {
    private String type;
    private String date;
    private String time;
    private String address;

    public String getType() {
        return type;
    }

    public Appointment(String type, String date, String time, String address) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
