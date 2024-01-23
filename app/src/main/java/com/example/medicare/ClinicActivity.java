package com.example.medicare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import classes.Appointment;

public class ClinicActivity extends AppCompatActivity {
    ArrayList<ClinicTab> fragments = new ArrayList<>();
    ArrayList<Appointment> requestedAppointments = new ArrayList<>();
    ArrayList<Appointment> scheduledAppointments = new ArrayList<>();
    ArrayList<Appointment> cancelledAppointments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);

        getDBData();

        fragments.add(ClinicTab.newInstance(requestedAppointments));
        fragments.add(ClinicTab.newInstance(scheduledAppointments));
        fragments.add(ClinicTab.newInstance(cancelledAppointments));

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabAdapter tabAdapter = new TabAdapter(this, fragments);
        viewPager.setAdapter(tabAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabAdapter.getPageTitle(position))).attach();
    }

    protected void getDBData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appointment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Appointment tempAppointment = document.toObject(Appointment.class);
                        tempAppointment.setAppointmentId(document.getId());
                        switch (tempAppointment.getStatus()) {
                            case "pending":
                                requestedAppointments.add(tempAppointment);
                                break;
                            case "upcoming":
                                scheduledAppointments.add(tempAppointment);
                                break;
                            case "cancelled":
                                cancelledAppointments.add(tempAppointment);
                                break;
                            default:
                                Log.d("AppointmentStatus", "Unknown status for appointment: " + tempAppointment.getService());
                                break;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Get Appointments failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class TabAdapter extends FragmentStateAdapter {
        private final ArrayList<ClinicTab> fragments;
        private final String[] tabTitles = new String[]{ getApplicationContext().getString(R.string.clinic_requested), getApplicationContext().getString(R.string.clinic_scheduled), getApplicationContext().getString(R.string.clinic_cancelled)};
        public TabAdapter(FragmentActivity  fragmentActivity, ArrayList<ClinicTab> fragments) {
            super(fragmentActivity);
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
        public String getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}