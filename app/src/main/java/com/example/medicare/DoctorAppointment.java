package com.example.medicare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class DoctorAppointment extends AppCompatActivity {

    private final ArrayList<RecyclerItem> appointments = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);
        getDbData();
        setData();
    }

    protected void getDbData() {
        ArrayList<Appointment> testAppointments = new ArrayList<Appointment>(0);
        testAppointments.add(new Appointment("1", "2", "Health Checkup", (new Date()), "Singapore Polytechnic", "", ""));
        testAppointments.add(new Appointment("1", "2", "Health Checkup", (new Date()), "Singapore Polytechnic", "", ""));
        testAppointments.add(new Appointment("1", "2", "Health Checkup", (new Date()), "Singapore Polytechnic", "", ""));

        for (Appointment ap : testAppointments) {
            appointments.add(new RecyclerItem(ap.getService(), ap.getFormattedDate(), ap.getFormattedTime(), ap.getLocation()));
        }
    }

    protected void setData() {
        recyclerView = findViewById(R.id.viewAppointment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoctorAppointment.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecylerItemArrayAdapter myRecyclerViewAdapter = new RecylerItemArrayAdapter(appointments, getApplicationContext());
        recyclerView.setAdapter(myRecyclerViewAdapter);
    }
}

