package in.co.dipankar.quickandorid.utils;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.IOException;


public class AudioRecorderUtil implements IAudioRecorder{

    private static final String FOLDER_PATH = Environment.getExternalStorageDirectory() + "/record/";
    private static final int MAX_LENGTH = 3 * 60 * 60 * 1000; // 3 hrs


    public static final int AUDIO_CHANNEL_SINGLE = 1;
    public static final int AUDIO_CHANNEL_MORE = 2;
    private MediaRecorder mMediaRecorder;

    private String mFilePath;
    private long mStartTime;

    private boolean isRecording = false;

    private IAudioRecorder.Callback mCallback;
    private Handler mHandler = new Handler();

    private int DEFAULT_RATE  = 300;
    private int DEFAULT_CHANNEL  = 1;


    public AudioRecorderUtil(Context context){

    }

    private void init() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setMaxDuration(MAX_LENGTH);
        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                if(mCallback != null){
                    mCallback.onError("Some internal error on MediaRecorder");
                }
            }
        });
        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {

            }
        });

        createFile();
    }

    private File createFile(){
        File path = new File(FOLDER_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        return path;
    }

    @Override
    public void startRecord(String url, Callback callback) {

    }

    @Override
    public void startRecord(Callback callback){
        mCallback = callback;
        try {
            startRecordInternal();
        } catch (IOException e) {
            if(mCallback != null){
                mCallback.onError("Some internal error");
            }
        }
    }

    @Override
    public void stopRecord(){
        if(isRecording){
            isRecording = false;
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            if(mCallback != null){
                mCallback.onStop(mFilePath);
            }
        } else{
            if(mCallback != null){
                mCallback.onError("Please start the recording first");
            }
        }
    }

    @Override
    public void pauseRecord(){
        if(mMediaRecorder != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMediaRecorder.pause();
            }
        }
    }

    @Override
    public void resumeRecord(){
        if(mMediaRecorder != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMediaRecorder.resume();
            }
        }
    }

    private void startRecordInternal() throws IOException {

        if (mMediaRecorder == null) {
            init();
        } else{
            cancelRecord();
        }

        if (!isRecording) {
            try {
                mFilePath = FOLDER_PATH+"BENGALI_FM_" + System.currentTimeMillis() + ".mp4";
                mMediaRecorder.setOutputFile(mFilePath);
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                mStartTime = System.currentTimeMillis();
                isRecording = true;
                if(mCallback != null){
                    mCallback.onStart();
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (mCallback != null) {
                    mCallback.onError("Error while recoring ");
                }
                stopRecord();
                deleteFile();
            }
        } else{
            if(mCallback != null){
                mCallback.onError("Recoding has already started.");
            }
        }

    }
    @Override
    public boolean isRecording(){
        return isRecording;
    }

    @Override
    public void cancelRecord(){
        if(isRecording) {
            stopRecord();
            deleteFile();
        }
    }
    private void deleteFile() {
        File file = new File(mFilePath);
        if (file.exists())
            file.delete();
        mFilePath = "";
    }
}
