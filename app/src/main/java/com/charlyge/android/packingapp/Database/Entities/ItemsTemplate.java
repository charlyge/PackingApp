package com.charlyge.android.packingapp.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ItemsTemplate {
    @PrimaryKey(autoGenerate = true)
    private int ItemsTemplateId;
    public String Items;


    public ItemsTemplate(int ItemsTemplateId, String Items){
        this.Items = Items;
        this.ItemsTemplateId = ItemsTemplateId;
    }

    @Ignore
    public ItemsTemplate(String Items){
        this.Items = Items;
    }

    public int getItemsTemplateId() {
        return ItemsTemplateId;
    }

    public String getItems() {
        return Items;
    }
}
