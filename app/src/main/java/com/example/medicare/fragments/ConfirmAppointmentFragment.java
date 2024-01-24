package com.example.medicare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.medicare.BookAppointment;
import com.example.medicare.R;
import com.example.medicare.fragments.SuccessFragment;

public class ConfirmAppointmentFragment extends Fragment {

    // Arguments names
    private static final String ARG_SERVICE = "service";
    private static final String ARG_DOCTOR = "doctor";

    // Variables to store passed arguments
    private String selectedService,selectedDoctor;

    // Factory method to create a new instance of ConfirmAppointmentFragment with provided data
    public static ConfirmAppointmentFragment newInstance(String service, String doctor) {
        ConfirmAppointmentFragment fragment = new ConfirmAppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SERVICE, service);
        args.putString(ARG_DOCTOR, doctor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedService = getArguments().getString(ARG_SERVICE);
            selectedDoctor = getArguments().getString(ARG_DOCTOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup the views with the confirmation details using selectedService and selectedDoctor

        // Find the "Confirm" button and set its click listener
        view.findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the success page fragment
                SuccessFragment successFragment = new SuccessFragment();

                Bundle args = new Bundle();
                args.putString("service", selectedService);
                args.putString("doctor", selectedDoctor);
                ((BookAppointment) getActivity()).loadNextFragment(ConfirmAppointmentFragment.class, args);

                // Replace the current fragment with the success fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, successFragment);
                transaction.addToBackStack(null); // Add to back stack for navigation
                transaction.commit();
            }
        });
    }
}
