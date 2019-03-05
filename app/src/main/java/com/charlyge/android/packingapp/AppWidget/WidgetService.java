package com.charlyge.android.packingapp.AppWidget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.charlyge.android.packingapp.AppExecutors;
import com.charlyge.android.packingapp.Database.Entities.Items;
import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.charlyge.android.packingapp.Repository.AppRepository;

import java.util.Date;
import java.util.List;

public class WidgetService extends IntentService {
    private static String SERVICE_ACTION = "serviceaction";
    public static final String MY_PREFERENCE ="MY PRFERENCE";
    public static final String PACKLIST_KEY = "PACKLISTKEY";

    public static void StartWidgetService(Context context){
        Intent intent = new Intent(context,WidgetService.class);
        intent.setAction(SERVICE_ACTION);
        context.startService(intent);
    }


    // For Android O and above
    public static void StartWidgetServiceO(Context context){
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(SERVICE_ACTION);
        ContextCompat.startForegroundService(context,intent);
    }


    public WidgetService() {
        super("WidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if(action.equals(SERVICE_ACTION)){
            UpdatePackListWidget();
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "Channelid";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "charlyge service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }
    private void UpdatePackListWidget() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppRepository appRepository = new AppRepository(WidgetService.this);
                List<PackingReminder> packingReminderList = appRepository.getAllPackingNoObserveRm();
                if (packingReminderList.size()==0){
                    return;
                }
                Date date = packingReminderList.get(0).getTimeOfTravel();
                if(date==null){
                    Log.e("Empty Date","No Date found");

                }
                else{
                 List<Items> itemsList = appRepository.getItemsByTime(date);
                 StringBuilder builder = new StringBuilder();
                    for (Items items:itemsList) {
                        builder.append(":-").append(items.getItems()).append("\n");
                    }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetService.this);
                int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(WidgetService.this,PackListWidget.class));
                PackListWidget.UpdatePacklistWidget(WidgetService.this,appWidgetManager,widgetIds,builder.toString());
                }

            }
        });

    }

}
