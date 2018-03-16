package in.co.dipankar.quickandorid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;


public class PhoneStateReceiver extends BroadcastReceiver {
    Context mContext;
    private Callback mCalback;
    private BroadcastReceiver mBroadcastManager;;


    public interface Callback {
        void onIncallingCall();
        void onOutgoingCall();
        void onOngoingCall();
        void onCallEnd();

        void onIncommingSMS();
        void onOutgoingSMS();

        void onChargerConnected();
        void onChargerDisconnected();
        void onLowBattery();
    }
    public PhoneStateReceiver(){

    }

    public PhoneStateReceiver(Context context, Callback networkChangeCallback){
        mContext = context;
        mCalback = networkChangeCallback;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // Call
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                mCalback.onCallEnd();
            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                mCalback.onIncallingCall();
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                mCalback.onOngoingCall();
            }
        } else if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            mCalback.onOutgoingCall();
        }

        //Batery info
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if(isCharging || usbCharge || acCharge){
            mCalback.onChargerConnected();
        } else{
            mCalback.onChargerDisconnected();
        }
    }

    public void onResume(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        mContext.registerReceiver(this, intentFilter);
    }

    public void onPause(){
        mContext.unregisterReceiver(this);
    }
}
