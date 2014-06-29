package be.simonraes.telemeter.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import be.simonraes.telemeter.R;
import be.simonraes.telemeter.domain.MessageToaster;
import be.simonraes.telemeter.domain.TelemeterLoader;
import be.simonraes.telemeter.model.TelemeterData;
import be.simonraes.telemeter.util.Conversion;

/**
 * Created by Simon Raes on 23/06/2014.
 * Code for the widget.
 */
public class WidgetProvider extends AppWidgetProvider implements TelemeterLoader.TelemeterListener {

    private Context context;
    private RemoteViews widgetLayout;

    private TelemeterLoader telemeterLoader;
    private TelemeterData telemeterData;
    //save the last found info so it can be displayed
    private String previousUsage, previousMinRemaining;

    private static final String SYNC_CLICKED = "automaticWidgetSyncButtonClick";

    private int minWidth, maxWidth, minHeight, maxHeight;


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        this.context = context;


        loadTelemeterData();


        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            System.out.println("updating widget " + i);
            int appWidgetId = appWidgetIds[i];

            set1x1Layout();

            appWidgetManager.updateAppWidget(appWidgetId, widgetLayout);
        }
    }

    private void loadTelemeterData() {
//        if (telemeterLoader == null) {
//            telemeterLoader = TelemeterLoader.getInstance();
//            System.out.println("adding me as listener (widget)");
//            telemeterLoader.addListener(this);
//        }
        telemeterLoader = new TelemeterLoader(context, this);
        telemeterLoader.updateData();
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        this.context = context;

        minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        saveDimensions();

        System.out.println("got new minwidth:" + minWidth);

        setLayout();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        this.context = context;

        if (SYNC_CLICKED.equals(intent.getAction())) {

            System.out.println("clicked widget");

            loadTelemeterData();

            widgetLayout = new RemoteViews(context.getPackageName(), R.layout.widget_loading);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (appWidgetManager != null) {
                appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), widgetLayout);
            }
        }
    }

    /*Saves the widget dimensions to SharedPreferences so they're still available after updating the widget content.*/
    private void saveDimensions() {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("minWidth", minWidth).commit();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("maxWidth", maxWidth).commit();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("minHeight", minHeight).commit();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("maxHeight", maxHeight).commit();
    }

    private void loadDimensions() {
        minWidth = PreferenceManager.getDefaultSharedPreferences(context).getInt("minWidth", 0);
        maxWidth = PreferenceManager.getDefaultSharedPreferences(context).getInt("maxWidth", 0);
        minHeight = PreferenceManager.getDefaultSharedPreferences(context).getInt("minHeight", 0);
        maxHeight = PreferenceManager.getDefaultSharedPreferences(context).getInt("maxHeight", 0);
    }

    private void setLayout() {

        loadDimensions();

        if (minWidth < 100) {
            //1x1
            set1x1Layout();
        } else if (minWidth < 200) {
            //2x1
            set2x1Layout();
        } else {
            //also 2x1 (temp)
            set2x1Layout();
        }

//        System.out.println("current widget min-max dimensions: " + minWidth + "-" + maxWidth + " x " + minHeight + "-" + maxHeight);

        if (widgetLayout != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (appWidgetManager != null) {
                appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), widgetLayout);
            }
        }

    }

    private void set1x1Layout() {
        if (context != null) {
            widgetLayout = new RemoteViews(context.getPackageName(), R.layout.widget_1x1);
            widgetLayout.setOnClickPendingIntent(R.id.layWidget1x1, getPendingSelfIntent(context, SYNC_CLICKED));
            widgetLayout.setImageViewBitmap(R.id.imgWidgetUsage, usageTextToBitmap("Verbruikt", getCurrentUsage(), "GB"));
        }
    }

    private void set2x1Layout() {
        widgetLayout = new RemoteViews(context.getPackageName(), R.layout.widget_2x1);
        widgetLayout.setOnClickPendingIntent(R.id.layWidget2x1, getPendingSelfIntent(context, SYNC_CLICKED));

        widgetLayout.setImageViewBitmap(R.id.imgWidgetUsage, usageTextToBitmap("Verbruikt", getCurrentUsage(), "GB"));
        widgetLayout.setImageViewBitmap(R.id.imgWidgetRemaining, usageTextToBitmap("Resterend", getMinUsgeRemaining(), "GB"));
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private String getCurrentUsage() {
        if (telemeterData != null) {
            return Conversion.doubleToRoundedString(telemeterData.getUsage().getTotalUsage());
        } else {
            return previousUsage;
        }
    }

    private String getMinUsgeRemaining() {
        if (telemeterData != null) {
            return Conversion.doubleToRoundedString(telemeterData.getUsage().getMinUsageRemaining());
        } else {
            return previousMinRemaining;
        }
    }

    public Bitmap usageTextToBitmap(String header, String usage, String unit) {
        System.out.println("generating bitmap");
        Bitmap myBitmap = Bitmap.createBitmap(105, 115, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        if (context != null) {
            System.out.println("context not null");
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/cooper.ttf");
            paint.setAntiAlias(true);
            paint.setSubpixelText(true);
            paint.setTypeface(tf);
            paint.setStyle(Paint.Style.FILL);
            //can't get to the color values without an activity, create it from hex
            paint.setColor(Color.parseColor("#F36535"));
            paint.setTextAlign(Paint.Align.CENTER);
//            myCanvas.drawColor(Color.BLACK);
            //header settings
            paint.setTextSize(15);
            myCanvas.drawText(header, 52, 15, paint);

            //usage text settings
            paint.setTextSize(48);
            myCanvas.drawText(usage, 52, 55, paint);
            myCanvas.drawText(unit, 52, 105, paint);
        }

        System.out.println("returning bitmap");
        return myBitmap;
    }

    @Override
    public void responseComplete(TelemeterData response) {
        System.out.println("response received in widget");
        telemeterData = response;
        setLayout();
        if (response.getFault().getFaultString() != null && !response.getFault().getFaultString().equals("")) {
            //an error occurred - display the correct toast
            MessageToaster.displayStatusToast(context, response);
        } else {
            //store the newest data
            previousUsage = Conversion.doubleToRoundedString(response.getUsage().getTotalUsage());
            previousMinRemaining = Conversion.doubleToRoundedString(response.getUsage().getMinUsageRemaining());
        }
    }
}