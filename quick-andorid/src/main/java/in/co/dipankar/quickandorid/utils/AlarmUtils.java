package in.co.dipankar.quickandorid.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import in.co.dipankar.quickandorid.receivers.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;

public class AlarmUtils {

    private Context mContext;

    private AlarmManager alarmManager;
    private Map<String, PendingIntent> pendingIntentMap;


    public interface  Callback{
        public void onSetAlarm(String id);
        public void onCancelAlarm(String id);
    }

    public enum INTERVAL{
        NONE,
        DAILY;
    };

    private Callback mCallback;
    public AlarmUtils(Context context,Callback callback){
        mContext = context;
        mCallback = callback;
        pendingIntentMap = new HashMap<>();
        alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
    }

    public String setAlarm( int hours, int min , Intent myIntent){
        return setAlarm(-1, -1, hours, min, myIntent, INTERVAL.NONE);
    }

    public String setAlarm(int month, int date, int hours, int min, Intent myIntent, INTERVAL interval){
        if(myIntent == null){
            myIntent = new Intent(mContext, AlarmReceiver.class);
        }

        String id = UUID.randomUUID().toString();
        final Calendar calendar = Calendar.getInstance();

        if(month != -1){
            calendar.set(Calendar.MONTH , month);
        }
        if(date != -1){
            calendar.set(Calendar.DATE , date);
        }

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, min);
        myIntent.putExtra("uuid",id);
        PendingIntent pending_intent = PendingIntent.getBroadcast(mContext, 324, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        switch (interval){
            case DAILY:
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pending_intent);
                break;
            case NONE:
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
                break;
        }

        pendingIntentMap.put(id, pending_intent);
        if(mCallback != null){
            mCallback.onSetAlarm(id);
        }
        return id;
    }

    public void cancelAlarm(String id){
        PendingIntent p = pendingIntentMap.get(id);
        if(p == null){
            DLog.d("Alarm with this id not found");
            return;
        }
        alarmManager.cancel(p);
        if(mCallback != null){
            mCallback.onCancelAlarm(id);
        }
    }
}
