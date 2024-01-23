package com.example.medicare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.medicare.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookAppointmentFragment extends Fragment {

    private Spinner servicesSpinner;
    private Spinner doctorsSpinner;
    private ArrayAdapter<String> servicesAdapter;
    private ArrayAdapter<String> doctorsAdapter;

    private final Map<String, List<String>> servicesToDoctorsMap = new HashMap<String, List<String>>() {{
        put("Medical Consultation", Arrays.asList("Dr. Octopus", "Dr. Strange"));
        put("Dental Checkup", Arrays.asList("Dr. Smile", "Dr. Tooth"));
        // Add other services and doctors as needed
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        servicesSpinner = view.findViewById(R.id.spinner_services);
        doctorsSpinner = view.findViewById(R.id.spinner_doctors);

        // Create an ArrayAdapter for services
        servicesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>(servicesToDoctorsMap.keySet()));
        servicesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicesSpinner.setAdapter(servicesAdapter);

        // Listener for service selection to update doctors spinner
        servicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedService = (String) parent.getItemAtPosition(position);
                updateDoctorsSpinner(selectedService);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                doctorsSpinner.setAdapter(null);
            }
        });

        // Initial setup for doctors spinner with the first service
        updateDoctorsSpinner(servicesAdapter.getItem(0));


        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming you have the appointment details collected, pass them to the next fragment
                ConfirmAppointmentFragment confirmFragment = ConfirmAppointmentFragment.newInstance(
                        servicesSpinner.getSelectedItem().toString(),
                        doctorsSpinner.getSelectedItem().toString()
                );

                // Replace the current fragment with the confirmation fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, confirmFragment);
                transaction.addToBackStack(null); // Add to back stack for navigation
                transaction.commit();
            }
        });
    }

    private void updateDoctorsSpinner(String service) {
        List<String> doctors = servicesToDoctorsMap.get(service);

        // Create an ArrayAdapter for doctors based on the selected service
        doctorsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, doctors);
        doctorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorsSpinner.setAdapter(doctorsAdapter);
    }



}
