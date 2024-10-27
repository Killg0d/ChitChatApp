package com.example.project;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class AccountSettings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("Accounts");
        actionbar.setDisplayHomeAsUpEnabled(true);
        ListView optionView = findViewById(R.id.option_view);
        ArrayList<SettingOption> items = new ArrayList<>();
        items.add(new SettingOption("Email Address", R.mipmap.ic_email));
        items.add(new SettingOption("Change Password", R.mipmap.ic_lock));
        items.add(new SettingOption("Delete Account", R.mipmap.ic_bin));
        SettingOptionAdapter aa = new SettingOptionAdapter(this, items);
        optionView.setAdapter(aa);
        optionView.setOnItemClickListener((adapterView, view, i, l) -> {
            SettingOption item = (SettingOption) optionView.getItemAtPosition(i);
            if (item.getName().equals("Email Address")) {
                Dialog dialog = new Dialog(AccountSettings.this);
                dialog.setContentView(R.layout.custom_edit_box);

                // Find the close button and input fields
                ImageView closeButton = dialog.findViewById(R.id.close_button);
                EditText oldEmailInput = dialog.findViewById(R.id.old_input);
                EditText newEmailInput = dialog.findViewById(R.id.new_input);
                Button confirm = dialog.findViewById(R.id.confirm_button);
                // Close button listener
                closeButton.setOnClickListener(v -> {
                    //Code to change email will come here

                    dialog.dismiss();  // Close the dialog
                });
                TextView oldText = dialog.findViewById(R.id.textview_1);
                TextView newText = dialog.findViewById(R.id.textview2);

                oldText.setText(R.string.old_email);
                newText.setText(R.string.new_email);
                oldEmailInput.setHint("Enter old email");
                newEmailInput.setHint("Enter new email");
                // Confirm button Listner
                confirm.setOnClickListener(view1 -> {
                    String oldEmail = oldEmailInput.getText().toString().trim();
                    String newEmail = newEmailInput.getText().toString().trim();

                    // Validate the inputs
                    if (oldEmail.isEmpty() || newEmail.isEmpty()) {
                        Toast.makeText(AccountSettings.this, "Both fields must be filled.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    // Check if the old email matches the current user's email
                    if (user != null && user.getEmail() != null && user.getEmail().equals(oldEmail)) {
                        // Proceed to update email
                        user.updateEmail(newEmail)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Get the current user's UID
                                        String userId = user.getUid();
                                        Log.d(TAG, userId);
                                        // Update the email in Firestore
                                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                        firestore.collection("users").document(userId)
                                                .update("email", newEmail)
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.d(TAG, "User email in Firestore updated successfully.");
                                                    Toast.makeText(AccountSettings.this, "Email updated successfully.", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e(TAG, "Failed to update email in Firestore: " + e.getMessage());
                                                    Toast.makeText(AccountSettings.this, "Email updated in Firebase but failed in Firestore. Try again.", Toast.LENGTH_SHORT).show();
                                                });


                                        Log.d(TAG, "User email address updated.");

                                        Toast.makeText(AccountSettings.this, "Email updated successfully.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e(TAG, "Failed to update email: " + task.getException().getMessage());
                                        Toast.makeText(AccountSettings.this, "Failed to update email. Try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(AccountSettings.this, "Old email does not match.", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();  // Close the dialog after processing
                });

                // Show the dialog
                dialog.show();
            } else if (item.getName().equals("Change Password")) {
                Dialog dialog = new Dialog(AccountSettings.this);
                dialog.setContentView(R.layout.custom_edit_box);

                // Find the close button and input fields
                ImageView closeButton = dialog.findViewById(R.id.close_button);
                EditText oldPasswordInput = dialog.findViewById(R.id.old_input);
                EditText newPasswordInput = dialog.findViewById(R.id.new_input);
                TextView oldText = dialog.findViewById(R.id.textview_1);
                TextView newText = dialog.findViewById(R.id.textview2);
                Button confirm = dialog.findViewById(R.id.confirm_button);
                // Close button listener
                closeButton.setOnClickListener(v -> {
                    //Code to change email will come here

                    dialog.dismiss();  // Close the dialog
                });
                oldText.setText(R.string.old_password);
                newText.setText(R.string.new_password);
                oldPasswordInput.setHint("Enter old password");
                newPasswordInput.setHint("Enter new password");
                // Close button listener
                closeButton.setOnClickListener(v -> {
                    dialog.dismiss();  // Close the dialog
                });
                confirm.setOnClickListener(view1 -> {

                    dialog.dismiss();
                });
                String oldEmail = oldPasswordInput.getText().toString();
                String newEmail = newPasswordInput.getText().toString();

                // Show the dialog
                dialog.show();
            } else if (item.getName().equals("Delete Account")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountSettings.this);
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete the account?");
                builder.setPositiveButton("Yes", (dialogInterface, i1) -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    assert user != null;
                    String userId = user.getUid(); // Get the user ID

                    // Proceed to delete user data from Firestore first
                    deleteUserData(userId, new DeleteUserDataCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "User data deleted from Firestore.");

                            // After successful data deletion, delete the user from FirebaseAuth
                            user.delete()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted from FirebaseAuth.");
                                            Toast.makeText(AccountSettings.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();

                                            // Update shared preferences and sign out
                                            SharedPreferences p = getSharedPreferences("Login", MODE_PRIVATE);
                                            p.edit().putBoolean("isLogin?", false).commit();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(AccountSettings.this, Login.class));
                                            finish();
                                        } else {
                                            Exception exception = task.getException();
                                            if (exception instanceof FirebaseAuthRecentLoginRequiredException) {
                                                // Handle reauthentication required error
                                                Toast.makeText(AccountSettings.this, "Reauthentication required. Please log in again.", Toast.LENGTH_LONG).show();

                                                // Sign out and redirect to login page
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(AccountSettings.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.e(TAG, "Error deleting user account from FirebaseAuth", exception);
                                                Toast.makeText(AccountSettings.this, "Error deleting account from FirebaseAuth. Try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            // Log the failure of data deletion
                            Toast.makeText(AccountSettings.this, "Failed to delete user data. Account not deleted.", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Failed to delete all Firestore user data", e);
                        }
                    });
                });

                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                builder.show();


            }
        });
    }

    public interface DeleteUserDataCallback {
        void onSuccess();

        void onFailure(Exception e);
    }

    private void deleteUserData(String userId, DeleteUserDataCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        WriteBatch batch = firestore.batch(); // Create a batch for updates

        // 1. Remove the user document from the users collection
        DocumentReference userRef = firestore.collection("users").document(userId);
        batch.delete(userRef);

        // 2. Remove user from participants list in all chats
        firestore.collection("chats")
                .whereArrayContains("participants", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference chatRef = document.getReference();

                            // Remove the user ID from the participants array
                            batch.update(chatRef, "participants", FieldValue.arrayRemove(userId));
                        }

                        // 3. Commit the batch after adding all updates
                        batch.commit().addOnCompleteListener(commitTask -> {
                            if (commitTask.isSuccessful()) {
                                Log.d(TAG, "Successfully removed user from participants and deleted user data.");
                                callback.onSuccess(); // Notify success
                            } else {
                                Log.e(TAG, "Error committing batch update", commitTask.getException());
                                callback.onFailure(commitTask.getException()); // Notify failure
                            }
                        });
                    } else {
                        Log.e(TAG, "Error getting chats", task.getException());
                        callback.onFailure(task.getException()); // Notify failure
                    }
                });
    }


}