package com.charlyge.android.packingapp.Database.Dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.charlyge.android.packingapp.Database.Entities.PackingReminder;

import java.util.List;

@Dao
public interface PackingReminderDao {

    @Insert
    void insertIntoPackingReminderTable(PackingReminder packingReminder);

    @Query("Select * from PackingReminder")
    LiveData<List<PackingReminder>> getAllPackingReminders();

    @Query("Select * from PackingReminder")
    List<PackingReminder> getAllNonObservePackingReminders();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void UpdateReminder(PackingReminder packingReminder);

    @Delete()
    void deletePackingReminder(PackingReminder packingReminder);

    @Query("Select * from PackingReminder where ReminderId=:id")
    PackingReminder getPackingReminderById(int id);


}
