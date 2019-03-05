package com.charlyge.android.packingapp.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Items {
    @PrimaryKey(autoGenerate = true)
    private int itemsId;
    private String purpose;
    private Date time;
    private String items;
    private boolean status;

    public Items(int itemsId, String purpose, Date time,String items,boolean status){
   this.itemsId = itemsId;
   this.items = items;
   this.purpose = purpose;
   this.time = time;
   this.status = status;

   }
       @Ignore
    public Items(String purpose, Date time,String items,boolean status){
        this.purpose = purpose;
        this.items =items;
        this.time = time;
        this.status = status;

    }

    public Date getTime() {
        return time;
    }


    public int getItemsId() {
        return itemsId;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getItems() {
        return items;
    }

    public boolean getStatus() {
        return status;
    }
}
