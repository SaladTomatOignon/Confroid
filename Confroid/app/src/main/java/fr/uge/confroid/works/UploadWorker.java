package fr.uge.confroid.works;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.time.LocalDateTime;
import java.time.ZoneId;

import fr.uge.confroid.storage.ConfroidDatabase;
import fr.uge.confroid.web.Client;

/**
 * Worker that retrieves every configurations from locale database
 * and sends it to Confroid Storage Service.
 */
public class UploadWorker extends Worker {
    private static final String LOG_TAG = "Upload Configs Work";

    public UploadWorker(Context context, WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(LOG_TAG, "Starting uploading all configs from database");

        ConfroidDatabase.exec(getApplicationContext(), dao -> {
            dao.findAllNames().forEach(name -> {
                dao.findAllVersions(name).forEach(pkg -> {
                    Client.INSTANCE.sendConfig(pkg.getConfig(), pkg.getName(), pkg.getVersion(), pkg.getTag(), LocalDateTime.ofInstant(pkg.getDate().toInstant(), ZoneId.systemDefault()),
                            (response) -> Log.v(LOG_TAG, String.join(" ", "SUCCESS", pkg.getName(), String.valueOf(pkg.getVersion()), pkg.getTag())),
                            (error) -> Log.w(LOG_TAG, String.join(" ", "FAIL", pkg.getName(), String.valueOf(pkg.getVersion()), pkg.getTag(), error.toString())));
                });
            });
        });

        Log.i(LOG_TAG, "Ending uploading all configs from database");

        return Result.success();
    }
}
