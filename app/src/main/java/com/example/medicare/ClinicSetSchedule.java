package com.example.medicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClinicSetSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<Appointment> mondayAppointment = new ArrayList<Appointment>();
    ArrayList<Appointment> tuesdayAppointment = new ArrayList<Appointment>();
    ArrayList<Appointment> wednesdayAppointment = new ArrayList<Appointment>();
    ArrayList<Appointment> thursdayAppointment = new ArrayList<Appointment>();
    ArrayList<Appointment> fridayAppointment = new ArrayList<Appointment>();
    ArrayList<Appointment> saturdayAppointment = new ArrayList<Appointment>();
    ArrayList<Appointment> sundayAppointment = new ArrayList<Appointment>();
    Appointment appointment;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_set_schedule);
        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        appointment = getIntent().getParcelableExtra("APPOINTMENT_EXTRA");
        db = FirebaseFirestore.getInstance();
        db.collection("appointment").whereEqualTo("doctor", appointment.getDoctorPath()).whereEqualTo("clinic", appointment.getClinicPath()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Fragment fragment = new ClinicSetScheduleFragment(mondayAppointment);


        switch (position) {
            case 0:
                fragment = new ClinicSetScheduleFragment(mondayAppointment);
                break;
            case 1:
                fragment = new ClinicSetScheduleFragment(tuesdayAppointment);
                break;
            case 2:
                fragment = new ClinicSetScheduleFragment(wednesdayAppointment);
                break;
            case 3:
                fragment = new ClinicSetScheduleFragment(thursdayAppointment);
                break;
            case 4:
                fragment = new ClinicSetScheduleFragment(fridayAppointment);
                break;
            case 5:
                fragment = new ClinicSetScheduleFragment(saturdayAppointment);
                break;
            case 6:
                fragment = new ClinicSetScheduleFragment(sundayAppointment);
                break;
            default:
                fragment = new ClinicSetScheduleFragment(mondayAppointment);
                break;

        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment) // `fragment_container` is the ID of your FrameLayout
                .commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}