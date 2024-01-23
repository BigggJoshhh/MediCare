package com.example.medicare;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Appointment implements Parcelable {

    private String appointmentId;
    private String userPath;
    private String clinicPath;
    private String doctorPath;
    private String service ;
    private String others;
    private String status;
    private Timestamp datetime;
    private String location;

    public Appointment(String appointmentId, String clinicPath, String userPath, String doctorPath, String service, String others, String status, Timestamp datetime, String location) {
        this.appointmentId = appointmentId;
        this.clinicPath = clinicPath;
        this.userPath = userPath;
        this.doctorPath = doctorPath;
        this.service = service;
        this.others = others;
        this.status = status;
        this.datetime = datetime;
        this.location = location;
    }

    public Appointment() {
    }

    public String getClinicPath() {
        return clinicPath;
    }

    public void setClinicPath(String clinicPath) {
        this.clinicPath = clinicPath;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getUserPath() {
        return userPath;
    }

    public String setUserPath(String userPath) {
        return this.userPath = userPath;
    }

    public String getDoctorPath() {
        return doctorPath;
    }

    public String setDoctorPath(String doctorPath) {
        return this.doctorPath = doctorPath;
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
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

    // Parcelable implementation
    protected Appointment(Parcel in) {
        userPath = in.readString();
        doctorPath = in.readString();
        service = in.readString();
        others = in.readString();
        status = in.readString();
        datetime = new Timestamp(new Date(in.readLong()));
        location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userPath);
        dest.writeString(doctorPath);
        dest.writeString(service);
        dest.writeString(others);
        dest.writeString(status);
        if (datetime != null) {
            dest.writeLong(datetime.toDate().getTime());
        } else {
            dest.writeLong(0);
        }
        dest.writeString(location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

}
