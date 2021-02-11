package fr.uge.confroidlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.util.UUID;
import java.util.function.Consumer;

import androidx.core.content.ContextCompat;

public class ConfroidUtils {
    public static void saveConfiguration(Context context, String name, Object value, String versionName) {
        withAuth(context, token -> {
            Intent intent = new Intent();
            intent.setAction(ConfroidIntents.INTENT_CONFIGURATION_PUSHER);
            intent.setPackage(ConfroidIntents.PACKAGE_NAME);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

            String appId = context.getApplicationContext().getPackageName();
            intent.putExtra(ConfroidIntents.EXTRA_NAME, appId + "." + name);
            intent.putExtra(ConfroidIntents.EXTRA_TAG, versionName);
            intent.putExtra(ConfroidIntents.EXTRA_TOKEN, token);
            intent.putExtra(ConfroidIntents.EXTRA_CONTENT, BundleUtils.convertToBundleReflection(value));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, intent);
            } else {
                context.startService(intent);
            }
        });
    }

    public static <T> void loadConfiguration(Context context, String name, String version, Consumer<T> callback) {
        withAuth(context, token -> {
            String appId = context.getApplicationContext().getPackageName();
            String requestId = appId + "." + UUID.randomUUID();

            Toast.makeText(context, token, Toast.LENGTH_LONG).show();
        });
    }

    public static <T> void subscribeConfiguration(Context context, String name, Consumer<T> callback) {
        withAuth(context, token -> {
            Toast.makeText(context, token, Toast.LENGTH_LONG).show();
        });
    }

    public static <T> void cancelConfigurationSubscription(Context context, Consumer<T> callback) {
        withAuth(context, token -> {
            Toast.makeText(context, token, Toast.LENGTH_LONG).show();
        });
    }

    public static void getConfigurationVersions(Context context, String name, Consumer<Bundle> callback) {
        withAuth(context, token -> {
            Toast.makeText(context, token, Toast.LENGTH_LONG).show();
        });
    }

    private static void withAuth(Context context, Consumer<String> callback) {
        SharedPreferences preferences = context.getSharedPreferences(
            ConfroidUtils.class.getName(),
            Context.MODE_PRIVATE
        );

        String appId = context.getApplicationContext().getPackageName();
        String prefK = appId + ".token";
        String token = preferences.getString(prefK, null);
        if (token != null) {
            callback.accept(token);
            return;
        }

        String receiver = appId + ".auth-receiver";
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String token = intent.getStringExtra("token");
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(prefK, token);
                editor.apply();
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(receiver));


        Intent intent = new Intent();
        intent.setAction(ConfroidIntents.INTENT_TOKEN_DISPENSER);
        intent.setPackage(ConfroidIntents.PACKAGE_NAME);

        intent.putExtra("name", appId);
        intent.putExtra("receiver", receiver);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }
}
