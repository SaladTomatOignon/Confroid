package fr.uge.confroid.services;

import android.content.Intent;
import android.os.Bundle;

import java.util.Date;
import java.util.Objects;

import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.storage.ConfroidDatabase;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroidlib.ConfroidIntents;

// TODO partial updates like fr.uge.calculator.latestComputations/0.

public class ConfigurationPusher extends BackgroundService {
    public ConfigurationPusher() {
        super("ConfigurationPusher");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("intent");
        }

        extrasGuard(intent, ConfroidIntents.EXTRA_NAME, ConfroidIntents.EXTRA_TOKEN);

        String tag = intent.getStringExtra(ConfroidIntents.EXTRA_TAG);

        String name = intent.getStringExtra(ConfroidIntents.EXTRA_NAME);
        String token = intent.getStringExtra(ConfroidIntents.EXTRA_TOKEN);
        Bundle content = intent.getBundleExtra(ConfroidIntents.EXTRA_CONTENT);
        String receiver = intent.getStringExtra(ConfroidIntents.EXTRA_RECEIVER);

        authGuard(name, token);

        if (tag != null && content == null) { // should update only the last version's tag
            ConfroidDatabase.exec(this, dao -> {
                ConfroidPackage pkg = dao.findLastVersion(name);
                if (pkg != null) {
                    pkg.setTag(tag);
                    dao.update(pkg);
                    sendSuccess(receiver);
                } else {
                    throw new AssertionError("Missing configuration " + name);
                }
            });
            return;
        }

        if (content == null) {
            throw new AssertionError("Extra 'content' is required when 'tag' is not specified");
        }

        ConfroidDatabase.exec(this, dao -> {
            ConfroidPackage pkg = new ConfroidPackage(
                name, 0, new Date(), Configuration.fromBundle(content), tag
            );

            ConfroidPackage latest = dao.findLastVersion(name);
            if (latest != null) {
                pkg.setVersion(latest.getVersion() + 1);
            }

            /* Removing previous same tags because (name, tag) is unique */
            if (!Objects.isNull(pkg.getTag())) {
                dao.removeTag(pkg.getName(), pkg.getTag());
            }

            dao.create(pkg);

            sendSuccess(receiver);
        });
    }

    private void sendSuccess(String receiver) {
        if (receiver != null) {
            sendBroadcast(
                new Intent(receiver).putExtra(ConfroidIntents.EXTRA_SUCCESS, true)
            );
        }
    }
}
