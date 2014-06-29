package be.simonraes.telemeter.util;

import android.preference.PreferenceManager;

import java.text.DecimalFormat;

/**
 * Created by Simon Raes on 29/06/2014.
 */
public class Conversion {

    /*Converts a double to a string and removes the decimals*/
    public static String doubleToRoundedString(double number){
        DecimalFormat format = new DecimalFormat("0");

        return format.format(number);
    }
}
