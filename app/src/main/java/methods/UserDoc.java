package methods;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import classes.User;

public class UserDoc {

    private FirebaseFirestore db;
    private DocumentReference userDocRef;

    public UserDoc(String userId) {
        db = FirebaseFirestore.getInstance();
        userDocRef = db.collection("users").document(userId);
    }

    public Task<User> fetchUser() {
        return userDocRef.get().continueWith(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().toObject(User.class);
                Log.d("User LOGGED RN", String.valueOf(user));
                return user;
            } else {
                throw task.getException();
            }
        });
    }

    public Task<Void> updateEmail(String email) {
        return userDocRef.update("email", email);
    }

    public Task<Void> updateGender(String gender) {
        return userDocRef.update("gender", gender);
    }

    public Task<Void> updateLanguage(String language) {
        return userDocRef.update("language", language);
    }

    public Task<Void> updateNotification(boolean notification) {
        return userDocRef.update("notification", notification);
    }

    public Task<Void> updatePhoneNumber(String phoneNumber) {
        return userDocRef.update("phone_number", phoneNumber);
    }

    public Task<Void> updateUsername(String username) {
        return userDocRef.update("username", username);
    }

    // Update multiple fields at once
    public Task<Void> updateUserProfile(Map<String, Object> updates) {
        return userDocRef.update(updates);
    }

    // Method to update all user info at once
    public Task<Void> updateAllUserInfo(String email, String gender, String language,
                                        boolean notification, String phoneNumber, String username) {
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("email", email);
        userUpdates.put("gender", gender);
        userUpdates.put("language", language);
        userUpdates.put("notification", notification);
        userUpdates.put("phone_number", phoneNumber);
        userUpdates.put("username", username);

        return updateUserProfile(userUpdates);
    }
}
