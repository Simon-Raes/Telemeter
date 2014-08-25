package be.simonraes.telemeter.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import be.simonraes.telemeter.R;
import be.simonraes.telemeter.database.TelemeterDataDataSource;
import be.simonraes.telemeter.domain.MessageToaster;
import be.simonraes.telemeter.domain.TelemeterLoader;
import be.simonraes.telemeter.fragment.PeriodFragment;
import be.simonraes.telemeter.fragment.StatusFragment;
import be.simonraes.telemeter.fragment.UsageFragment;
import be.simonraes.telemeter.model.TelemeterData;

/**
 * Main activity showing telemeter usage.
 * Created by Simon Raes on 13/06/2014.
 */
public class MainActivity extends Activity implements TelemeterLoader.TelemeterLoaderResponse {

    //todo: app logo resize (smaller sizes needed)

    private TelemeterData data;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        if (getActionBar() != null) {
            getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F36535")));  //R.color.orange doesn't seem to work?
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Display the latest data (in case the widget downloaded more recent info).
        TelemeterDataDataSource tdds = new TelemeterDataDataSource(this);
        data = tdds.getLatestTelemeterData();

        if (data.getPeriod().getFrom() != null) {
            refreshUI();
        }

        TelemeterLoader.registerAsListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TelemeterLoader.unregisterAsListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSettings:
                goToSettings();
                return true;
            case R.id.btnRefresh:
                refreshData();
                return true;
            case R.id.btnDatabaseTest:
                TelemeterDataDataSource ds = new TelemeterDataDataSource(this);
                TelemeterData data = ds.getLatestTelemeterData();
                System.out.println("Data was downloaded at " + data.getTicket().getTimestamp());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshData() {
        System.out.println("refreshing");
        String login = PreferenceManager.getDefaultSharedPreferences(this).getString("be.simonraes.telemeter.login", "");
        String password = PreferenceManager.getDefaultSharedPreferences(this).getString("be.simonraes.telemeter.password", "");
        if (!login.equals("") && !password.equals("")) {
            Toast.makeText(this, "Refreshing data...", Toast.LENGTH_SHORT).show();
            TelemeterLoader telemeterLoader = new TelemeterLoader(this, this);
            telemeterLoader.updateData();
        } else {
            Toast.makeText(this, "Please set your login and password.", Toast.LENGTH_LONG).show();
            goToSettings();
        }
    }

    private void goToSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void refreshUI() {
        FragmentManager manager = getFragmentManager();

        PeriodFragment periodFragment = (PeriodFragment) manager.findFragmentById(R.id.periodFragment);
        if (periodFragment != null) {
            periodFragment.setPeriod(data.getPeriod());
        }

        UsageFragment usageFragment = (UsageFragment) manager.findFragmentById(R.id.usageFragment);
        if (usageFragment != null) {
            usageFragment.setUsage(data.getUsage());
        }

        StatusFragment statusFragment = (StatusFragment) manager.findFragmentById(R.id.statusFragment);
        if (statusFragment != null) {
            statusFragment.setStatus(data.getStatus());
        }
    }

    @Override
    public void telemeterDataUpdated() {

        if (data != null) {
            if (data.getFault().getFaultString() != null && !data.getFault().getFaultString().equals("")) {
                // An error occurred
                MessageToaster.displayFaultToast(this, data);
            } else {
                loadLatestDatabaseData();
                refreshUI();
            }
        }
    }

    private void loadLatestDatabaseData() {
        TelemeterDataDataSource tdds = new TelemeterDataDataSource(this);
        data = tdds.getLatestTelemeterData();
    }
}