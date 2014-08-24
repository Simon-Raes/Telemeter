package be.simonraes.telemeter.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;
import be.simonraes.telemeter.domain.TelemeterLoader;

/**
 * Created by Simon Raes on 23/08/2014.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        // Update telemeter data.
        Toast.makeText(context, "Updating Telemeter data", Toast.LENGTH_SHORT).show();
        TelemeterLoader loader = new TelemeterLoader(context, null);
        loader.updateData();

        // Todo: cleanup. Delete all entries of a day except the last one.

        //Release the lock
        wl.release();
    }

    public void setAlarm(Context context, int updateEveryHours) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        long updateEveryMillis = updateEveryHours * 60 * 60 * 1000;

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), updateEveryMillis, pendingIntent);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
