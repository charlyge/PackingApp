package com.charlyge.android.packingapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.charlyge.android.packingapp.Database.Entities.Items;
import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.charlyge.android.packingapp.Repository.AppRepository;

import java.util.List;

public class PackingReminderViewModel extends AndroidViewModel {
    private LiveData<List<PackingReminder>> packingReminderList;


    public PackingReminderViewModel(@NonNull Application application) {
        super(application);
        AppRepository appRepository = new AppRepository(application);
        packingReminderList = appRepository.getAllPackingRm();


    }

    public LiveData<List<PackingReminder>> getPackingReminderList() {
        return packingReminderList;
    }


}
