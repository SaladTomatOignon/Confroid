package fr.uge.confroid.services;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import androidx.annotation.Nullable;
import fr.uge.confroid.storage.ConfroidDatabase;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroidlib.ConfroidIntents;

public class ConfigurationVersions extends BackgroundService {

    public ConfigurationVersions() {
        super("ConfigurationVersions");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("intent");
        }

        String[] requirements = {
            ConfroidIntents.EXTRA_NAME, ConfroidIntents.EXTRA_TOKEN,
            ConfroidIntents.EXTRA_REQUEST_ID, ConfroidIntents.EXTRA_RECEIVER
        };

        extrasGuard(intent, requirements);

        String name = intent.getStringExtra(ConfroidIntents.EXTRA_NAME);
        String token = intent.getStringExtra(ConfroidIntents.EXTRA_TOKEN);
        String receiver = intent.getStringExtra(ConfroidIntents.EXTRA_RECEIVER);
        String requestId = intent.getStringExtra(ConfroidIntents.EXTRA_REQUEST_ID);

        authGuard(name, token);

        ConfroidDatabase.exec(this, dao -> {
            List<ConfroidPackage> packages = dao.findAllVersions(name);
            Bundle versions = new Bundle();
            packages.forEach(p -> {
                Bundle version = new Bundle();
                version.putSerializable(ConfroidIntents.EXTRA_DATE, p.getDate());
                if (p.getTag() != null) {
                    version.putString(ConfroidIntents.EXTRA_TAG, p.getTag());
                }
                versions.putBundle(Integer.toString(p.getVersion()), version);
            });

            Intent response = new Intent(receiver);
            response.putExtra(ConfroidIntents.EXTRA_NAME, name);
            response.putExtra(ConfroidIntents.EXTRA_REQUEST_ID, requestId);
            response.putExtra(ConfroidIntents.EXTRA_VERSIONS, versions);
            sendBroadcast(response);
        });
    }
}
