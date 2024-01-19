package com.example.medicare;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {
    private String user;
    private String doctor;
    private String service;
    private Date date;
    private String location;
    private String others;
    private String status;

    public Appointment(String user, String doctor, String service, Date date, String location, String others, String status) {
        this.user = user;
        this.doctor = doctor;
        this.service = service;
        this.date = date;
        this.location = location;
        this.others = others;
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        return sdf.format(this.date);
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(this.date);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
