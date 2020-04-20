package com.techuva.iot.utils.views;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techuva.iot.R;
import com.techuva.iot.activity.HBLNotificationActivity;
import com.techuva.iot.app.Constants;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 *
 * <intent-filter>
 *   <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private EventListener listener;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
      Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            int i = Integer.parseInt(MApplication.getString(this, Constants.NotificationCount));
            i = i+1;
            MApplication.setString(this, Constants.NotificationCount, String.valueOf(i));
           // Dashboard.UpdateNotificationCount(this);
            listener.onEvent(true);
            //sendNotification(remoteMessage.getData().get("Alert"));
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData().get("Alert"));
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
              //  scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            int i = Integer.parseInt(MApplication.getString(this, Constants.NotificationCount));
            i = i+1;
            MApplication.setString(this, Constants.NotificationCount, String.valueOf(i));
            //Dashboard.UpdateNotificationCount(this);
            //listener.onEvent(true);
            sendNotification(remoteMessage.getData().get("Alert"));
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //  scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

       // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
//    private void scheduleJob() {
//        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance().beginWith(work).enqueue();
//        // [END dispatch_job]
//    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }


    /*  NotificationChannel mChannel;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        mChannel = new NotificationChannel(CHANNEL_ID, Utils.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        mChannel.setLightColor(Color.GRAY);
        mChannel.enableLights(true);
        mChannel.setDescription(Utils.CHANNEL_SIREN_DESCRIPTION);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_NOTIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        mChannel.setSound(soundUri, audioAttributes);

        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel( mChannel );
        }
    }*/

    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, HBLNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.app_name);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri defaultSoundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() +"/" + R.raw.bell);
            Log.e("Sound", defaultSoundUri.toString());
            Notification.Builder notificationBuilder =
                    new Notification.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.icon_small)
                            .setContentTitle(getApplicationContext().getString(R.string.app_name))
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setNumber(1)
                            //.setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                            //.setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            //notificationChannel.setSound(defaultSoundUri,att);
            notificationChannel.setDescription(messageBody);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
           /* if (imageThumbnail != null) {
                notificationBuilder.setStyle(new Notification.BigPictureStyle()
                        .bigPicture(imageThumbnail).setSummaryText(messageBody));
            }*/
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        } else {
            Uri defaultSoundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.bell);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.icon_small)
                            .setContentTitle(getApplicationContext().getString(R.string.app_name))
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                            //.setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        }
    }

    public interface EventListener {
        void onEvent(Boolean data);

        void onDelete(Boolean data);
    }


}

