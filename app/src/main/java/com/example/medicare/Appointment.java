package com.example.medicare;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Appointment {
    private DocumentReference user;
    private DocumentReference doctor;
    private String service ;
    private String others;
    private String status;
    private Timestamp datetime;
    private String location;

    public Appointment(DocumentReference user, DocumentReference doctor, String service, String others, String status, Timestamp datetime, String location) {
        this.user = user;
        this.doctor = doctor;
        this.service = service;
        this.others = others;
        this.status = status;
        this.datetime = datetime;
        this.location = location;
    }

    public Appointment() {
    }

    public DocumentReference getUser() {
        return user;
    }

    public DocumentReference setUser(DocumentReference user) {
        return this.user = user;
    }

    public DocumentReference getDoctor() {
        return doctor;
    }

    public DocumentReference setDoctor(DocumentReference doctor) {
        return this.doctor = doctor;
    }
    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }

    public String getOthers() {
        return others;
    }
    public void setOthers(String others) {
        this.others = others;
    }
    public String getStatus() { return status;}
    public void setStatus(String status) { this.status = status; }

    public Timestamp getDateTime() {
        return datetime;
    }
    public void setDateTime(Timestamp datetime) {
        this.datetime = datetime;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFormattedDate() {
        if (datetime != null) {
            Date date = datetime.toDate(); // Convert Timestamp to Date
            // Define the format you want. For example: "dd MMM yyyy"
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return sdf.format(date);
        }
        return "";
    }

    public String getFormattedTime() {
        if (datetime != null) {
            Date date = datetime.toDate(); // Convert Timestamp to Date
            // Define the format you want. For example: "hh:mm a"
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sdf.format(date);
        }
        return "";
    }

}
