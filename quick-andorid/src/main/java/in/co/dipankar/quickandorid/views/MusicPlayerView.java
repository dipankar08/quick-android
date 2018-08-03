package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import in.co.dipankar.quickandorid.R;
import in.co.dipankar.quickandorid.utils.Player;

public class MusicPlayerView extends LinearLayout {
    private Context mContext;
    private @Nullable  Player mPlayer;

    private ImageButton mPlayPause;
    private ImageButton mVol;
    private SeekBar mSeekBar;
    private TextView duration;
    private boolean isVolOn = true;
    public MusicPlayerView(Context context) {
        this(context, null);
    }

    public MusicPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initPlayer();
    }

    private void initView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_media_player, this);
        mPlayPause = findViewById(R.id.play_pause);
        mVol = findViewById(R.id.vol);
        mSeekBar = findViewById(R.id.seekbar);
        duration = findViewById(R.id.duration);

        mPlayPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer != null){
                    if(mPlayer.isPlaying()){
                        mPlayer.pause();
                    } else if( mPlayer.isPaused()){
                        mPlayer.resume();
                    } else{
                        mPlayer.restart();
                    }
                } else{

                }
            }
        });
        final AudioManager am =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mVol.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isVolOn) {
                    am.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                            0);
                    mVol.setImageResource(R.drawable.ic_vol_on);
                } else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                    } else {
                        am.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    }
                    mVol.setImageResource(R.drawable.ic_vol_off);
                }
                isVolOn = !isVolOn;
            }
        });

        mSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mPlayer.seekTo(progress);
                        }
                    }
                });
    }

    private void initPlayer() {
        mPlayer = new Player(getContext(), new Player.IPlayerCallback() {
            @Override
            public void onTryPlaying(String id, String msg) {
                mPlayPause.setImageResource(R.drawable.ic_pause_grey_24);
                mPlayPause.setEnabled(false);
            }

            @Override
            public void onSuccess(String id, String ms) {
                mPlayPause.setImageResource(R.drawable.ic_pause_grey_24);
                mPlayPause.setEnabled(true);
            }

            @Override
            public void onResume(String id, String ms) {
                mPlayPause.setImageResource(R.drawable.ic_pause_grey_24);
                mPlayPause.setEnabled(true);
            }

            @Override
            public void onPause(String id, String ms) {
                mPlayPause.setImageResource(R.drawable.ic_play_grey_24);
                mPlayPause.setEnabled(true);
            }

            @Override
            public void onMusicInfo(String id, HashMap<String, Object> info) {

            }

            @Override
            public void onSeekBarPossionUpdate(String id, int total, int cur) {
                duration.setText(MiliToStr(cur)+" / " + MiliToStr(total));
                mSeekBar.setProgress((int) ((float) cur / total * 100));
            }

            @Override
            public void onError(String id, String msg) {
                mPlayPause.setImageResource(R.drawable.ic_play_grey_24);
                mPlayPause.setEnabled(true);
            }

            @Override
            public void onComplete(String id, String ms) {
                mPlayPause.setImageResource(R.drawable.ic_play_grey_24);
                mPlayPause.setEnabled(true);
            }
        });
    }

    public void play(String id, String title, String url ){
        mPlayer.play(id, title, url);
    }

    private String MiliToStr(int millis) {
        return String.format(
                "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}