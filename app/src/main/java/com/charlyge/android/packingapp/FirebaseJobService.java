/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.charlyge.android.packingapp;


import android.os.AsyncTask;
import android.util.Log;

import com.charlyge.android.packingapp.Database.Entities.Items;
import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.charlyge.android.packingapp.Database.packDb.PackingReminderDb;
import com.charlyge.android.packingapp.Repository.AppRepository;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;


public class FirebaseJobService extends JobService {
    private int statusCount = 0;
    private AsyncTask mBackgroundTask;
    private static final String DATE_FORMAT = "dd/MM/yyyyhh:mm";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Timber.d(" Onstart Job");
        final FirebaseJobService context = FirebaseJobService.this;
        final AppRepository appRepository = new AppRepository(context.getApplication());

        mBackgroundTask = new AsyncTask() {


            @Override
            protected Object doInBackground(Object[] params) {
                Log.d("ServiceNotification", " in doing background");

                List<PackingReminder> taskEntryList = appRepository.getAllPackingNoObserveRm();
                if (taskEntryList.size() == 0) {
                    Log.d("EmtpyDb", "EmtpyDb");
                    return new ArrayList<>();
                } else {
                    Date timeOfTravel = taskEntryList.get(0).getTimeOfTravel();
                    Timber.d(" time of travel is " + dateFormat.format(timeOfTravel));
                    List<Items> itemlist = PackingReminderDb.getsInstance(context).itemsDao().getItemsByTime(timeOfTravel);
                    for (Items items : itemlist) {
                        if (items.getStatus()) {
                            statusCount++;
                        }
                    }
                    if (statusCount == itemlist.size()) {
                        //SendAllPackedNotification
                        NotificationUtils.sendNotification(context, " All Items packed ", "You have No Items Unpacked");
                        return new ArrayList<>();
                    }

                    NotificationUtils.sendNotification(context,
                            "You still have items unpacked Go to check List and mark " +
                                    "off items You have packed. Keep packing!", "Packing Reminder");
                    Timber.d(" sent");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

                jobFinished(jobParameters, true);
            }
        };

        mBackgroundTask.execute();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}