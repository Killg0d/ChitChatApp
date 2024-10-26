package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageTextAdapter extends ArrayAdapter<Message> {
    private Context context;
    private List<Message> messages;

    public MessageTextAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message messageText = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_message_text, parent, false);
        }

        // UI Elements for both user and recipient messages
        LinearLayout llCurrentUser = convertView.findViewById(R.id.linearText);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView messageTextView = convertView.findViewById(R.id.myText);
        TextView sentAtTextView = convertView.findViewById(R.id.sentAtTextView);

        LinearLayout llOtherUser = convertView.findViewById(R.id.linearText2);
        TextView nameTextView2 = convertView.findViewById(R.id.nameTextView2);
        TextView messageTextView2 = convertView.findViewById(R.id.myText2);
        TextView sentAtTextView2 = convertView.findViewById(R.id.sentAtTextView2);

        String senderId = messageText.getSenderId();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Load user details and set name in the UI
        FirebaseFirestore.getInstance().collection("users").document(senderId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            nameTextView.setText(user.getFullName());
                            nameTextView2.setText(user.getFullName());
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d("Error", "Failed to load user details"));

        // Set message content and timestamp
        messageTextView.setText(messageText.getMessage());
        messageTextView2.setText(messageText.getMessage());
        Timestamp timestamp = messageText.getTimestamp();
        if (timestamp != null) {
            Date date = timestamp.toDate();
            sentAtTextView.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date));
            sentAtTextView2.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date));
        } else {
            sentAtTextView.setText("Unknown Time");
            sentAtTextView2.setText("Unknown Time");
        }

        if (senderId.equals(currentUserId)) {
            // For the current user's messages
            llCurrentUser.setVisibility(View.VISIBLE);
            llOtherUser.setVisibility(View.GONE);

        } else {
            // For other users' messages
            llCurrentUser.setVisibility(View.GONE);
            llOtherUser.setVisibility(View.VISIBLE);

        }

        return convertView;
    }

}
