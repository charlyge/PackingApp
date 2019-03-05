import android.app.Application;
import android.util.Log;

import com.charlyge.android.packingapp.BuildConfig;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

    }

    private class ReleaseTree extends Timber.Tree {
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
}
