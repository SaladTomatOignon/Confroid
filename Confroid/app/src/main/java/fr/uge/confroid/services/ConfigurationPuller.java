package fr.uge.confroid.services;

import android.content.Intent;

import androidx.annotation.Nullable;

public class ConfigurationPuller extends BackgroundService {
    private static final String TAG = "ConfigurationPuller";

    public ConfigurationPuller() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) { }
}
