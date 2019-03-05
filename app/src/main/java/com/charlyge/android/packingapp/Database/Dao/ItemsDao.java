package com.charlyge.android.packingapp.Database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.charlyge.android.packingapp.Database.Entities.Items;

import java.util.Date;
import java.util.List;
@Dao
public interface ItemsDao {

    @Insert
    void insertIntoItemsTable(Items items);

    @Query("Select * from Items")
    List<Items> getAllItems();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void UpdateItems(Items items);

    @Delete()
    void deleteItems(Items items);

    @Query("Select * from Items where itemsId=:id")
    Items getItemsById(int id);

    @Query("Select * from Items where  time=:time")
    List<Items> getItemsByTime(Date time);


    @Query("UPDATE Items SET status = :status WHERE itemsId =:id")
    void updateItemsStatus(boolean status, int id);
}
