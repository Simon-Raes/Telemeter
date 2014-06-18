package be.simonraes.telemeter.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import be.simonraes.telemeter.R;
import be.simonraes.telemeter.code.TelemeterLoader;
import be.simonraes.telemeter.code.TelenetXmlParser;
import be.simonraes.telemeter.fragment.PeriodFragment;
import be.simonraes.telemeter.fragment.UsageFragment;
import be.simonraes.telemeter.model.TelemeterData;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class MainActivity extends Activity implements TelemeterLoader.TelemeterLoaderResponse, TelenetXmlParser.TelenetXmlResponse {

    private TelemeterData telemeterData;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

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
            TelemeterLoader telemeterLoader = new TelemeterLoader(this, this);
            Toast.makeText(this, "Refreshing data...", Toast.LENGTH_SHORT).show();
            telemeterLoader.execute(login, password);
        } else {
            Toast.makeText(this, "Please set your login and password.", Toast.LENGTH_LONG).show();
            goToSettings();
        }
    }

    private void goToSettings(){
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    /*Finished downloading SOAP response*/
    @Override
    public void loadComplete(String response) {
        System.out.println("response envelope= " + response);
        TelenetXmlParser telenetXmlParser = new TelenetXmlParser(this);
        telenetXmlParser.execute(response);
    }

    /*Finished parsing XML.*/
    @Override
    public void parseComplete(TelemeterData response) {
        if(response!=null){
            telemeterData = response;
        }
        if (response.getFault().getFaultString() != null && !response.getFault().getFaultString().equals("")) {
            //an error occurred
            System.out.println(response.getFault().getDetail().getCode());
            //todo: check error description to determine type of error
            if (response.getFault().getFaultString().contains("Incorrect Login or Password specified")) {
                Toast.makeText(this, "Incorrect login/password combination.", Toast.LENGTH_SHORT).show();
            } else if (response.getFault().getFaultString().contains("Please try accessing data after expiry time")) {
                Toast.makeText(this, "Refreshed too often, please try again in a couple of minutes.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unknown error. Please try again later.", Toast.LENGTH_SHORT).show();
            }


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
                Toast.makeText(this, "Data up-to-date.", Toast.LENGTH_SHORT).show();

            } else {
                if (response.getVolume().getLimit() != null) {
                    //got a volume response

                }
            }
        }
    }

    private void refreshUI(){
        FragmentManager manager = getFragmentManager();
        PeriodFragment periodFragment = (PeriodFragment) manager.findFragmentById(R.id.periodFragment);
        periodFragment.setPeriod(telemeterData.getPeriod());

        UsageFragment usageFragment = (UsageFragment) manager.findFragmentById(R.id.usageFragment);
        usageFragment.setUsage(telemeterData.getUsage());
    }
}