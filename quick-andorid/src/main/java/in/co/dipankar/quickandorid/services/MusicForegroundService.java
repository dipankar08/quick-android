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
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.Serializable;
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
    
    public interface Callback{

        void onTryPlaying(String id, String msg);

        void onSuccess(String id, String ms);

        void onResume(String id, String ms);

        void onPause(String id, String msg);

        void onError(String id, String msg);

        void onSeekBarPossionUpdate(String id, int total, int cur);

        void onMusicInfo(String id, HashMap<String, Object> info);
        void onComplete(String id, String msg);
    }
    private MusicPlayerUtils mMusicPlayerUtils;
    
    String mTitle;
    private List<Item> mPlayList;
    private int mCurIndex;
    @Nullable  private Callback mCallback;

    
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

    public enum State{
        TRY_PLAYING,
        SUCCESS,
        ERROR,
        RESUME,
        PASUE,
        COMPLETE
    }
    @Override
    public void onCreate() {
        super.onCreate();

        mMusicPlayerUtils = new MusicPlayerUtils(this, new MusicPlayerUtils.IPlayerCallback() {
            @Override
            public void onTryPlaying(String id, String msg) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(State.TRY_PLAYING));
                if(mCallback !=null) {
                    mCallback.onTryPlaying(id, msg);
                }
            }

            @Override
            public void onSuccess(String id, String ms) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(State.SUCCESS));
                if(mCallback !=null) {
                    mCallback.onSuccess(id, ms);
                }
            }

            @Override
            public void onResume(String id, String ms) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(State.RESUME));
                if(mCallback !=null) {
                    mCallback.onResume(id, ms);
                }
            }

            @Override
            public void onPause(String id, String msg) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(State.PASUE));
                if(mCallback !=null) {
                    mCallback.onPause(id, msg);
                }
            }

            @Override
            public void onMusicInfo(String id, HashMap<String, Object> info) {
                if(mCallback !=null) {
                    mCallback.onMusicInfo(id, info);
                }
            }

            @Override
            public void onSeekBarPossionUpdate(String id, int total, int cur) {
                if(mCallback !=null) {
                    mCallback.onSeekBarPossionUpdate(id, total, cur);
                }
            }

            @Override
            public void onError(String id, String msg) {
                NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,getNotification(State.ERROR));
                if(mCallback !=null) {
                    mCallback.onError(id, msg);
                }
            }

            @Override
            public void onComplete(String id, String msg) {
                if(mCallback != null) {
                    mCallback.onComplete(id, msg);
                }
            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() == null) {
            return START_STICKY;
        }
        State state = State.TRY_PLAYING;
        if (intent.getAction().equals(Contracts.START)) {
            DLog.d("MusicForegroundService:: Start called");
            mPlayList = (List<Item>) intent.getSerializableExtra("LIST");
            mCurIndex = intent.getIntExtra("INDEX", 0);
            play();
        } else if (intent.getAction().equals(Contracts.PLAY_PAUSE)) {
            DLog.d("MusicForegroundService:: playPause called");
            if (mMusicPlayerUtils.isPlaying()) {
                mMusicPlayerUtils.pause();
                state = State.PASUE;
            } else if (mMusicPlayerUtils.isPaused()) {
                mMusicPlayerUtils.resume();
                state = State.RESUME;
            }
        } else if (intent.getAction().equals(Contracts.NEXT)) {
            DLog.d("MusicForegroundService:: Next called");
            mCurIndex++;
            play();
        } else if (intent.getAction().equals(Contracts.PREV)) {
            DLog.d("MusicForegroundService:: Prev called");
            mCurIndex--;
            play();
            //mMusicPlayerUtils.
        } else if (intent.getAction().equals(Contracts.QUIT)) {
            DLog.d("MusicForegroundService:: Quit called");
            mMusicPlayerUtils.stop();
            stopForeground(true);
            stopSelf();
        }
        startForeground(Contracts.NOTIFICATION_ID, getNotification(state));
        return START_STICKY;

    }

    private void play() {
        if(mPlayList == null|| mPlayList.size() <=0){
            return;
        }
        if(mCurIndex>=mPlayList.size()){
            mCurIndex=0;
        }
        if(mCurIndex < 0){
            mCurIndex = mPlayList.size() - 1;
        }

        Item item = mPlayList.get(mCurIndex);
        mTitle = item.getName();
        if(item != null) {
            mMusicPlayerUtils.play(item.getId(), item.getName(), item.getUrl());
        }
    }


    private Notification getNotification(State state){

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
            .setContentText(getStringForAction(state))
            .setSmallIcon(R.drawable.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH);

        if(mMusicPlayerUtils.isPlaying()){
            notification.addAction(R.drawable.ic_launcher, "Pause", pendingPlayPauseIntent);
        } else{
            notification.addAction(R.drawable.ic_launcher, "Play", pendingPlayPauseIntent);
        }


        notification.addAction(R.drawable.ic_launcher, "Next", pendingNextPauseIntent);
        notification.addAction(R.drawable.ic_launcher, "Quit", pendingQuitPauseIntent);

        return notification.build();
    }

    private CharSequence getStringForAction(State state) {
        switch (state){
            case ERROR:
                return "Error Occuried while playing "+mTitle;
            case PASUE:
                return "Paused "+mTitle;
            case RESUME:
                return "Playing "+mTitle;
            case SUCCESS:
                return "Playing "+mTitle;
            case COMPLETE:
                return "Completed Playing"+mTitle;
            case TRY_PLAYING:
                return "Trying to play "+mTitle;
            default:
                return "Unknown State";
        }
    }

    protected abstract Class getActivityClass();

    @Override
    public void onDestroy() {
        super.onDestroy();
        DLog.d("MusicForegroundService:: onDestroy called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        DLog.d("MusicForegroundService::onBind called");
        return binder;
    }



    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public void unsetCallback(Callback callback){
        mCallback = null;
    }

    public class LocalBinder extends Binder {
        public MusicForegroundService getService() {
            return MusicForegroundService.this;
        }
    }

    private IBinder binder = new LocalBinder();

}


