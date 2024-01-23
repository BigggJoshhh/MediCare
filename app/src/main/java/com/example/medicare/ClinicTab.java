package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import classes.Appointment;

public class ClinicTab extends Fragment implements AppointmentAdapter.OnAppointmentClickListener  {

    RecyclerView recyclerView;
    AppointmentAdapter adapter;
    List<Appointment> appointmentList = new ArrayList<>();

    public static ClinicTab newInstance(ArrayList<Appointment> appointments) {
        ClinicTab fragment = new ClinicTab();
        Bundle args = new Bundle();
        args.putParcelableArrayList("appointments", appointments);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize your appointment list here or in onCreateView to prevent null reference
        if (getArguments() != null) {
            appointmentList = getArguments().getParcelableArrayList("appointments");
        }
        if (appointmentList == null) { // If no arguments, initialize an empty list
            appointmentList = new ArrayList<>();
        }
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
        adapter = new AppointmentAdapter(appointmentList, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAppointmentClick(Appointment appointment) {
        Intent intent = new Intent(getActivity(), ClinicSetSchedule.class);
        // Assuming Appointment class implements Parcelable or Serializable
        intent.putExtra("APPOINTMENT_EXTRA", appointment);
        startActivity(intent);
    }
}