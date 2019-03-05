package com.charlyge.android.packingapp.Database.packDb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.charlyge.android.packingapp.Database.Dao.DestinationDao;
import com.charlyge.android.packingapp.Database.Dao.ItemsDao;
import com.charlyge.android.packingapp.Database.Dao.PackingReminderDao;
import com.charlyge.android.packingapp.Database.Entities.Destination;
import com.charlyge.android.packingapp.Database.Entities.Items;
import com.charlyge.android.packingapp.Database.Entities.ItemsTemplate;
import com.charlyge.android.packingapp.Database.Entities.Purpose;
import com.charlyge.android.packingapp.Database.Entities.PurposeTemplate;
import com.charlyge.android.packingapp.Database.Entities.ReminderPattern;
import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.charlyge.android.packingapp.DateConverter;

@Database(entities = {PackingReminder.class, Destination.class,
        Purpose.class, Items.class, ItemsTemplate.class, ReminderPattern.class, PurposeTemplate.class}
,version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class PackingReminderDb extends RoomDatabase{

     private static PackingReminderDb sInstance;
     private static String DB_NAME = "PackingReminderDb";

    public synchronized static PackingReminderDb getsInstance(Context context) {
        if(sInstance==null){
            sInstance = Room.databaseBuilder(context,PackingReminderDb.class,DB_NAME).build();
            //Temporary allowing Queries on Main Thread will change later
        }
        return sInstance;
    }

    public abstract PackingReminderDao packingReminderDao();
    public abstract ItemsDao itemsDao();
    public abstract DestinationDao destinationDao();

}
