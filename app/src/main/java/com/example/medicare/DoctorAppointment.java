//package com.example.medicare;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class DoctorAppointment extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private Context context = getApplicationContext();
//
//    private ArrayList<RecyclerItem> appointments = new ArrayList<>();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_doctor_appointment);
//        getDbData();
//        setData();
//    }
//
////    protected void getDbData(){
////        ArrayList<Appointment> testAppointments = new ArrayList<Appointment>(0);
////        testAppointments.add(new Appointment("1" ,"2", "3", "4"));
////        for (Appointment ap: testAppointments){
////            appointments.add(new RecyclerItem(ap.getType(), ap.getDate(),ap.getTime(), ap.getAddress()));
////        }
////    }
//
//    protected void setData(){
//        recyclerView = findViewById(R.id.viewAppointment);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoctorAppointment.this, RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        RecylerItemArrayAdapter myRecyclerViewAdapter = new RecylerItemArrayAdapter(appointments, context);
//        recyclerView.setAdapter(myRecyclerViewAdapter);
//    }
//}
