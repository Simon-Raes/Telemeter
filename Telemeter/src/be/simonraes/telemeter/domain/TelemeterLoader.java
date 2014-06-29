package be.simonraes.telemeter.domain;

import android.content.Context;
import android.preference.PreferenceManager;
import be.simonraes.telemeter.model.TelemeterData;

/**
 * Created by Simon Raes on 27/06/2014.
 */
public class TelemeterLoader implements TelemeterSoap.TelemeterLoaderResponse, TelenetXmlParser.TelenetXmlResponse {
    private TelemeterListener listener;
    private static TelemeterLoader telemeterLoader;
    private Context context;

    public TelemeterLoader(Context context, TelemeterListener listener) {
        this.context = context;
        this.listener = listener;
    }

//    public static TelemeterLoader getInstance() {
//        if (telemeterLoader == null) {
//            telemeterLoader = new TelemeterLoader();
//        }
//        return telemeterLoader;
//    }

//    public void addListener(TelemeterListener listener) {
//        if (!listeners.contains(listener)) {
//            listeners.add(listener);
//        }
//    }
//
//    public void removeListener(TelemeterListener listener) {
//        if (!listeners.contains(listener)) {
//            listeners.remove(listener);
//        }
//    }

    public void updateData() {
        String login = PreferenceManager.getDefaultSharedPreferences(context).getString("be.simonraes.telemeter.login", "");
        String password = PreferenceManager.getDefaultSharedPreferences(context).getString("be.simonraes.telemeter.password", "");

        TelemeterSoap telemeterSoap = new TelemeterSoap(context, this);
        telemeterSoap.execute(login, password);
    }

    @Override
    public void loadComplete(String response) {
        TelenetXmlParser telenetXmlParser = new TelenetXmlParser(this);
        telenetXmlParser.execute(response);
    }

    @Override
    public void parseComplete(TelemeterData response) {
//        for (TelemeterListener listener : listeners) {
        listener.responseComplete(response);
//        }
    }


    public interface TelemeterListener {
        public void responseComplete(TelemeterData response);
    }
}
