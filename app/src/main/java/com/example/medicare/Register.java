package com.example.medicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText editTextUsername, editTextEmail, editTextPhoneNumber, editTextPassword;
    TextView textViewLogin;
    Button buttonCreate;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    private void saveUserDetailsToFirestore(FirebaseUser user, String phone_number) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with details
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", user.getDisplayName());
        userDetails.put("email", user.getEmail());
        userDetails.put("phone_number", phone_number);
        userDetails.put("notification", true);
        userDetails.put("language", "English");
        userDetails.put("photo", "https://firebasestorage.googleapis.com/v0/b/ande-ca2-f3d28.appspot.com/o/default-no-image.png?alt=media&token=851bfd15-c7fb-41f0-ab7e-17c92cd69a0d");

        // Add other details you want to store

        // Add a new document with a generated ID
        db.collection("users").document(user.getUid())
                .set(userDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("RegisterActivity", "DocumentSnapshot successfully written!");
                        // Handle success - e.g., navigate to another activity
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("RegisterActivity", "Error writing document", e);
                        // Handle the error
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        textViewLogin = findViewById(R.id.tv_login);
        editTextUsername = findViewById(R.id.et_username);
        editTextEmail = findViewById(R.id.et_email);
        editTextPhoneNumber = findViewById(R.id.et_phone_number);
        editTextPassword = findViewById(R.id.et_password);
        buttonCreate = findViewById(R.id.btn_create);
        progressBar = findViewById(R.id.progress_bar);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String username, email, phone_number, password;

                username = String.valueOf(editTextUsername.getText());
                email = String.valueOf(editTextEmail.getText());
                phone_number = String.valueOf(editTextPhoneNumber.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Register.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phone_number)) {
                    Toast.makeText(Register.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getInstance().getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            // .setPhotoUri(Uri.parse("Your photo uri")) // If you have a photo URL
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("RegisterActivity", "User profile updated.");
                                                        saveUserDetailsToFirestore(user, phone_number);
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                    Toast.makeText(Register.this, "Authentication success!.",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });


    }
}

