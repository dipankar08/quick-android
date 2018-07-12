package in.co.dipankar.quickandorid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import in.co.dipankar.quickandorid.utils.DLog;
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DLog.e( "AlarmUtils: Received with id:"+intent.getStringExtra("uuid"));

        // Handle the intent..
    }
}