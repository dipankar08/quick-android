package in.co.dipankar.quickandorid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class RuntimePermissionUtils {
    public interface  CallBack{
        void onSuccess();
        void onFail();
    }
    private static RuntimePermissionUtils instance;
    private Context mContext;
    int mRequestCode =100;
    CallBack mCallBack;


    public synchronized static RuntimePermissionUtils getInstance() {
        if (instance == null) {
            instance = new RuntimePermissionUtils();
        }
        return instance;
    }

    public void init(Context context){
        mContext = context;
    }


    public void  askPermission(String[] pList, CallBack callBack ){
        mCallBack = callBack;
        if (Build.VERSION.SDK_INT >= 23) {
            boolean pass = true;
            for (String p : pList){
                if (mContext.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED){
                    pass = false;
                    break;
                }
            }
            if(pass){
                mCallBack.onSuccess();
            } else{
                ActivityCompat.requestPermissions((Activity)mContext, pList, mRequestCode);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            mCallBack.onSuccess();
        }
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        boolean pass = true;
        if  (requestCode == mRequestCode) {
                if (grantResults.length > 0){
                    for(int i =0; i< grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            pass = false;
                            DLog.d("Failed Because of "+permissions[i]);
                        }
                    }
                    if(pass){
                        mCallBack.onSuccess();
                    } else{
                        mCallBack.onFail();
                    }
                }
        }
        mRequestCode++;
    }

    private RuntimePermissionUtils() {
    }
}
