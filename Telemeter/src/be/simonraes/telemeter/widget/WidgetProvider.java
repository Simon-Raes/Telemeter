package be.simonraes.telemeter.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;
import be.simonraes.telemeter.R;

/**
 * Created by Simon Raes on 23/06/2014.
 */
public class WidgetProvider extends AppWidgetProvider {

    private Context context;
    private RemoteViews widgetLayout;
    private Bundle bundle;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        this.context = context;

        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            set1x1Layout();

            appWidgetManager.updateAppWidget(appWidgetId, widgetLayout);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        bundle = newOptions;
        this.context = context;

        int minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        if(minWidth<100){
            //1x1
            set1x1Layout();
        } else if (minWidth<200){
            //2x1
            set2x1Layout();
        } else {
            //also 2x1 (temp)
            set2x1Layout();
        }

        System.out.println("current widget min-max dimensions: "+minWidth+"-"+maxWidth+ " x "+minHeight+"-"+maxHeight);

        appWidgetManager.updateAppWidget(appWidgetId, widgetLayout);
    }

    private void set1x1Layout(){
        widgetLayout = new RemoteViews(context.getPackageName(), R.layout.widget_1x1);
        widgetLayout.setImageViewBitmap(R.id.imgWidgetUsage, usageTextToBitmap("Verbruikt", getCurrentUsage(),"GB"));
    }

    private void set2x1Layout(){
        widgetLayout = new RemoteViews(context.getPackageName(), R.layout.widget_2x1);
        widgetLayout.setImageViewBitmap(R.id.imgWidgetUsage, usageTextToBitmap("Verbruikt", getCurrentUsage(),"GB"));
        widgetLayout.setImageViewBitmap(R.id.imgWidgetRemaining, usageTextToBitmap("Resterend", getMinUsgeRemaining(),"GB"));
    }

    private String getCurrentUsage(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("TotalUsage","?");
    }

    private String getMinUsgeRemaining(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("MinUsageRemaining","?");
    }

    public Bitmap usageTextToBitmap(String header, String usage, String unit)
    {
        System.out.println("generating bitmap");
        Bitmap myBitmap = Bitmap.createBitmap(105, 115, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        if(context!=null){
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
}