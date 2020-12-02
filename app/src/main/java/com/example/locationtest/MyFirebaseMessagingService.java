package com.example.locationtest;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;

import java.io.IOException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";

    /**
     * Called if FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve
     * the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // final FirebaseDatabase database = FirebaseDatabase.getInstance();
        // DatabaseReference ref = database.getReference("server/saving-data/Tokens");
        // ref.push().setvalue(token);

        // SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // ref.edit().putString("firebase token", token).apply();
        UploadTokensToServer upload = new UploadTokensToServer();
        try{
            // upload.uploadToServer("http://localhost:3000/tokens", getSharedPreferences("firebase token", MODE_PRIVATE).toString());
            upload.uploadToServer("http://localhost:3000/tokens", token);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String messageBody = remoteMessage.getNotification().getBody();
        String messageTitle = remoteMessage.getNotification().getTitle();
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // TODO(developer): Handle FCM messages here.

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_001")
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(1)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(notifyPendingIntent);

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
