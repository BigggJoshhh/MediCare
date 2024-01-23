package com.example.medicare.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.medicare.EditProfile;
import com.example.medicare.LanguageSelect;
import com.example.medicare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import classes.User;

public class SettingsFragment extends Fragment {

    LinearLayout editProfilePage;
    LinearLayout languagePage;
    ImageView imageView;
    TextView username;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        languagePage = view.findViewById(R.id.language_card);
        editProfilePage = view.findViewById(R.id.edit_profile_card);
        db = FirebaseFirestore.getInstance();

        currentUser = mAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Log.d("user current", currentUser.getUid());
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.d("user current", String.valueOf(documentSnapshot));

                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            username = view.findViewById(R.id.settings_username);
                            username.setText(user.getUsername());
                            imageView = view.findViewById(R.id.settings_image);
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
                Intent intent = new Intent(getActivity(), LanguageSelect.class);
                startActivity(intent);
            }
        });

        editProfilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });
    }
}
