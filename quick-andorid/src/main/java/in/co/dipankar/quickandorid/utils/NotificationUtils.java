package in.co.dipankar.quickandorid.utils;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import in.co.dipankar.quickandorid.R;

public class NotificationUtils {
    public static void openNotification(Context context, String title, String subtitle){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        Notification noti = mBuilder.build();

    }

    public static void openNotificationWithAction(Context context, String title, String subtitle, Intent intent){
       // Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
       // snoozeIntent.setAction(ACTION_SNOOZE);
        // snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1234")
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(snoozePendingIntent)
                .addAction(R.id.accept, "OK", snoozePendingIntent);
    }
}
