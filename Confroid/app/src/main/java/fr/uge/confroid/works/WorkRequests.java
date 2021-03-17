package fr.uge.confroid.works;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

import fr.uge.confroid.settings.AppSettings;

public class WorkRequests {

    public static WorkRequest getUploadWorkRequest() {
        AppSettings settings = AppSettings.getINSTANCE();

        Constraints.Builder constraintsBuilder = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED) // On WiFi available only
                .setRequiresBatteryNotLow(true) // When the device battery is not low
                .setRequiresCharging(settings.isRequiresCharging());

        if (settings.isAllowCellularData()) {
            constraintsBuilder.setRequiredNetworkType(NetworkType.CONNECTED); // Any connection type
        }

        return new PeriodicWorkRequest.Builder(UploadWorker.class,
                settings.getRepeatInterval(), settings.getRepeatIntervalTimeUnit())
                .setConstraints(constraintsBuilder.build())
                .build();
    }
}
