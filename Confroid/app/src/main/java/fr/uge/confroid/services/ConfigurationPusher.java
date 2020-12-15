package fr.uge.confroid.services;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import fr.uge.confroid.storage.AuthToken;

public class ConfigurationPusher extends BackgroundService {
    public ConfigurationPusher() {
        super("ConfigurationPusher");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;

        // TODO handle names like fr.uge.calculator.latestComputations/0
        String name = intent.getStringExtra("name");
        String token = intent.getStringExtra("token");
        String receiver = intent.getStringExtra("receiver");
        String tag = intent.getStringExtra("tag");
        Bundle content = intent.getBundleExtra("content");

        if (name == null || receiver == null || token == null) {
            sendResponse(receiver, false);
            return;
        }

        AuthToken auth = new AuthToken(this);
        if (!auth.get(name).equals(token)) {
            sendResponse(receiver, false);
            return;
        }

        // TODO save configuration in database

        sendResponse(receiver, true);
    }

    private void sendResponse(String receiver, boolean success) {
        if (receiver != null) {
            sendBroadcast(new Intent(receiver).putExtra("success", success));
        }
    }
}
