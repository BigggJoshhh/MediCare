package com.example.medicare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import com.example.medicare.fragments.BookAppointmentFragment;

public class BookAppointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        if (savedInstanceState == null) {
            // Load the BookAppointmentFragment as the initial fragment
            loadBookAppointmentFragment();
        }
    }

    private void loadBookAppointmentFragment() {
        BookAppointmentFragment bookAppointmentFragment = new BookAppointmentFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, bookAppointmentFragment);
        transaction.commit();
    }

    // Method to be called from fragments when needing to navigate to the next step
    public void loadNextFragment(Class<? extends Fragment> fragmentClass, Bundle args) {
        Fragment fragmentInstance;

        try {
            // Create a new instance of the fragment class
            fragmentInstance = fragmentClass.newInstance();
            if (args != null) {
                // Set arguments if there are any
                fragmentInstance.setArguments(args);
            }

            // Replace the current fragment with the new one
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragmentInstance);
            transaction.addToBackStack(null); // Add to back stack for navigation
            transaction.commit();

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
}
