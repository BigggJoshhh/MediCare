package com.example.medicare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    // Initialize variable
     RecyclerView recyclerView;
     AppointmentAdapter adapter;
     List<Appointment> appointmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize your appointment list here or in the constructor
        appointmentList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_appointment_status, container, false);

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.appointmentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Initialize the adapter with the appointment list
        adapter = new AppointmentAdapter(appointmentList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void updateAppointments(List<Appointment> appointments) {
        if (adapter != null) {
            appointmentList.clear();
            appointmentList.addAll(appointments);
            adapter.notifyDataSetChanged();
        }
    }
}
