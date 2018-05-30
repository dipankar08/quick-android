package in.co.dipankar.quickandorid.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TelemetryUtils {

    private Context mContext;
    private Callback mCallback;
    private String mUrl;
    private boolean mDebug;
    OkHttpClient mHttpclient;

    private final static String s_session = getSaltString();
    private final String TAG = "DIPANKAR";

    public interface Callback {
        void onSuccess();
        void onFail();
    }

    public TelemetryUtils(Context context, String url, boolean isForce, Callback  callback){
        mContext = context;
        mUrl = url;
        mCallback = callback;
        mDebug = AndroidUtils.Get().isDebug();
        if (isForce) {
            mDebug = false;
        }
        initInternal();
    }

    public void markHit(String tag) {
        sendTelemetry(tag, new HashMap<String, String>());
    }

    public void sendTelemetry(String tag, Map<String, String> map) {
        if (mHttpclient == null || mUrl == null) {
            Log.d(TAG, "You must need to call setup() first.");
            return;
        }
        if (mDebug == true) {
            Log.d(TAG, "Ignore Sending telemetry data as debug build ");
            return;
        }
        JSONObject data = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                data.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        sendTelemtry(tag, data);
    }


    private void initInternal() {
        mHttpclient = new OkHttpClient();
        sendEventLaunch();
    }

    private void sendTelemtry(String tag, JSONObject json) {
        if (mDebug == true) {
            Log.d("DIPANKAR", "Skipping telemetry as debug build");
            return;
        }
        try {
            json.put("session", s_session);
            json.put("_cmd", "insert");
            json.put("_dotserializeinp", true);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
            Calendar cal = Calendar.getInstance();
            String strDate = sdf.format(cal.getTime());
            json.put("timestamp", strDate);
            json.put("tag", tag);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder().url(mUrl).post(body).build();
            mHttpclient
                    .newCall(request)
                    .enqueue(
                            new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d(TAG, "TelemetryUtils: onFailure " + e.toString());
                                    if(mCallback != null){
                                        mCallback.onFail();
                                    }
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.d(TAG, "TelemetryUtils: onResponse " + response.toString());
                                    if(mCallback != null){
                                        mCallback.onSuccess();
                                    }
                                }
                            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    private void sendEventLaunch() {
        if (mDebug == true) {
            Log.d(TAG, "Ignore Sending telemetry data as debug build ");
            return;
        }
        JSONObject data = new JSONObject();
        TimeZone tz = TimeZone.getDefault();
        try {
            data.put("_cmd", "insert");
            data.put("deviceinfo.version", System.getProperty("os.version")); // OS version
            data.put("deviceinfo.version", System.getProperty("os.version")); // OS version
            data.put("deviceinfo.sdk", android.os.Build.VERSION.SDK); // OS version
            data.put("deviceinfo.device", android.os.Build.DEVICE); // OS version
            data.put("deviceinfo.model", android.os.Build.MODEL); // OS version
            data.put("deviceinfo.product", android.os.Build.PRODUCT); // OS version
            if (mContext != null) {
                data.put(
                        "deviceinfo.deviceid",
                        Settings.Secure.getString(
                                mContext.getContentResolver(), Settings.Secure.ANDROID_ID)); // OS version
            }
            data.put(
                    "deviceinfo.timezone",
                    "TimeZone   "
                            + tz.getDisplayName(false, TimeZone.SHORT)
                            + " Timezon id :: "
                            + tz.getID());
            sendTelemtry("launch", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
