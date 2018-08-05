package in.co.dipankar.quickandorid.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import in.co.dipankar.quickandorid.R;
import in.co.dipankar.quickandorid.utils.DLog;
import in.co.dipankar.quickandorid.utils.MusicPlayerUtils;

import static in.co.dipankar.quickandorid.services.MusicForegroundService.Contracts.ACTION_TRY_PLAYING;
import static in.co.dipankar.quickandorid.services.MusicForegroundService.Contracts.CHANNEL_ID;
import static in.co.dipankar.quickandorid.services.MusicForegroundService.Contracts.NOTIFICATION_ID;
import static in.co.dipankar.quickandorid.services.MusicForegroundService.Contracts.PLAY_PAUSE;

public abstract class MusicForegroundService extends Service {
    private MusicPlayerUtils mMusicPlayerUtils;
    String mTitle;
    private List<PlayList> mPlayList;
    private int mCurIndex;


    public interface Contracts {
        public String START = "START_PLAY";
        public String ACTION_TRY_PLAYING = "TRY_PLAYING";
        public final String PLAY_PAUSE = "PLAY_PAUSE";
        public final String NEXT = "NEXT_PLAY";
        public String PREV = "NEXT_PREV";
        public String QUIT = "QUIT_PLAY";
        public int NOTIFICATION_ID = 1;
        // Note:: that You must create the channel in your application.
        public static final String CHANNEL_ID = "MUSIC_NOTIFICATION_CHANNEL";
        String OPEN_APP = "OPEN_APP";
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMusicPlayerUtils = new MusicPlayerUtils(this, new MusicPlayerUtils.IPlayerCallback() {
            @Override
            public void onTryPlaying(String id, String msg) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(ACTION_TRY_PLAYING));
            }

            @Override
            public void onSuccess(String id, String ms) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(PLAY_PAUSE));
            }

            @Override
            public void onResume(String id, String ms) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(PLAY_PAUSE));
            }

            @Override
            public void onPause(String id, String ms) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(PLAY_PAUSE));
            }

            @Override
            public void onMusicInfo(String id, HashMap<String, Object> info) {

            }

            @Override
            public void onSeekBarPossionUpdate(String id, int total, int cur) {

            }

            @Override
            public void onError(String id, String msg) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(PLAY_PAUSE));
            }

            @Override
            public void onComplete(String id, String ms) {

            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() == null) {
            return START_STICKY;
        }
        if (intent.getAction().equals(Contracts.START)) {
            DLog.d("MusicForegroundService:: Start called");
            String id = intent.getStringExtra("ID");
            String title = intent.getStringExtra("TITLE");
            mTitle = title;
            String url = intent.getStringExtra("URL");
            mMusicPlayerUtils.play(id, title, url);
        } else if (intent.getAction().equals(Contracts.PLAY_PAUSE)) {
            DLog.d("MusicForegroundService:: playPause called");
            if (mMusicPlayerUtils.isPlaying()) {
                mMusicPlayerUtils.pause();
            } else if (mMusicPlayerUtils.isPaused()) {
                mMusicPlayerUtils.resume();
            }
        } else if (intent.getAction().equals(Contracts.NEXT)) {
            DLog.d("MusicForegroundService:: Next called");
            // mMusicPlayerUtils.
        } else if (intent.getAction().equals(Contracts.PREV)) {
            DLog.d("MusicForegroundService:: Prev called");
            //mMusicPlayerUtils.
        } else if (intent.getAction().equals(Contracts.QUIT)) {
            DLog.d("MusicForegroundService:: Quit called");
            mMusicPlayerUtils.stop();
            stopForeground(true);
            stopSelf();
        }
        startForeground(Contracts.NOTIFICATION_ID, getNotification(intent.getAction()));
        return START_STICKY;

    }


    private Notification getNotification(String action){
        Intent notificationIntent = new Intent(this, getActivityClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Intent playPauseIntent = new Intent(this, this.getClass());
        playPauseIntent.setAction(Contracts.PLAY_PAUSE);
        PendingIntent pendingPlayPauseIntent = PendingIntent.getService(this, 0,
                playPauseIntent, 0);


        Intent nextPauseIntent = new Intent(this, this.getClass());
        nextPauseIntent.setAction(Contracts.NEXT);
        PendingIntent pendingNextPauseIntent = PendingIntent.getService(this, 0,
                nextPauseIntent, 0);


        Intent quitPauseIntent = new Intent(this, this.getClass());
        quitPauseIntent.setAction(Contracts.QUIT);
        PendingIntent pendingQuitPauseIntent = PendingIntent.getService(this, 0,
                quitPauseIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Best FM Radio India")
            .setContentText(mTitle)
            .setSmallIcon(R.drawable.ic_launcher)

            .setPriority(NotificationCompat.PRIORITY_HIGH);

        if(mMusicPlayerUtils.isPlaying()){
            notification.addAction(R.drawable.ic_launcher, "Pause", pendingPlayPauseIntent);
        } else{
            notification.addAction(R.drawable.ic_launcher, "Play", pendingPlayPauseIntent);
        }

        if(action.equals(ACTION_TRY_PLAYING)){
            notification.setContentText("Wait ... Try playing..");
        } else{
            notification.setContentText(mTitle);
        }

        // TODO.
       // notification.addAction(R.drawable.ic_launcher, "Next", pendingNextPauseIntent);
        notification.addAction(R.drawable.ic_launcher, "Quit", pendingQuitPauseIntent);

        return notification.build();
    }

    protected abstract Class getActivityClass();

    @Override
    public void onDestroy() {
        super.onDestroy();
        DLog.d("MusicForegroundService:: onDestroy called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    public final class PlayList{
        private String name;
        private String id;
        private String url;
        public PlayList(String id, String name, String url){
            this.id = id;
            this.name= name;
            this.url = url;
        }
    }
}


