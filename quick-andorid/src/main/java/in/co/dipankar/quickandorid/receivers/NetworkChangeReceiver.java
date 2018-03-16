package in.co.dipankar.quickandorid.receivers;


        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.wifi.WifiInfo;
        import android.net.wifi.WifiManager;

public class NetworkChangeReceiver extends BroadcastReceiver {
    Context mContext;
    private Callback mNetworkChangeCallback;
    private BroadcastReceiver mBroadcastManager;;


    public interface Callback {
        void onNetworkGone();
        void onNetworkAvailable();
        void onNetworkAvailableWifi();
        void onNetworkAvailableMobileData();
    }
    public NetworkChangeReceiver(){

    }

    public NetworkChangeReceiver(Context context, Callback networkChangeCallback){
        mContext = context;
        mNetworkChangeCallback = networkChangeCallback;
        //mBroadcastManager =    BroadcastReceiver.(mContext);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (mNetworkChangeCallback != null) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(info != null && info.isConnected()) {
                mNetworkChangeCallback.onNetworkAvailable();
                if(info.getTypeName().equals("MOBILE")){
                    mNetworkChangeCallback.onNetworkAvailableMobileData();
                } else{
                    mNetworkChangeCallback.onNetworkAvailableWifi();
                }
            } else{
                mNetworkChangeCallback.onNetworkGone();
            }
        }
    }

    public void onResume(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(this, intentFilter);
    }
    
    public void onPause(){
        mContext.unregisterReceiver(this);
    }
}
