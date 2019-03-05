package com.charlyge.android.packingapp.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class PackingReminder {
    @PrimaryKey(autoGenerate = true)
    private int ReminderId;
    private String destination;
    private String travelMode;
    private Date timeOfTravel;
    private int reminderPattern;
    private String purpose;

    @Ignore
    public PackingReminder(){

    }
    public PackingReminder(int ReminderId, String destination, String travelMode, String purpose,Date timeOfTravel, int reminderPattern){
        this.destination = destination;
        this.ReminderId= ReminderId;
        this.reminderPattern = reminderPattern;
        this.timeOfTravel = timeOfTravel;
        this.purpose = purpose;
        this.travelMode = travelMode;

    }

          @Ignore
        public PackingReminder(String destination, String travelMode, String purpose,Date timeOfTravel, int reminderPattern){
        this.destination = destination;
        this.reminderPattern = reminderPattern;
        this.timeOfTravel = timeOfTravel;
        this.purpose = purpose;
        this.travelMode = travelMode;

    }

    public Date getTimeOfTravel() {
        return timeOfTravel;
    }

    public String getDestination() {
        return destination;
    }

    public int getReminderId() {
        return ReminderId;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public int getReminderPattern() {
        return reminderPattern;
    }

    public String getPurpose() {
        return purpose;
    }
}
