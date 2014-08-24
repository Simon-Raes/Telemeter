package be.simonraes.telemeter.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import be.simonraes.telemeter.R;
import be.simonraes.telemeter.service.AlarmManagerBroadcastReceiver;

/**
 * Created by Simon Raes on 18/06/2014.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AlarmManagerBroadcastReceiver alarm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        if (getPreferenceScreen() != null && getPreferenceScreen().getSharedPreferences() != null) {
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }
        updateLoginSummary();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPreferenceScreen() != null && getPreferenceScreen().getSharedPreferences() != null) {
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getPreferenceScreen() != null && getPreferenceScreen().getSharedPreferences() != null) {
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("be.simonraes.telemeter.login")) {
            updateLoginSummary();
        } else if (key.equals("be.simonraes.telemeter.autoupdate")) {
            toggleService();
        } else if (key.equals("be.simonraes.telemeter.autoupdatetime")) {
            if (Integer.parseInt(getPreferenceScreen().getSharedPreferences().getString("be.simonraes.telemeter.autoupdate", "Your telenet login")) < 1) {
                getPreferenceScreen().getSharedPreferences().edit().putString("be.simonraes.telemeter.autoupdate", "1").commit();
            }
        }
    }

    private void updateLoginSummary() {
        Preference prefLogin = findPreference("be.simonraes.telemeter.login");
        if (prefLogin != null && getPreferenceScreen() != null && getPreferenceScreen().getSharedPreferences() != null) {
            prefLogin.setSummary(getPreferenceScreen().getSharedPreferences().getString("be.simonraes.telemeter.login", "Your telenet login"));
        }
    }

    private void toggleService() {
        if (alarm == null) {
            alarm = new AlarmManagerBroadcastReceiver();
        }

        if (getPreferenceScreen().getSharedPreferences().getBoolean("be.simonraes.telemeter.autoupdate", false)) {
            int updateTimer = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("be.simonraes.telemeter.autoupdatetimer","24"));
            alarm.setAlarm(getActivity(), updateTimer);
        } else {
            alarm.cancelAlarm(getActivity());
        }
    }
}
