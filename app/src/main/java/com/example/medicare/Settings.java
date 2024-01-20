package com.example.medicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import classes.User;
import de.hdodenhof.circleimageview.CircleImageView;
import methods.UserDoc;


public class Settings extends AppCompatActivity {

    LinearLayout editProfilePage;
    LinearLayout languagePage;
    de.hdodenhof.circleimageview.CircleImageView imageView;
    UserDoc userDoc;
    User user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languagePage = findViewById(R.id.language_card);
        editProfilePage = findViewById(R.id.edit_profile_card);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            UserDoc userdoc = new UserDoc(currentUser.getUid());
            userdoc.fetchUser().addOnSuccessListener(new OnSuccessListener<User>() {
                @Override
                public void onSuccess(User userResult) {
                    // This code will run on the main thread, it's safe to update UI components
                    TextView username = findViewById(R.id.user_name);
                    CircleImageView imageView = findViewById(R.id.profile_image);

//                    username.setText(userResult.getUsername());
//                    Glide.with(Settings.this)
//                            .load(Uri.parse(user.getPhoto()))
//                            .into(imageView);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the error
                    Log.e("Settings", "Error fetching user", e);
                }
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