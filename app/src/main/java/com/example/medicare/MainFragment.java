package com.example.medicare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    // Initialize variable
     RecyclerView recyclerView;
     AppointmentAdapter adapter;
    List<Appointment> appointmentList = new ArrayList<>();

    public static MainFragment newInstance(ArrayList<Appointment> appointments) {
        MainFragment fragment = new MainFragment();
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
        adapter = new AppointmentAdapter(appointmentList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void updateAppointments(ArrayList<Appointment> appointments) {
        if (appointmentList != null) {
            appointmentList.clear();
            appointmentList.addAll(appointments);
            if (adapter != null) {
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        } else {
            Log.e("MainFragment", "Appointment list is null, can't update appointments");
        }
    }
}
