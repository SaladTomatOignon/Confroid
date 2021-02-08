package fr.uge.confroid.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import fr.uge.confroid.R;

/**
 * Base class that handle background services limitations in post android 8.0 devices.
 *
 * https://developer.android.com/about/versions/oreo/background
 * https://stackoverflow.com/questions/46445265/android-8-0-java-lang-illegalstateexception-not-allowed-to-start-service-inten
 */
public abstract class BackgroundService extends IntentService {
    private final String name;

    public BackgroundService(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getPackageName();
            String channelName = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    importance
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    this,
                    channelId
            );
            startForeground(1, builder.build());
        }
    }
}
