package com.example.medicare;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medicare.fragments.AppointmentFragment;
import com.example.medicare.fragments.HomeFragment;
import com.example.medicare.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_START_FRAGMENT = "com.example.medicare.START_FRAGMENT";
    public static final String FRAGMENT_HOME = "HomeFragment";
    public static final String FRAGMENT_CALENDAR = "CalendarFragment";
    public static final String FRAGMENT_APPT = "AppointmentFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Check for the starting fragment extra
        String startFragment = getIntent().getStringExtra(EXTRA_START_FRAGMENT);
        if (startFragment != null) {
            selectFragment(startFragment);
        } else {
            // Default starting fragment
            bottomNav.setSelectedItemId(R.id.navigation_home);
        }
    }


    private void selectFragment(String fragmentName) {
        Fragment selectedFragment = null;
        int navItemId = R.id.navigation_home; // default ID
        switch (fragmentName) {
            case FRAGMENT_HOME:
                selectedFragment = new HomeFragment();
                navItemId = R.id.navigation_home;
                break;
            case FRAGMENT_CALENDAR:
                selectedFragment = new SettingsFragment();
                navItemId = R.id.navigation_settings;
                break;
            case FRAGMENT_APPT:
                selectedFragment = new AppointmentFragment();
                navItemId = R.id.navigation_appt;
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(navItemId);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                        case R.id.navigation_appt:
                            displayFragment(new AppointmentFragment());
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }

                    return true;
                }
            };

    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment) // Use your actual container ID
                .commit();
    }
}
