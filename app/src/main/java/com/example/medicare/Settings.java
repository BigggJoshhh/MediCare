package com.example.medicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    LinearLayout editProfilePage;
    LinearLayout languagePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languagePage = findViewById(R.id.language_card);
        editProfilePage = findViewById(R.id.edit_profile_card);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView username = findViewById(R.id.user_name);
        username.setText(currentUser.getDisplayName());
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