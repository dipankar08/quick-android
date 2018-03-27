package in.co.dipankar.quickandorid.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GateKeeperUtils {

    private static String TAG = "DIPANKAR";
    private static final String MY_PREFS_NAME = "GK_FILE";

    Map<String, String> configMap;
    private Context mContext;
    private INetwork mNetwork;

    // Constractuter
    public void GateKeeperUtils(Context context, INetwork network, String remoteUrl) {
        mContext = context;
        mNetwork = network;
        _downlaodAndSaveRemoteConfirg(remoteUrl);
    }

    // Call this function to verify if the this feature is enabled
    public boolean isFeatureEnabled(String gk_name, boolean defl) {
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString(gk_name, null);
        if (restoredText != null) {
            if (restoredText.toLowerCase().equals("true") || restoredText.toLowerCase().equals("1")) {
                return true;
            } else {
                int num = _getStringInt(restoredText);
                return _rollDie(num);
            }
        } else {
            return defl;
        }
    }

    //Call this function to do debug only feature
    public boolean isDebugOnlyFeature() {
        return AndroidUtils.isDebug();
    }

    // Get the remote String which can be a setting.
    public String getRemoteSetting(String gk_name, String defl) {
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString(gk_name, null);
        if (restoredText != null) {
            return restoredText;
        } else {
            return defl;
        }
    }

    private boolean _rollDie(int percentGiven) {
        Random rand = new Random();
        int roll = rand.nextInt(100);
        if (roll < percentGiven) {
            return true;
        } else {
            return false;
        }
    }

    private int _getStringInt(String s) {
        try {
            int num = Integer.parseInt(s);
            if (num >= 0 && num <= 100) {
                return num;
            } else {
                return 0;
            }
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void _downlaodAndSaveRemoteConfirg(String remoteUrl) {
        mNetwork
                .retrive(
                        remoteUrl,
                        Network.CacheControl.GET_LIVE_ONLY,
                        new Network.Callback() {
                            @Override
                            public void onSuccess(JSONObject jsonObject) {

                                JSONArray Jarray;
                                Map<String, String> map = new HashMap<>();
                                try {
                                    Jarray = jsonObject.getJSONArray("out");
                                    for (int i = 0; i < Jarray.length(); i++) {
                                        JSONObject object = Jarray.getJSONObject(i);
                                        if (object.has("gk_name") && object.has("gk_value")) {
                                            map.put(object.getString("gk_name"), object.getString("gk_value"));
                                        }
                                    }
                                    if (map.size() > 0) {
                                        configMap = map;
                                        SharedPreferences.Editor editor =
                                                mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        for (Map.Entry<String, String> entry : configMap.entrySet()) {
                                            editor.putString(entry.getKey().toString(), entry.getValue().toString());
                                        }
                                        editor.apply();
                                        Log.d(TAG, "GateKeep configuration loaded successfully!");
                                    }
                                } catch (JSONException e) {
                                    Log.d(TAG, "ERROR104" + e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String msg) {}
                        });
    }
}
