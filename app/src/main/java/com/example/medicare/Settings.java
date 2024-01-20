package com.example.medicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import classes.User;
import de.hdodenhof.circleimageview.CircleImageView;
import methods.UserDoc;


public class Settings extends AppCompatActivity {

    LinearLayout editProfilePage;
    LinearLayout languagePage;
    CircleImageView imageView;
    TextView username;
    UserDoc userDoc;
    User user;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languagePage = findViewById(R.id.language_card);
        editProfilePage = findViewById(R.id.edit_profile_card);
        db = FirebaseFirestore.getInstance();

        currentUser = mAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Log.d("user current", currentUser.getUid());
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.d("user current", String.valueOf(documentSnapshot));

                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            username = findViewById(R.id.settings_username);
                            username.setText(user.getUsername());
                            imageView = findViewById(R.id.settings_image);
                            if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                                Glide.with(this)
                                        .load(Uri.parse(user.getPhoto()))
                                        .into(imageView);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                    });
        }

        languagePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, LanguageSelect.class);
                startActivity(intent);
                finish();
            }
        });

        editProfilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, EditProfile.class);
                startActivity(intent);
                finish();
            }
        });
    }
}