package com.charlyge.android.packingapp.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Destination {
    @PrimaryKey(autoGenerate = true)
    private int destinationId;
    private String destinationName;

    @Ignore
    public Destination(String destinationName){
    this.destinationName = destinationName;

    }


    public Destination(int destinationId, String destinationName){
    this.destinationId = destinationId;

    }

    public int getDestinationId() {
        return destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }
}
