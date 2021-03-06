package be.simonraes.telemeter.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import be.simonraes.telemeter.R;
import be.simonraes.telemeter.service.AutoUpdater;

/**
 * Created by Simon Raes on 18/06/2014.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AutoUpdater alarm;

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
            toggleAutoUpdater();
        } else if (key.equals("be.simonraes.telemeter.autoupdateinterval")) {
            if (Integer.parseInt(getPreferenceScreen().getSharedPreferences().getString("be.simonraes.telemeter.autoupdateinterval", "24")) < 1) {

                getPreferenceScreen().getSharedPreferences().edit().putString("be.simonraes.telemeter.autoupdateinterval", "1").commit();
            }

                String summary = getPreferenceScreen().getSharedPreferences().getString("be.simonraes.telemeter.autoupdateinterval", "24")+ " hours";
                findPreference("be.simonraes.telemeter.autoupdateinterval").setSummary(summary);
                restartAutoUpdater();

        }
    }

    private void updateLoginSummary() {
        Preference prefLogin = findPreference("be.simonraes.telemeter.login");
        if (prefLogin != null && getPreferenceScreen() != null && getPreferenceScreen().getSharedPreferences() != null) {
            prefLogin.setSummary(getPreferenceScreen().getSharedPreferences().getString("be.simonraes.telemeter.login", "Your telenet login"));
        }
    }

    private void toggleAutoUpdater() {
        if (alarm == null) {
            alarm = new AutoUpdater();
        }

        if (getPreferenceScreen().getSharedPreferences().getBoolean("be.simonraes.telemeter.autoupdate", false)) {
            alarm.setAlarm(getActivity(), getUpdateInterval());
        } else {
            alarm.cancelAlarm(getActivity());
        }
    }

    private void restartAutoUpdater(){
        if(alarm==null){
            alarm = new AutoUpdater();
            alarm.cancelAlarm(getActivity());
            alarm.setAlarm(getActivity(),getUpdateInterval());
        }
    }

    private int getUpdateInterval(){
        int updateInterval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("be.simonraes.telemeter.autoupdateinterval","24"));
        return updateInterval;
    }
}
