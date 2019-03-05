package com.charlyge.android.packingapp.Repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.charlyge.android.packingapp.Database.Entities.Items;
import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.charlyge.android.packingapp.Database.packDb.PackingReminderDb;

import java.util.Date;
import java.util.List;

public class AppRepository {
    private PackingReminderDb packingReminderDb;


    public AppRepository(Context context){
        packingReminderDb =  PackingReminderDb.getsInstance(context.getApplicationContext());

    }

    public LiveData<List<PackingReminder>> getAllPackingRm(){
        return packingReminderDb.packingReminderDao().getAllPackingReminders();
    }

    public List<PackingReminder> getAllPackingNoObserveRm(){
        return packingReminderDb.packingReminderDao().getAllNonObservePackingReminders();
    }

    public void insertPackingRm(PackingReminder packingReminder){
        packingReminderDb.packingReminderDao().insertIntoPackingReminderTable(packingReminder);
    }

    public void insertItems(Items items){
        packingReminderDb.itemsDao().insertIntoItemsTable(items);
    }

    public void deletePackingReminder(PackingReminder packingReminder){
        packingReminderDb.packingReminderDao().deletePackingReminder(packingReminder);
    }

    public List<Items> getAllItems(){
        return packingReminderDb.itemsDao().getAllItems();
    }

    public void deleteItem(Items items){
        packingReminderDb.itemsDao().deleteItems(items);
    }

    public List<Items> getItemsByTime(Date date){
        return packingReminderDb.itemsDao().getItemsByTime(date);
    }
}
