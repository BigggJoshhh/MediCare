package com.example.medicare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends BaseActivity  {
    EditText editTextEmail, editTextPassword;
    TextView textViewLogin;
    Button buttonCreate;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DocumentReference docRef = db.collection("users").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String role = "user";
                    Intent intent;
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            role = document.getString("role");
                        }
                    }
                    if (role.equals("doctor")) {
                        intent = new Intent(getApplicationContext(), DoctorAppointment.class);
                    } else {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                    }
                    Toast.makeText(getApplicationContext(), "Authentication sucesss.",
                            Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            });

            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Uri photoUrl = currentUser.getPhotoUrl();
            boolean emailVerified = currentUser.isEmailVerified();
            String uid = currentUser.getUid();

            // Log the details
            Log.d("FirebaseUser", "Name: " + name);
            Log.d("FirebaseUser", "Email: " + email);
            Log.d("FirebaseUser", "Photo URL: " + (photoUrl != null ? photoUrl.toString() : "null"));
            Log.d("FirebaseUser", "Email Verified: " + emailVerified);
            Log.d("FirebaseUser", "UID: " + uid);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        textViewLogin = findViewById(R.id.tv_register);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonCreate = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;

                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DocumentReference docRef = db.collection("users").document(user.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            String role = "user";
                                            Intent intent;
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    role = document.getString("role");
                                                }
                                            }
                                            if (role.equals("doctor")) {
                                                intent = new Intent(getApplicationContext(), DoctorAppointment.class);
                                            } else {
                                                intent = new Intent(getApplicationContext(), MainActivity.class);
                                            }
                                            Toast.makeText(getApplicationContext(), "Authentication sucess.",
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}