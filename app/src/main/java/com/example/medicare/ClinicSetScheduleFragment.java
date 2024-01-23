package com.example.medicare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import classes.Appointment;

public class ClinicSetScheduleFragment extends Fragment {

    public ClinicSetScheduleFragment(ArrayList<Appointment> arrayList){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clinic_set_schedule, container, false);
    }
}