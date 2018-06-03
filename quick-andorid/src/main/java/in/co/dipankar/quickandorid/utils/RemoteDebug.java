package in.co.dipankar.quickandorid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import static android.content.Context.MODE_PRIVATE;

public class RemoteDebug {
    private HttpdUtils mHttpdUtils;
    private Context mContext;
    private Provider mProvider;

    public  interface Provider{
        int getViewId(String id);
    }

    public RemoteDebug(Context context, Provider r){
        mContext = context;
        mProvider = r;
        mHttpdUtils = new HttpdUtils(8081, new HttpdUtils.Callback() {
            @Override
            public String Handle(String method, String key, Map<String, String> param) {
                String data = param.get("data");
                switch (key){
                    case "/setPref":
                        return setPerf(data);
                    case "/getPref":
                        return getPerf(data);
                    case "/isVisible":
                        return checkVisibility(data);
                    case "/isExist":
                        return isExist(data);
                    case "/doClick":
                        return performActionOnView(data,"doClick");
                    case "/doLongClick":
                        return performActionOnView(data,"doLongClick");
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

    private String checkVisibility(String data){
        if(data == null || data.length() < 1){
            return "Please pass some args\n";
        }
        List<String> tokens = Arrays.asList(data.split(" "));
        if(tokens.size() < 1){
            return "Less args sent";
        }

        View v = getViewByIdStr(tokens.get(0));
        if(v == null){
            return "VIEW_NOT_FOUND";
        }
        if(v.getVisibility() == View.VISIBLE){
            return "True";
        } else{
            return "False";
        }
    }

    private String performActionOnView(String data, String type){
        if(data == null || data.length() < 1){
            return "Please pass some args\n";
        }
        List<String> tokens = Arrays.asList(data.split(" "));
        if(tokens.size() < 1){
            return "Less args sent";
        }

        final View v = getViewByIdStr(tokens.get(0));
        if(v == null){
            return "VIEW_NOT_FOUND";
        }

        if(v.getVisibility() != View.VISIBLE){
            return "VIEW_NOT_VISIBLE";
        }

        switch (type){
            case "doClick":
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.performClick();
                    }
                });

                break;
            case "doLongClick":
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.performLongClick();
                    }
                });
                break;
                default:
                    return "Invalid Do Command";
        }
        return "DONE";
    }

    private View getViewByIdStr(String idStr) {
        int id = mContext.getResources().
                getIdentifier(idStr, "id", mContext.getPackageName());
        //int id = getResId(idStr, (R)mR.id.class);
        //int id = mProvider.getViewId(idStr);
        if (id == -1) {
            return null;
        }
        return ((Activity) mContext).findViewById(id);
    }

    private String isExist(String data){
        if(data == null || data.length() < 1){
            return "Please pass some args\n";
        }
        List<String> tokens = Arrays.asList(data.split(" "));
        if(tokens.size() < 1){
            return "Less args sent";
        }

        View v = getViewByIdStr(tokens.get(0));
        if(v == null){
            return "False";
        } else{
            return "True";
        }
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
