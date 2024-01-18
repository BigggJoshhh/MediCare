package com.example.medicare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    RecyclerView appointmentsRecyclerView;
    AppointmentAdapter appointmentAdapter;
    List<Appointment> appointmentsList = new ArrayList<>();
     List<Appointment> upcomingAppointments = new ArrayList<>();
     List<Appointment> missedAppointments = new ArrayList<>();
     List<Appointment> openAppointments = new ArrayList<>();
     ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_appointments, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        appointmentsRecyclerView = view.findViewById(R.id.appointmentsRecyclerView);

        setupTabLayoutAndViewPager();
        setupRecyclerView();

        fetchAppointmentsFromFirestore();

        return view;
    }

    private void setupTabLayoutAndViewPager() {
        // Initialize array list
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Upcoming");
        arrayList.add("Missed");
        arrayList.add("Open");

        // Initialize adapter
        MainAdapter adapter = new MainAdapter(this);
        for (String title : arrayList) {
            MainFragment fragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment);
        }
        viewPager.setAdapter(adapter);

        // Connect TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(arrayList.get(position))
        ).attach();
    }

    private void setupRecyclerView() {
        appointmentAdapter = new AppointmentAdapter(appointmentsList);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentsRecyclerView.setAdapter(appointmentAdapter);
    }

    private void fetchAppointmentsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appointment")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear all lists before adding new items
                        upcomingAppointments.clear();
                        missedAppointments.clear();
                        openAppointments.clear();

                        for (DocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            if (appointment != null) {
                                switch (appointment.getStatus()) {
                                    case "upcoming":
                                        upcomingAppointments.add(appointment);
                                        break;
                                    case "missed":
                                        missedAppointments.add(appointment);
                                        break;
                                    case "open":
                                        openAppointments.add(appointment);
                                        break;
                                }
                            }
                        }
                        if (fragmentArrayList.size() > 2) {
                            MainFragment upcomingFragment = (MainFragment) fragmentArrayList.get(0);
                            MainFragment missedFragment = (MainFragment) fragmentArrayList.get(1);
                            MainFragment openFragment = (MainFragment) fragmentArrayList.get(2);
                            upcomingFragment.updateAppointments(upcomingAppointments);
                            missedFragment.updateAppointments(missedAppointments);
                            openFragment.updateAppointments(openAppointments);
                        }
                    } else {
                        Log.d("Firestore Error", "Error getting documents: ", task.getException());
                    }
                });
    }

    private static class MainAdapter extends FragmentStateAdapter {
        private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        public MainAdapter(Fragment fragment) {
            super(fragment.getChildFragmentManager(), fragment.getLifecycle());
        }

        public void addFragment(Fragment fragment) {
            fragmentArrayList.add(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentArrayList.size();
        }
    }
}
