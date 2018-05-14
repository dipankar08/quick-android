package in.co.dipankar.quickandorid.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;

/**
 * Created by dip on 5/14/18.
 */

public class M3U8RecordUtils implements IAudioRecorder {

    private static final String FOLDER_PATH = Environment.getExternalStorageDirectory() + "/record/";
    private static final int MAX_LENGTH = 3 * 60 * 60 * 1000; // 3 hrs

    @Override
    public void startRecord(String url, Callback callback) {

        try {
            startRecordFromUrlInternal(url, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void startRecordFromUrlInternal(String urlstr, Callback callback) throws IOException {

    }

    private File createFile(){
        File path = new File(FOLDER_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(FOLDER_PATH+"BENGALI_FM_" + System.currentTimeMillis() + ".mp4");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    @Override
    public void startRecord(Callback callback) {

    }

    @Override
    public void stopRecord() {

    }

    @Override
    public void pauseRecord() {

    }

    @Override
    public void resumeRecord() {

    }

    @Override
    public boolean isRecording() {
        return false;
    }

    @Override
    public void cancelRecord() {

    }
}
