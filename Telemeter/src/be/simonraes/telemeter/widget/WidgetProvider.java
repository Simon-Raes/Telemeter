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
import be.simonraes.telemeter.database.TelemeterDataDataSource;
import be.simonraes.telemeter.domain.TelemeterLoader;
import be.simonraes.telemeter.model.TelemeterData;
import be.simonraes.telemeter.util.Conversion;

/**
 * Created by Simon Raes on 23/06/2014.
 * Code for the widget.
 */
public class WidgetProvider extends AppWidgetProvider implements TelemeterLoader.TelemeterLoaderResponse {

    private Context context;
    private RemoteViews widgetLayout;

    private TelemeterLoader telemeterLoader;
    private TelemeterData telemeterData;

    private String previousUsage, previousMinRemaining;
    private int minWidth, maxWidth, minHeight, maxHeight;

    private static final String SYNC_CLICKED = "telemeterWidgetClicked";

    /**
     * Called when the widget is added to the home screen.
     */
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;

        TelemeterDataDataSource tdds = new TelemeterDataDataSource(context);
        telemeterData = tdds.getLatestTelemeterData();

        TelemeterLoader.registerAsListener(this);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < appWidgetIds.length; i++) {
            System.out.println("updating widget " + i);
            int appWidgetId = appWidgetIds[i];

            set1x1Layout();

            appWidgetManager.updateAppWidget(appWidgetId, widgetLayout);
        }
    }

    /**
     * Called every time the user resizes the widget.
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        this.context = context;

        minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        saveDimensions();
        setLayout();
    }

    /**
     * Called when a user taps the widget.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        this.context = context;

        if (SYNC_CLICKED.equals(intent.getAction())) {

            refreshTelemeterData();

            // Start the loading indicator.
            widgetLayout = new RemoteViews(context.getPackageName(), R.layout.widget_loading);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (appWidgetManager != null) {
                appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), widgetLayout);
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        TelemeterLoader.unregisterAsListener(this);
    }

    /**
     * Saves the widget dimensions to SharedPreferences so they will be available after updating the widget content.
     */
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

        if (telemeterData == null) {
            loadLatestDatabaseData();
        }

        loadDimensions();

        if (minWidth < 100) {
            set1x1Layout();
        } else if (minWidth < 200) {
            set2x1Layout();
        } else {
            //also 2x1 for bigger widgets (todo: additional, bigger layouts with more info)
            set2x1Layout();
        }

        if (widgetLayout != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (appWidgetManager != null) {
                appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), widgetLayout);
            }
        }
    }

    private void set1x1Layout() {
        widgetLayout = new RemoteViews(context.getPackageName(), R.layout.widget_1x1);
        widgetLayout.setOnClickPendingIntent(R.id.layWidget1x1, getPendingSelfIntent(context, SYNC_CLICKED));

        widgetLayout.setImageViewBitmap(R.id.imgWidgetUsage, usageTextToBitmap("Verbruikt", getCurrentUsage(), "GB"));
    }

    private void set2x1Layout() {
        widgetLayout = new RemoteViews(context.getPackageName(), R.layout.widget_2x1);
        widgetLayout.setOnClickPendingIntent(R.id.layWidget2x1, getPendingSelfIntent(context, SYNC_CLICKED));

        widgetLayout.setImageViewBitmap(R.id.imgWidgetUsage, usageTextToBitmap("Verbruikt", getCurrentUsage(), "GB"));
        widgetLayout.setImageViewBitmap(R.id.imgWidgetRemaining, usageTextToBitmap("Resterend", getMinUsageRemaining(), "GB"));
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private String getCurrentUsage() {
        return Conversion.doubleToRoundedString(telemeterData.getUsage().getTotalUsage());
    }

    private String getMinUsageRemaining() {
        return Conversion.doubleToRoundedString(telemeterData.getUsage().getMinUsageRemaining());
    }

    public Bitmap usageTextToBitmap(String header, String usage, String unit) {
        Bitmap myBitmap = Bitmap.createBitmap(105, 115, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        if (context != null) {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/cooper.ttf");
            paint.setAntiAlias(true);
            paint.setSubpixelText(true);
            paint.setTypeface(tf);
            paint.setStyle(Paint.Style.FILL);
            //can't get to the color values without an activity, create it from hex
            paint.setColor(Color.parseColor("#F36535"));
            paint.setTextAlign(Paint.Align.CENTER);

            //header settings
            paint.setTextSize(15);
            myCanvas.drawText(header, 52, 15, paint);

            //usage text settings
            paint.setTextSize(48);
            myCanvas.drawText(usage, 52, 55, paint);
            myCanvas.drawText(unit, 52, 105, paint);
        }

        return myBitmap;
    }

    /*Requests new Telemeter data.*/
    private void refreshTelemeterData() {
        telemeterLoader = new TelemeterLoader(context, this);
        telemeterLoader.updateData();
    }

    /*Receives notification that new data is available.*/
    @Override
    public void telemeterDataUpdated() {
        loadLatestDatabaseData();
    }

    private void loadLatestDatabaseData() {
        TelemeterDataDataSource tdds = new TelemeterDataDataSource(context);
        telemeterData = tdds.getLatestTelemeterData();
        setLayout();

        //store the newest data
        previousUsage = Conversion.doubleToRoundedString(telemeterData.getUsage().getTotalUsage());
        previousMinRemaining = Conversion.doubleToRoundedString(telemeterData.getUsage().getMinUsageRemaining());
    }
}