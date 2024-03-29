package classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Appointment implements Parcelable {

    private String appointmentId;
    private DocumentReference user;
    private DocumentReference doctor;
    private DocumentReference clinic;
    private String service ;
    private String others;
    private String status;
    private Timestamp datetime;
    private String location;




    public Appointment() {
    }
    public Appointment(String appointmentId, DocumentReference user, DocumentReference doctor, DocumentReference clinic, String service, String others, String status, Timestamp datetime, String location) {
        this.appointmentId = appointmentId;
        this.user = user;
        this.doctor = doctor;
        this.clinic = clinic;
        this.service = service;
        this.others = others;
        this.status = status;
        this.datetime = datetime;
        this.location = location;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public DocumentReference getClinic() {
        return clinic;
    }

    public void setClinic(DocumentReference clinic) {
        this.clinic = clinic;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
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
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf.format(date);
        }
        return "";
    }

    public String getFormattedTime() {
        if (datetime != null) {
            Date date = datetime.toDate(); // Convert Timestamp to Date
            // Define the format you want. For example: "hh:mm a"
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf.format(date);
        }
        return "";
    }

    // Parcelable implementation
    protected Appointment(Parcel in) {
        // Read the userPath and doctorPath as strings
        String userPathString = in.readString();
        String doctorPathString = in.readString();
        String clinicPathString = in.readString();

        // Convert the strings back to DocumentReference
        user = convertStringToDocumentReference(userPathString);
        doctor = convertStringToDocumentReference(doctorPathString);
        clinic = convertStringToDocumentReference(clinicPathString);
        appointmentId = in.readString();
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
        dest.writeString(clinicPathToString(clinic));
        dest.writeString(appointmentId);
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

    private String clinicPathToString(DocumentReference clinicRef) {
        if (clinicRef != null) {
            return clinicRef.getPath();
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
