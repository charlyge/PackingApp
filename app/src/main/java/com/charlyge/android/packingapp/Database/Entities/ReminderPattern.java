package com.charlyge.android.packingapp.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ReminderPattern {
    @PrimaryKey(autoGenerate = true)
    private int reminderPtternId;
    private int reminderPttern;

    public ReminderPattern(int reminderPtternId, int reminderPttern){
     this.reminderPttern = reminderPttern;
     this.reminderPtternId= reminderPtternId;
    }


    @Ignore
    public ReminderPattern(int reminderPttern){
        this.reminderPttern = reminderPttern;

    }

    public int getReminderPtternId() {
        return reminderPtternId;
    }

    public int getReminderPttern() {
        return reminderPttern;
    }
}
