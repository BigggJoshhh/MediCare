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
import com.google.firebase.firestore.QuerySnapshot;

import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;

public class AppointmentFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    MainAdapter adapter;
    ArrayList<MainFragment> fragments = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointments, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        fetchAppointmentsFromFirestore();

        return view;
    }
    private void setupViewPager(ArrayList<Appointment> upcomingAppointments,
                               ArrayList<Appointment> missedAppointments,
                               ArrayList<Appointment> openAppointments) {
        fragments.add(MainFragment.newInstance(upcomingAppointments)); // Upcoming
        fragments.add(MainFragment.newInstance(missedAppointments));   // Missed
        fragments.add(MainFragment.newInstance(openAppointments));     // Open

        adapter = new MainAdapter(this, fragments);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapter.getPageTitle(position))
        ).attach();
    }


    private void fetchAppointmentsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appointment")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Separate the appointments based on their status
                        ArrayList<Appointment> upcomingAppointments = new ArrayList<>();
                        ArrayList<Appointment> missedAppointments = new ArrayList<>();
                        ArrayList<Appointment> openAppointments = new ArrayList<>();

                        // Process the fetched appointments
                        processFetchedAppointments(task.getResult(),
                                upcomingAppointments, missedAppointments, openAppointments);

                        // Now that we have the data, set up the ViewPager with the correct fragments
                        setupViewPager(upcomingAppointments, missedAppointments, openAppointments);
                    } else {
                        Log.d("Firestore Error", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void processFetchedAppointments(QuerySnapshot result,
                                            ArrayList<Appointment> upcomingAppointments,
                                            ArrayList<Appointment> missedAppointments,
                                            ArrayList<Appointment> openAppointments) {
        for (DocumentSnapshot document : result) {
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
                    default:
                        Log.d("AppointmentStatus", "Unknown status for appointment: " + appointment.getService());
                        break;
                }
            }
        }
        Log.d("UpdateData", "Upcoming: " + upcomingAppointments.size());
        Log.d("UpdateData", "Missed: " + missedAppointments.size());
        Log.d("UpdateData", "Open: " + openAppointments.size());
    }

    private static class MainAdapter extends FragmentStateAdapter {
        private final ArrayList<MainFragment> fragments;
        private final String[] tabTitles = new String[]{"Upcoming", "Missed", "Open"};

        MainAdapter(Fragment fragment, ArrayList<MainFragment> fragments) {
            super(fragment.getChildFragmentManager(), fragment.getLifecycle());
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

        CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}