package com.charlyge.android.packingapp.AppWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.charlyge.android.packingapp.MainActivity;
import com.charlyge.android.packingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class PackListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,String packlist) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pack_list_widget);
        views.setTextViewText(R.id.appwidget_text, packlist);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void UpdatePacklistWidget(Context context, AppWidgetManager appWidgetManager, int[] widgetIds, String packlist) {
        for (int appWidgetId : widgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, packlist);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        if(Build.VERSION.SDK_INT > 25){
            //Start the widget service to update the widget
            WidgetService.StartWidgetServiceO(context);
        }
        else{

            WidgetService.StartWidgetService(context);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

