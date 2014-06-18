package be.simonraes.telemeter.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import be.simonraes.telemeter.R;

/**
 * Created by Simon Raes on 18/06/2014.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        if (getPreferenceScreen() != null && getPreferenceScreen().getSharedPreferences() != null) {
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }
        updateSummaries();
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
            updateSummaries();
        }
    }

    //not used
    private void updateSummaries() {
        Preference prefLogin = findPreference("be.simonraes.telemeter.login");
        if (prefLogin != null && getPreferenceScreen() != null && getPreferenceScreen().getSharedPreferences() != null) {
            prefLogin.setSummary(getPreferenceScreen().getSharedPreferences().getString("be.simonraes.telemeter.login", "Your telenet login"));
        }
    }
}
