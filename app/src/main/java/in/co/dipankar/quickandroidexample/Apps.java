package in.co.dipankar.quickandroidexample;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import in.co.dipankar.quickandorid.utils.DLog;

public class Apps extends Application {
    public static final String CHANNEL_ID = "MUSIC_NOTIFICATION_CHANNEL";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        DLog.d("Crearing Notification Channel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}