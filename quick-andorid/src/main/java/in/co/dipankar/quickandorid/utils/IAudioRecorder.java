package in.co.dipankar.quickandorid.utils;

/**
 * Created by dip on 5/14/18.
 */

public interface IAudioRecorder {
    void startRecord(String url, Callback callback);
    void startRecord(Callback callback);

    void stopRecord();

    void pauseRecord();

    void resumeRecord();

    boolean isRecording();

    void cancelRecord();

    public interface Callback{
        void onStart();
        void onError(String msg);
        void onStop(String path);
    }
}
