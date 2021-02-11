package fr.uge.confroidlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import androidx.core.content.ContextCompat;

/**
 * Exposes convenient methods to use Confroid APIs.
 */
public class ConfroidUtils {

    /**
     *  Creates new version of the configuration identified by `name`.
     * @param context Target application context.
     * @param name Name of the configuration to load (without the application id as a prefix).
     * @param value Configuration to save.
     * @param versionName Optional tag to associated to the created version.
     */
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

            startService(context, intent);
        });
    }

    /**
     * Loads asynchronously the given `version` of the configuration identified by `name`.
     * @param context Target application context.
     * @param name Name of the configuration to load (without the application id as a prefix).
     * @param version Version number or tag name of the version to load (latest to load the last version).
     * @param callback Callback function called with the loaded configuration once resolved.
     * @param <T>
     */
    public static <T> void loadConfiguration(Context context, String name, String version, Consumer<T> callback) {
        withAuth(context, token -> {
            String appId = context.getApplicationContext().getPackageName();
            String requestId = UUID.randomUUID().toString();

            Intent intent = new Intent();
            intent.setAction(ConfroidIntents.INTENT_CONFIGURATION_PULLER);
            intent.setPackage(ConfroidIntents.PACKAGE_NAME);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

            intent.putExtra(ConfroidIntents.EXTRA_NAME, appId + "." + name);
            intent.putExtra(ConfroidIntents.EXTRA_TOKEN, token);
            intent.putExtra(ConfroidIntents.EXTRA_REQUEST_ID, requestId);
            intent.putExtra(ConfroidIntents.EXTRA_VERSION, version);
            intent.putExtra(ConfroidIntents.EXTRA_RECEIVER, requestId);
            intent.putExtra(ConfroidIntents.EXTRA_EXPIRATION, -1);

            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    context.unregisterReceiver(this);

                    Bundle content = intent.getBundleExtra(ConfroidIntents.EXTRA_CONTENT);
                    try {
                        callback.accept((T) BundleUtils.convertFromBundle(content));
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.accept(null);
                    }
                }
            }, new IntentFilter(requestId));

            startService(context, intent);
        });
    }

    /**
     * Gets all the versions of the configuration identified by `name`.
     * @param context Target application context.
     * @param name Name of the configuration to load (without the application id as a prefix).
     * @param callback Callback function called with the resolved versions.
     */
    public static void getConfigurationVersions(Context context, String name, Consumer<List<Version>> callback) {
        withAuth(context, token -> {
            String appId = context.getApplicationContext().getPackageName();
            String requestId = UUID.randomUUID().toString();

            Intent intent = new Intent();
            intent.setAction(ConfroidIntents.INTENT_CONFIGURATION_VERSIONS);
            intent.setPackage(ConfroidIntents.PACKAGE_NAME);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

            intent.putExtra(ConfroidIntents.EXTRA_NAME, appId + "." + name);
            intent.putExtra(ConfroidIntents.EXTRA_TOKEN, token);
            intent.putExtra(ConfroidIntents.EXTRA_REQUEST_ID, requestId);
            intent.putExtra(ConfroidIntents.EXTRA_RECEIVER, requestId);

            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    context.unregisterReceiver(this);

                    ArrayList<Version> versions = new ArrayList<>();

                    Bundle bundle = intent.getBundleExtra(ConfroidIntents.EXTRA_VERSIONS);
                    for (String name : bundle.keySet()) {
                        Bundle version = bundle.getBundle(name);
                        String tag = version.getString(ConfroidIntents.EXTRA_TAG, null);
                        Date date = (Date) version.getSerializable(ConfroidIntents.EXTRA_DATE);
                        versions.add(new Version(name, date, tag));
                    }

                    callback.accept(versions);
                }
            }, new IntentFilter(requestId));

            startService(context, intent);
        });
    }

    // TODO
    /**
     * Subscribes to the changes occurred to the configuration identified by `name`.
     * @param context Target application context.
     * @param name Name of the configuration to load (without the application id as a prefix).
     * @param callback Callback function called with the last value of the config after each change.
     * @param <T>
     */
    public static <T> void subscribeConfiguration(Context context, String name, Consumer<T> callback) {
        withAuth(context, token -> {
            Toast.makeText(context, token, Toast.LENGTH_LONG).show();
        });
    }

    // TODO
    /**
     *
     * @param context Target application context.
     * @param callback
     * @param <T>
     */
    public static <T> void cancelConfigurationSubscription(Context context, Consumer<T> callback) {
        withAuth(context, token -> {
            Toast.makeText(context, token, Toast.LENGTH_LONG).show();
        });
    }


    /**
     * Retrieves the authentication token of the application attached to the given `context`.
     * @param context Context that hold the application informations.
     * @param callback Function called once the token is resolved with the token.
     */
    private static void withAuth(Context context, Consumer<String> callback) {
        SharedPreferences preferences = context.getSharedPreferences(
            ConfroidUtils.class.getName(),
            Context.MODE_PRIVATE
        );

        String appId = context.getApplicationContext().getPackageName();
        String pref = appId + ".confroid-auth-token";
        String token = preferences.getString(pref, null);
        if (token != null) {
            callback.accept(token);
            return;
        }

        String receiver = UUID.randomUUID().toString();
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);

                String token = intent.getStringExtra(ConfroidIntents.EXTRA_TOKEN);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(pref, token);
                editor.apply();

                callback.accept(token);
            }
        }, new IntentFilter(receiver));

        Intent intent = new Intent();
        intent.setAction(ConfroidIntents.INTENT_TOKEN_DISPENSER);
        intent.setPackage(ConfroidIntents.PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra(ConfroidIntents.EXTRA_NAME, appId);
        intent.putExtra(ConfroidIntents.EXTRA_RECEIVER, receiver);
        context.sendBroadcast(intent);
    }

    private static void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent);
        } else {
            context.startService(intent);
        }
    }
}
