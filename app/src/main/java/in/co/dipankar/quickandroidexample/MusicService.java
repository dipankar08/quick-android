package in.co.dipankar.quickandroidexample;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import in.co.dipankar.quickandorid.services.MusicForegroundService;


public class MusicService extends MusicForegroundService{

    @Override
    protected Class getActivityClass() {
        return MainActivity.class;
    }
}
