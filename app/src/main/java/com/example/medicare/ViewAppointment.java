package com.example.medicare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import classes.Appointment;

public class ViewAppointment extends AppCompatActivity {

    String doctorName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        TextView tvLocationAddress = findViewById(R.id.tvLocationAddress);
        TextView tvServiceValue = findViewById(R.id.tvServiceValue);
        TextView tvDoctorValue = findViewById(R.id.tvDoctorValue);
        TextView tvDateValue = findViewById(R.id.tvDateValue);
        TextView tvTimeValue = findViewById(R.id.tvTimeValue);
        TextView tvOtherValue = findViewById(R.id.tvOtherValue);

        Appointment appointment = getIntent().getParcelableExtra("APPOINTMENT_EXTRA");
        if(appointment != null) {
            // Get the doctor's path
            DocumentReference docRef = appointment.getDoctor();

            Log.d("errorDoc", docRef.getPath());
           docRef.get()
                   .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(Task<DocumentSnapshot> task) {
                           if (task.isSuccessful()) {
                               DocumentSnapshot document = task.getResult();
                               if(document.exists()) {
                                   // Extract data from  the referenced document
                                   doctorName = document.getString("username");
                               }
                               else {
                                   Log.e("errorr", "document doesn't exist");
                               }
                               Log.d("errorr ", "doctorName");
                           } else {
                               Log.e("errorr", "error retrieving document");
                           }
                       }
                   });


            String othersText = appointment.getOthers();
            if (othersText == null || othersText.trim().isEmpty()) {
                tvOtherValue.setText("Nil");
            } else {
                tvOtherValue.setText(othersText);
            }

            tvLocationAddress.setText(appointment.getLocation());
            tvServiceValue.setText(appointment.getService());
            tvDoctorValue.setText("Dr " + doctorName);
            tvDateValue.setText(appointment.getFormattedDate());
            tvTimeValue.setText(appointment.getFormattedTime());
        }
    }
}