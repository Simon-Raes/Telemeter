package be.simonraes.telemeter.domain;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;
import be.simonraes.telemeter.database.TelemeterDataDataSource;
import be.simonraes.telemeter.model.TelemeterData;

import java.util.ArrayList;

/**
 * Controls the SOAP and XML async classes.
 * Created by Simon Raes on 27/06/2014.
 */
public class TelemeterLoader implements TelemeterSoap.TelemeterSoapResponse, TelenetXmlParser.TelenetXmlResponse {

    private static ArrayList<TelemeterLoaderResponse> listeners;

    public interface TelemeterLoaderResponse {
        public void telemeterDataUpdated();
    }

    private Context context;

    public TelemeterLoader(Context context, TelemeterLoaderResponse listener) {
        this.context = context;
    }

    public static void registerAsListener(TelemeterLoaderResponse listener) {
        if (listeners == null) {
            listeners = new ArrayList<TelemeterLoaderResponse>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void unregisterAsListener(TelemeterLoaderResponse listener) {
        if (listeners != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    /**
     * Sends a new request for data.
     */
    public void updateData() {
        String login = PreferenceManager.getDefaultSharedPreferences(context).getString("be.simonraes.telemeter.login", "");
        String password = PreferenceManager.getDefaultSharedPreferences(context).getString("be.simonraes.telemeter.password", "");

        TelemeterSoap telemeterSoap = new TelemeterSoap(context, this);
        telemeterSoap.execute(login, password);
    }

    /**
     * Received SOAP response.
     */
    @Override
    public void loadComplete(String response) {
        TelenetXmlParser telenetXmlParser = new TelenetXmlParser(this);
        telenetXmlParser.execute(response);
    }

    /**
     * Finished parsing XML response.
     */
    @Override
    public void parseComplete(TelemeterData response) {

        if (response.getFault().getFaultString() != null && !response.getFault().getFaultString().equals("")) {
            // Error detected
            MessageToaster.displayFaultToast(context, response);
        } else {
            TelemeterDataDataSource tdds = new TelemeterDataDataSource(context);
            tdds.saveTelemeterData(response);
            Toast.makeText(context, "Telemeter data updated.", Toast.LENGTH_LONG).show();

            if (listeners != null && listeners.size() > 0) {
                for (TelemeterLoaderResponse listener : listeners) {
                    listener.telemeterDataUpdated();
                }
            }
        }
    }
}
