package fr.uge.confroid.services;

import android.content.Intent;

import fr.uge.confroid.storage.ConfroidDatabase;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroidlib.ConfroidIntents;

public class ConfigurationPuller extends BackgroundService {

    public ConfigurationPuller() {
        super("ConfigurationPuller");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("intent");
        }

        String[] requirements = {
            ConfroidIntents.EXTRA_NAME, ConfroidIntents.EXTRA_TOKEN,
            ConfroidIntents.EXTRA_REQUEST_ID, ConfroidIntents.EXTRA_RECEIVER,
            ConfroidIntents.EXTRA_VERSION
        };

        extrasGuard(intent, requirements);

        String name = intent.getStringExtra(ConfroidIntents.EXTRA_NAME);
        String token = intent.getStringExtra(ConfroidIntents.EXTRA_TOKEN);
        String requestId = intent.getStringExtra(ConfroidIntents.EXTRA_REQUEST_ID);
        String version = intent.getStringExtra(ConfroidIntents.EXTRA_VERSION);
        String receiver = intent.getStringExtra(ConfroidIntents.EXTRA_RECEIVER);
        int expiration = intent.getIntExtra(ConfroidIntents.EXTRA_EXPIRATION, -1); // TODO handle expiration

        authGuard(name, token);

        ConfroidDatabase.exec(this, dao -> {
            ConfroidPackage pkg = null;
            int versionNumber = parseVersion(version);
            if (versionNumber != -1) {
                pkg = dao.findByVersion(name, versionNumber);
            } else if (version.equals("latest")) {
                pkg = dao.findLastVersion(name);
            } else {
                pkg = dao.findByTag(name, version);
            }

            Intent response = new Intent(receiver);
            response.putExtra(ConfroidIntents.EXTRA_NAME, name);
            response.putExtra(ConfroidIntents.EXTRA_REQUEST_ID, requestId);
            if (pkg != null) {
                response.putExtra(ConfroidIntents.EXTRA_CONTENT, pkg.getConfig().toBundle());
                response.putExtra(ConfroidIntents.EXTRA_VERSION, pkg.getVersion());
            }
            sendBroadcast(response);
        });
    }

    private int parseVersion(String version) {
        try {
            return Integer.parseInt(version);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
