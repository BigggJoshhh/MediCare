package com.example.medicare;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Appointment implements Parcelable {
    private DocumentReference user;
    private String userPath;
    private DocumentReference doctor;
    private String doctorPath;
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

    public void setUser(DocumentReference user) {
         this.user = user;
    }

    public DocumentReference getDoctor() {
            return doctor;
    }

    public void setDoctor(DocumentReference doctor) {
        this.doctor = doctor;
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
        // Read the userPath and doctorPath as strings
        String userPathString = in.readString();
        String doctorPathString = in.readString();

        // Convert the strings back to DocumentReference
        user = convertStringToDocumentReference(userPathString);
        doctor = convertStringToDocumentReference(doctorPathString);
        service = in.readString();
        others = in.readString();
        status = in.readString();
        datetime = new Timestamp(new Date(in.readLong()));
        location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write the userPath and doctorPath as strings
        dest.writeString(userPathToString(user));
        dest.writeString(doctorPathToString(doctor));
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

    private String userPathToString(DocumentReference userRef) {
        if (userRef != null) {
            return userRef.getPath();
        }
        return null; // Handle the case where docRef is null
    }

    private String doctorPathToString(DocumentReference docRef) {
        if (docRef != null) {
            return docRef.getPath();
        }
        return null; // Handle the case where docRef is null
    }


    private DocumentReference convertStringToDocumentReference(String path) {
        if (path != null) {
            return FirebaseFirestore.getInstance().document(path);
        }
        return null; // Handle the case where path is null
    }

}
