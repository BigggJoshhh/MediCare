package com.example.medicare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ViewAppointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        Appointment appointment = getIntent().getParcelableExtra("APPOINTMENT_EXTRA");
        // Use the appointment object to populate your views....
    }
}