package com.example.medicare;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;
import java.util.Map;


// Your custom classes
import classes.*;
import de.hdodenhof.circleimageview.CircleImageView;
import methods.*;


public class EditProfile extends AppCompatActivity {

    ImageView imageView;
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

            galleryActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            imageView.setImageURI(result.getData().getData());
                        }
                    }
            );

            imageView.setOnClickListener(v -> {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(openGallery);
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

                if (selectedImageUri != null) {
                    uploadImageToFirebase(selectedImageUri, new OnImageUploadCompleteListener() {
                        @Override
                        public void onImageUploadComplete(String imageUrl) {
                            imageView.setImageURI(Uri.parse(imageUrl));
                            updates.put("photo", imageUrl);

                            Toast.makeText(EditProfile.this, "Profile image updated!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onImageUploadFailed(Exception exception) {
                            Toast.makeText(EditProfile.this, "Failed to upload image: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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

    private void uploadImageToFirebase(Uri imageUri, final OnImageUploadCompleteListener listener) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference()
                .child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build();

                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    listener.onImageUploadComplete(uri.toString());
                                } else {
                                    listener.onImageUploadFailed(task.getException());
                                }
                            });
                }))
                .addOnFailureListener(listener::onImageUploadFailed);
    }

    public interface OnImageUploadCompleteListener {
        void onImageUploadComplete(String imageUrl);

        void onImageUploadFailed(Exception exception);
    }


};

