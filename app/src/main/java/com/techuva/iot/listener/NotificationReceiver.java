package com.techuva.iot.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.techuva.iot.R;


public class NotificationReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        playNotificationSound(context);
    }

    public void playNotificationSound(Context context) {
        try {
           // Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Uri notification = Uri.parse("android.resource://" + context.getPackageName() +"/" + R.raw.bell);
            Ringtone r = RingtoneManager.getRingtone(context, notification);

            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}