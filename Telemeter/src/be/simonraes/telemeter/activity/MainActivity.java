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
import be.simonraes.telemeter.domain.MessageToaster;
import be.simonraes.telemeter.domain.TelemeterLoader;
import be.simonraes.telemeter.fragment.PeriodFragment;
import be.simonraes.telemeter.fragment.StatusFragment;
import be.simonraes.telemeter.fragment.UsageFragment;
import be.simonraes.telemeter.model.TelemeterData;

import java.text.DecimalFormat;

/**
 * Main activity showing telemeter usages
 * Created by Simon Raes on 13/06/2014.
 */
public class MainActivity extends Activity implements  TelemeterLoader.TelemeterListener {

    private TelemeterData telemeterData;

    //todo: app logo resize (smaller)

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        //getActionBar().setBackgroundDrawable(new ColorDrawable(R.color.orange)); //doesn't work?
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F36535")));
        if(savedInstanceState!=null){
            telemeterData = savedInstanceState.getParcelable("telemeterData");
            refreshUI();
        } else {
            refreshData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("telemeterData", telemeterData);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshData() {

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

    private void goToSettings(){
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


    private void refreshUI(){
        FragmentManager manager = getFragmentManager();
        PeriodFragment periodFragment = (PeriodFragment) manager.findFragmentById(R.id.periodFragment);
        if(periodFragment!=null){
            periodFragment.setPeriod(telemeterData.getPeriod());
        }

        UsageFragment usageFragment = (UsageFragment) manager.findFragmentById(R.id.usageFragment);
        if(usageFragment!=null){
            usageFragment.setUsage(telemeterData.getUsage());
        }

        StatusFragment statusFragment = (StatusFragment) manager.findFragmentById(R.id.statusFragment);
        if(statusFragment!=null){
            statusFragment.setStatus(telemeterData.getStatus());
        }

    }

    @Override
    public void responseComplete(TelemeterData response) {
        if(response!=null){
            telemeterData = response;

        }
        if (response.getFault().getFaultString() != null && !response.getFault().getFaultString().equals("")) {
            //an error occurred
            MessageToaster.displayStatusToast(this, response);
        } else {
            if (response.getUsage() != null) {
                //got a FUP response

//                System.out.println("ticket info: "+response.getTicket().getTimestamp()+" and "+response.getTicket().getExpiryTimestamp());
//                System.out.println("total current usage (usage remaining): " + response.getUsage().getTotalUsage()+response.getUsage().getUnit()+" ("+response.getUsage().getMinUsageRemaining()+"-"+response.getUsage().getMaxUsageRemaining()+")");
//                System.out.println("period (from-till (today)): "+response.getPeriod().getFrom()+" - "+response.getPeriod().getTill()+" ("+response.getPeriod().getCurrentDay()+")");
//                System.out.println("Status: "+response.getStatus());
//                System.out.println("status description: "+response.getStatusDescription().getNl());
//                System.out.println("description status: "+response.getStatusDescription().getFr());

                refreshUI();
                Toast.makeText(this, "Done.", Toast.LENGTH_SHORT).show();

            } else {
                if (response.getVolume().getLimit() != null) {
                    //got a volume response
                    //todo: find someone with a volume telenet plan

                }
            }
        }
    }
}