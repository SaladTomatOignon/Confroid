package fr.uge.confroid.works;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class WorkRequests {

    public static WorkRequest getUploadWorkRequest() {
        // TODO : Rendre parametrable les contraintes et la périodicité de la tache.
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED) // On WiFi available only
                .setRequiresBatteryNotLow(true) // When the device battery is not low
                .build();

        return new PeriodicWorkRequest.Builder(UploadWorker.class,
                1, TimeUnit.DAYS) // Everyday
                .build();
    }
}
