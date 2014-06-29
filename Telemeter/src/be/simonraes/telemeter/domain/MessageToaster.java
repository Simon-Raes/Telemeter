package be.simonraes.telemeter.domain;

import android.content.Context;
import android.widget.Toast;
import be.simonraes.telemeter.model.TelemeterData;

/**
 * Created by Simon Raes on 29/06/2014.
 */
public class MessageToaster {
    public static void displayStatusToast(Context context, TelemeterData data){
        if (data.getFault().getFaultString().contains("Incorrect Login or Password specified")) {
            Toast.makeText(context, "Incorrect login/password combination.", Toast.LENGTH_SHORT).show();
        } else if (data.getFault().getFaultString().contains("Please try accessing data after expiry time")) {
            Toast.makeText(context, "Refreshed too often. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Unknown error. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
