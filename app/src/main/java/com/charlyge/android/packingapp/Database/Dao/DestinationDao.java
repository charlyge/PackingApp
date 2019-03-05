package com.charlyge.android.packingapp.Database.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.charlyge.android.packingapp.Database.Entities.Destination;

import java.util.List;

@Dao
public interface DestinationDao {
    @Insert
    void insertIntoDestinationTable(Destination destination);

    @Query("Select * from Destination")
    List<Destination> getAllDestinations();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void UpdateReminder(Destination destination);

    @Delete()
    void deleteDestinations(Destination destination);

    @Query("Select * from Destination where destinationId=:id")
    Destination getDestinationById(int id);
}
