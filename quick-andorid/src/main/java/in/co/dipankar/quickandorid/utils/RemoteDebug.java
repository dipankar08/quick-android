package in.co.dipankar.quickandorid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RemoteDebug {
    private HttpdUtils mHttpdUtils;
    private Context mContext;

    public RemoteDebug(Context context){
        mContext = context;
        mHttpdUtils = new HttpdUtils(8081, new HttpdUtils.Callback() {
            @Override
            public String Handle(String method, String key, Map<String, String> param) {
                String data = param.get("data");
                switch (key){
                    case "/setPref":
                        return setPerf(data);
                    case "/getPref":
                        return getPerf(data);
                    default:
                        return "Invalid command Sent!\n";

                }
            }
            @Override
            public void onSuccess(String msg) {}

            @Override
            public void onError(String msg) {}
        });
    }
    private String setPerf(String data){
        if(data == null || data.length() < 1){
            return "Please pass some args\n";
        }
        List<String> tokens = Arrays.asList(data.split(" "));
        if(tokens.size() < 3){
            return "Less args sent";
        }
        SharedPreferences.Editor editor = mContext.getSharedPreferences("test", MODE_PRIVATE).edit();
        switch(tokens.get(0)){
            case "S":
                editor.putString(tokens.get(1), tokens.get(2));
                editor.apply();
                return "Set Successfully";
            case "I":
                editor.putInt(tokens.get(1), Integer.parseInt(tokens.get(2)));
                editor.apply();
                return "Set Successfully";

        }
        return "Not able to set the shared pref";
    }
    private String getPerf(String data){
        if(data == null || data.length() < 1){
            return "Please send some args\n";
        }
        List<String> tokens = Arrays.asList(data.split(" "));
        if(tokens.size() < 2){
            return "Less args sent";
        }
        SharedPreferences prefs = mContext.getSharedPreferences("test", MODE_PRIVATE);

        switch(tokens.get(0)){
            case "S":
                return prefs.getString(tokens.get(1),"null");
            case "I":
                int x =  prefs.getInt(tokens.get(1),-1);
                return x+"";

        }
        return "Not able to get the shared pref";
    }
}
