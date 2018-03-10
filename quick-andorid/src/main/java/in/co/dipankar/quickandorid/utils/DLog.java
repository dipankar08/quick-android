package in.co.dipankar.quickandorid.utils;


import android.util.Log;

public class DLog {
    private  static String TAG  = "DIPANKAR08";
    public static void d(String msg){
        Log.d(TAG,msg);
    }
    public static void e(String msg){
        Log.e(TAG,msg);
    }
    public static void i(String msg){
        Log.i(TAG,msg);
    }
}

