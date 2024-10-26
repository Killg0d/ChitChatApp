package com.example.project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.Timestamp;

public class FirebaseChatListenerService extends Service {
    private ListenerRegistration chatListener;
    private static final String TAG = "FirebaseChatListener";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationHelper.createNotificationChannel(this);  // Initialize the notification channel
        startChatListener();  // Start listening for chat updates
        return START_STICKY;
    }

    private void startChatListener() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Replace with actual user ID
        Log.d("Notification","Working");
        Log.d("FirebaseChatListener", "Service started");
        chatListener = FirebaseFirestore.getInstance().collection("chats")
                .whereArrayContains("participants", userId)
                .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Chat listener failed", e);
                        return;
                    }

                    if (snapshots != null && !snapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : snapshots) {
                            String chatId = document.getId();
                            Timestamp lastMessageTime = document.getTimestamp("lastMessageTime");
                            String messageContent = document.getString("lastMessage");

                            // Customize notification content here
                            sendNotification(chatId, messageContent);
                        }
                    }
                });
    }

    private void sendNotification(String chatId, String messageContent) {
        Intent intent = new Intent(this, MainActivity.class);  // Replace with your target activity
        intent.putExtra("chatId", chatId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat_notification)  // Replace with your notification icon
                .setContentTitle("New Chat Update")
                .setContentText(messageContent != null ? messageContent : "You have a new message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(chatId.hashCode(), notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatListener != null) {
            chatListener.remove();  // Clean up the listener when the service is destroyed
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

