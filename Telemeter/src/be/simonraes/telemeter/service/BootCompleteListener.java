package be.simonraes.telemeter.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Detects start-up of the device and starts the service if needed.
 * Created by Simon Raes on 23/08/2014.
 */
public class BootCompleteListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent i) {
        restartService(context);
    }

    static void restartService(Context context) {
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("be.simonraes.telemeter.autoupdate", false)){
            AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
            int updateTimer = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("be.simonraes.telemeter.autoupdatetimer","24"));
            alarm.setAlarm(context, updateTimer);
        } else {
            AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
            alarm.cancelAlarm(context);
        }
    }
}
