package methods;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuth {

    private com.google.firebase.auth.FirebaseAuth mAuth;

    // Constructor
    public FirebaseAuth() {
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
    }

    // Sign up new users
    public void signUp(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    // Sign in existing users
    public void signIn(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    // Sign out the current user
    public void signOut() {
        mAuth.signOut();
    }

    // Send password reset email
    public void sendPasswordResetEmail(String email, OnCompleteListener<Void> listener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(listener);
    }

    // Change the user password
    public void changePassword(FirebaseUser user, String newPassword, OnCompleteListener<Void> listener) {
        user.updatePassword(newPassword).addOnCompleteListener(listener);
    }

    // Get the currently signed-in user
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Check if the user is signed in
    public boolean isUserSignedIn() {
        return getCurrentUser() != null;
    }
}
