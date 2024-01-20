package com.example.medicare;

import android.app.Activity;
import android.content.Intent;
import android.Manifest;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


// Your custom classes
import classes.*;
import de.hdodenhof.circleimageview.CircleImageView;
import methods.*;


public class EditProfile extends AppCompatActivity {

    CircleImageView imageView;
    private Uri selectedImageUri = null;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    EditText username_et;
    EditText email_et;
    EditText phone_et;
    RadioGroup gender_radio;
    RadioButton male;
    RadioButton female;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private String originalUsername;
    private String originalEmail;
    private String originalPhone;
    private String originalGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        verifyStoragePermissions(this);
        // Initialize EditText fields
        username_et = findViewById(R.id.edit_username_et);
        email_et = findViewById(R.id.edit_email_et);
        phone_et = findViewById(R.id.edit_phone_et);
        male = findViewById(R.id.gender_male);
        female = findViewById(R.id.gender_female);

        imageView = findViewById(R.id.profile_image);

        // Initialize RadioGroup for gender
        gender_radio = findViewById(R.id.gender_group);


        // Firebase fetchUser
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


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
                            Glide.with(this)
                                    .load(Uri.parse(user.getPhoto()))
                                    .into(imageView);


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

            imageView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                galleryActivityResultLauncher.launch(intent);
            });

            galleryActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            imageView.setImageURI(selectedImageUri); // Directly set the URI to the imageView
//                            Glide.with(this)
//                                    .load(selectedImageUri)
//                                    .into(imageView);
                        }
                    }
            );


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

                if (selectedImageUri != null) {
                    uploadImageToFirebase(selectedImageUri, new OnImageUploadCompleteListener() {
                        @Override
                        public void onImageUploadComplete(String imageUrl) {
                            updates.put("photo", imageUrl);
                            Toast.makeText(EditProfile.this, "Profile image updated!", Toast.LENGTH_SHORT).show();
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
                                        Intent intent = new Intent(EditProfile.this, Settings.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                            }
                        }

                        @Override
                        public void onImageUploadFailed(Exception exception) {
                            Toast.makeText(EditProfile.this, "Failed to upload image: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // Cancel button listener
            findViewById(R.id.cancel_button).setOnClickListener(v -> {
                Intent intent = new Intent(EditProfile.this, Settings.class);
                startActivity(intent);
                finish();
            });
        }
    }


    private void uploadImageToFirebase(Uri imageUri, final OnImageUploadCompleteListener listener) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference()
                .child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profile.jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Handle the success
                    listener.onImageUploadComplete(uri.toString());
                }))
                .addOnFailureListener(listener::onImageUploadFailed);
    }



    public interface OnImageUploadCompleteListener {
        void onImageUploadComplete(String imageUrl);

        void onImageUploadFailed(Exception exception);
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have read permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }



};

