package in.co.dipankar.quickandorid.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This provides a correct music player utils for all func
 */
public class MusicPlayerUtils {

    public interface IPlayerCallback {
        void onTryPlaying(String id, String msg);
        void onSuccess(String id, String ms);
        void onResume(String id, String ms);
        void onPause(String id, String ms);
        void onMusicInfo(String id, HashMap<String, Object> info);
        void onSeekBarPossionUpdate(String id, int total, int cur);
        void onError(String id, String msg);
        void onComplete(String id, String ms);
    }

    private MediaPlayer mPlayer;
    private Context mContext;
    private IPlayerCallback mPlayerCallback;
    private String id;
    private String mUrl;
    private String mTitle;
    private int mTotalDuration;
    private Handler mHandler = new Handler();;
    private boolean mIsPaused = false;


    public MusicPlayerUtils(Context context, IPlayerCallback playerCallback) {
        mPlayerCallback = playerCallback;
        mContext = context;
        init();
    }

    private void init() {
        if (mPlayer == null) {
            DLog.d( "MusicPlayerUtils::init called");
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setScreenOnWhilePlaying(true);
            mPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
            mPlayer.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer player) {
                        if(mPlayer == null){
                            return;
                        }
                        mPlayer.start();
                        mTotalDuration = mPlayer.getDuration();
                        onSuccess(mTitle);
                        if(mPlayer != null) {
                            onMusicInfo(
                                    new HashMap<String, Object>() {
                                        {
                                            put("CurrentPosition", mPlayer.getCurrentPosition());
                                            put("Duration", mPlayer.getDuration());
                                            put("count", "1");
                                        }
                                    });
                        }
                        // schedule the refreshUI timer
                        mHandler.postDelayed(mUpdateTimeTask, 1000);
                    }
                });
            mPlayer.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        onComplete(mTitle);
                    }
                });
            mPlayer.setOnErrorListener(
                new MediaPlayer.OnErrorListener() {
                    public boolean onError(MediaPlayer mp, int what, final int extra) {
                        onErrorInternal("Not able to play this right now.");
                        return true;
                    }
                });
        } else{
            DLog.d( "MusicPlayerUtils::skip init as it already exist");
        }
    }


    // public functions.
    public void stop() {
        DLog.d( "MusicPlayerUtils::stop called");
        mIsPaused = false;
        if (mPlayer != null) {
            mPlayer.stop();
            if(mPlayer != null) {
                mPlayer.reset();
            }
            mPlayer = null;
        }
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    public void pause() {
        DLog.d( "MusicPlayerUtils::pause called");
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayerCallback.onPause(id, mTitle);
            mIsPaused = true;
        }
    }

    public void resume() {
        DLog.d( "MusicPlayerUtils::resume called");
        if (mPlayer != null && mPlayer.isPlaying() == false) {
            mPlayer.start();
            mPlayerCallback.onResume(id, mTitle);
            mIsPaused = false;
        }
    }

    public void restart() {
        DLog.d( "MusicPlayerUtils::restart called");
        if (mUrl != null) {
            play(id, mTitle, mUrl);
        }
        mIsPaused = false;
    }


    public void mute() {
        DLog.d( "MusicPlayerUtils::mute called");
    }


    public void unmute() {
        DLog.d( "MusicPlayerUtils::unmute called");
    }


    public boolean isPaused() {
        return mIsPaused;
    }

    public void seekTo(int progress) {
        DLog.d( "MusicPlayerUtils::seekTo called");
        int msec = (int) (mTotalDuration * (progress / 100.0));
        if (mPlayer != null) {
            mPlayer.seekTo(msec);
        }
    }


    public void play(final String id, final String title, final String url) {
        DLog.d( "MusicPlayerUtils::play called");
        mIsPaused = false;

        if (url == null) {
            onErrorInternal("Invalid URL passed");
            return;
        }
        this.id = id;
        mUrl = url;
        mTitle = title;
        onTryPlaying(title);
        stop();
        init();
        try {
            playInternal(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void  playInternal( final String url) throws IOException {
        if(mPlayer == null){
            onErrorInternal("Not able to play, please retry.");
            return;
        }
        try {
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();
        } catch (final Exception e) {
            DLog.d("Not able to play because of:" + e.getMessage());
            onErrorInternal("Not able to play this right now.");
            stop();
            e.printStackTrace();
            return;
        }
    }

    private void onErrorInternal(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPlayerCallback.onError(id, msg);
            }
        });
    }

    private void onTryPlaying(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPlayerCallback.onTryPlaying(id, msg);
            }
        });
    }

    private void onSuccess(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPlayerCallback.onSuccess(id, msg);
            }
        });
    }

    private void onComplete(final String msg) {
        new Handler(Looper.getMainLooper()).post( new Runnable() {
            @Override
            public void run() {
                mPlayerCallback.onComplete(id, msg);
            }
        });
    }

    private void onMusicInfo(final HashMap<String, Object> info) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPlayerCallback.onMusicInfo(id, info);
            }
        });
    }

    private void onSeekBarPossionUpdate(final int total, final int cur) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPlayerCallback.onSeekBarPossionUpdate(id, total, cur);
            }
        });
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mPlayer != null && mPlayer.isPlaying()) {
                int totalDuration = mPlayer.getDuration();
                int currentDuration = mPlayer.getCurrentPosition();
                onSeekBarPossionUpdate(totalDuration, currentDuration);
                mHandler.postDelayed(this, 1000);
            }
        }
    };

}
