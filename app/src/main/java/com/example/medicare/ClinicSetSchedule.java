package com.example.medicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import classes.Appointment;


public class ClinicSetSchedule extends AppCompatActivity{
    Appointment appointment;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_set_schedule);
        appointment = getIntent().getParcelableExtra("APPOINTMENT_EXTRA");
        db = FirebaseFirestore.getInstance();
        db.collection("appointment").whereEqualTo("doctor", appointment.getDoctor()).whereEqualTo("clinic", appointment.getClinic()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    if (task.isSuccessful()) {
                        Toast.makeText(ClinicSetSchedule.this, "", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ClinicSetSchedule.this, "", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ClinicSetSchedule.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}