package com.charlyge.android.packingapp.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
@Entity
public class PurposeTemplate {
    @PrimaryKey(autoGenerate = true)
    private int PurposeTemplateId;
    private String Purpose;

    public PurposeTemplate(int PurposeTemplateId, String Purpose){
        this.Purpose = Purpose;
        this.PurposeTemplateId = PurposeTemplateId;

    }

    @Ignore
    public PurposeTemplate(String Purpose){
        this.Purpose = Purpose;

    }
    public int getPurposeTemplateId() {
        return PurposeTemplateId;
    }

    public String getPurpose() {
        return Purpose;
    }
}
