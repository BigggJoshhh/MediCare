package com.example.medicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import classes.User;
import methods.UserDoc;

public class EditProfile extends AppCompatActivity {

    EditText username_et;
    EditText email_et;
    EditText phone_et;
    RadioGroup gender_radio;
    RadioButton male;
    RadioButton female;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String originalUsername;
    private String originalEmail;
    private String originalPhone;
    private String originalGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // Initialize EditText fields
        username_et = findViewById(R.id.edit_username_et);
        email_et = findViewById(R.id.edit_email_et);
        phone_et = findViewById(R.id.edit_phone_et);
        male = findViewById(R.id.gender_male);
        female = findViewById(R.id.gender_female);
        // Initialize RadioGroup for gender
        gender_radio = findViewById(R.id.gender_group);


        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            // Populate fields and store original data
                            originalUsername = user.getUsername();
                            originalEmail = user.getEmail();
                            originalPhone = user.getPhone_number();
                            originalGender = user.getGender();

                            username_et.setText(originalUsername);
                            email_et.setText(originalEmail);
                            phone_et.setText(originalPhone);

                            if ("Male".equals(originalGender)) {
                                male.setChecked(true);
                            } else if ("Female".equals(originalGender)) {
                                female.setChecked(true);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                    });

            findViewById(R.id.save_button).setOnClickListener(v -> {
                // Prepare a map to store updates
                Map<String, Object> updates = new HashMap<>();

                // Check each field for changes and add them to the updates map
                if (!username_et.getText().toString().equals(originalUsername)) {
                    updates.put("username", username_et.getText().toString());
                }
                if (!email_et.getText().toString().equals(originalEmail)) {
                    updates.put("email", email_et.getText().toString());
                }
                if (!phone_et.getText().toString().equals(originalPhone)) {
                    updates.put("phone_number", phone_et.getText().toString());
                }
                String selectedGender = gender_radio.getCheckedRadioButtonId() == R.id.gender_male ? "Male" : "Female";
                if (!selectedGender.equals(originalGender)) {
                    updates.put("gender", selectedGender);
                }

                // Update Firestore if there are changes
                if (!updates.isEmpty()) {
                    UserDoc userDoc = new UserDoc(currentUser.getUid());
                    userDoc.updateUserProfile(updates).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Updated Profile!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfile.this, Settings.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Update Profile!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfile.this, LanguageSelect.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                }
            });

            // Cancel button listener
            findViewById(R.id.cancel_button).setOnClickListener(v -> {
                // Handle cancel action (e.g., finish the activity)
                finish();
            });
        }
    }
};

