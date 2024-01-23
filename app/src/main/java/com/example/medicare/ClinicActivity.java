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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;


public class ClinicActivity extends AppCompatActivity {
    ArrayList<ClinicTab> fragments = new ArrayList<>();
    ArrayList<Appointment> requestedAppointments = new ArrayList<>();
    ArrayList<Appointment> scheduledAppointments = new ArrayList<>();
    ArrayList<Appointment> cancelledAppointments = new ArrayList<>();
    DocumentReference clinicRef;
    FirebaseFirestore db;


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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        clinicRef = document.getDocumentReference("clinic");
                        fetchAppointments(clinicRef);
                    } else {
                        Toast.makeText(getApplicationContext(), "Get Clinic Failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Get Clinic Failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchAppointments (DocumentReference clinicRef) {
        db.collection("appointment").whereEqualTo("clinic", clinicRef).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Debug", Integer.toString(task.getResult().size()));
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