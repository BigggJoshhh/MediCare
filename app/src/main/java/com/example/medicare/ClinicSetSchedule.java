package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import classes.Appointment;


public class ClinicSetSchedule extends AppCompatActivity {
    Appointment appointment;
    FirebaseFirestore db;
    TextView user, service, doctor, other;
    Spinner spinnerDate, spinnerTime;
    Button button;
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> timeList = new ArrayList<>();
    ArrayList<Appointment> appointmentList = new ArrayList<>();
    String selectedDate, selectedTime;
    Timestamp scheduledTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_set_schedule);

        appointment = getIntent().getParcelableExtra("APPOINTMENT_EXTRA");
        db = FirebaseFirestore.getInstance();
        user = (TextView) findViewById(R.id.user);
        service = (TextView) findViewById(R.id.service);
        doctor = (TextView) findViewById(R.id.doctor);
        other = (TextView) findViewById(R.id.other);
        button = (Button) findViewById(R.id.button);
        spinnerDate = (Spinner) findViewById(R.id.spinnerDay);
        spinnerTime = (Spinner) findViewById(R.id.spinnerTime);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("appointment").whereEqualTo("datetime", scheduledTime).whereEqualTo("doctor", appointment.getDoctor()).whereEqualTo("status", "upcoming").whereEqualTo("clinic", appointment.getClinic()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() == 0){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("datetime", scheduledTime);
                                updates.put("status", "upcoming");
                                db.collection("appointment").document(appointment.getAppointmentId()).update(updates);
                                Toast.makeText(ClinicSetSchedule.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), ClinicActivity.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(ClinicSetSchedule.this, "Time slot taken", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ClinicSetSchedule.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        
        service.setText(appointment.getService());
        other.setText(appointment.getOthers());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE", Locale.getDefault());
        sdf2.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        for (int i = 0; i < 14; i++) {
            dateList.add(sdf.format(calendar.getTime()) + " (" + sdf2.format(calendar.getTime()) + ")");
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapter);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 30);
        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime) || currentTime.equals(endTime)) {
            timeList.add(currentTime.toString());
            currentTime = currentTime.plusMinutes(30); // Increment by 15 minutes
        }
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeList);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedDate = dateList.get(position);
                handleSelect();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected (if needed)
            }
        });

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTime = timeList.get(position);
                handleSelect();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected (if needed)
            }
        });

        appointment.getUser().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user.setText(document.getString("username"));
                    } else {
                        Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();

                }
            }
        });

        appointment.getDoctor().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        doctor.setText(document.getString("username"));
                    } else {
                        Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    protected void handleSelect() {
        if (selectedTime != null && selectedDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy (EEEE) HH:mm", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            String strDate = selectedDate + " " + selectedTime;
            Date date = null;
            try {
                date = sdf.parse(strDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            scheduledTime = new Timestamp(date);
        }
    }
}