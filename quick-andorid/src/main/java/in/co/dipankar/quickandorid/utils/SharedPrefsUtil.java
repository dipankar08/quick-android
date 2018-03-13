package in.co.dipankar.quickandorid.utils;

import android.content.Context;

/**
 *  Usees.
 *  SharedPrefUtils.Get?()
 */

public class SharedPrefsUtil {
    private static SharedPrefsUtil instance;
    private static final String PREFS_NAME = "default_preferences";
    private Context mContext;


    public synchronized static SharedPrefsUtil getInstance() {
        if (instance == null) {
            instance = new SharedPrefsUtil();
        }
        return instance;
    }

    public void init(Context context){
        mContext = context;
    }



    private SharedPrefsUtil() {
    }

    public String getString(String key, String defl) {
        if(mContext != null){
            return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .getString(key, defl);
        } else{
            DLog.e("SharedPrefsUtil is not yet initialized");
            return "";
        }
    }
    public void setString(String key, String val) {
        mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(key, val).apply();
    }
//
    public Boolean getBoolean(String key, Boolean defl) {
        if(mContext != null){
            return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .getBoolean(key, defl);
        } else{
            DLog.e("SharedPrefsUtil is not yet initialized");
            return false;
        }
    }
    public void setBoolean(String key, Boolean val) {
        mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(key, val).apply();
    }

    public int getInt(String key, int defl) {
        if(mContext != null){
            return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .getInt(key, defl);
        } else{
            DLog.e("SharedPrefsUtil is not yet initialized");
            return 0;
        }
    }
    public void setInt(String key, int val) {
        mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putInt(key, val).apply();
    }
}


