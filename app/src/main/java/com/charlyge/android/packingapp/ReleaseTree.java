package com.charlyge.android.packingapp;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
        }

        // log your crash to your favourite
        // Sending crash report to Firebase CrashAnalytics

        // FirebaseCrash.report(message);
        // FirebaseCrash.report(new Exception(message));
    }
}

