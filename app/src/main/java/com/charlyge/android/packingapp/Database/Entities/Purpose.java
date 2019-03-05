package com.charlyge.android.packingapp.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Purpose {
    @PrimaryKey(autoGenerate = true)
    private int purposeId;
    private String purpose;

    @Ignore
    public Purpose(String purpose){
        this.purpose = purpose;
        this.purposeId = purposeId;
    }

    public Purpose(int purposeId, String purpose){
        this.purpose = purpose;
        this.purposeId = purposeId;
    }
    public int getPurposeId() {
        return purposeId;
    }

    public String getPurpose() {
        return purpose;
    }
}
