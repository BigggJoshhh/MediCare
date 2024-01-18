package com.example.medicare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {
    // Initialize variable
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_appointment_status, container, false);

        // Assign variable
        textView = view.findViewById(R.id.text_view);

        // Check if arguments are available and get the title if so
        if (getArguments() != null) {
            String sTitle = getArguments().getString("title", "Default Title"); // Provide a default value
            // Set title on text view
            textView.setText(sTitle);
        }

        // return view
        return view;
    }
}
