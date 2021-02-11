package fr.uge.confroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Stores application authentication tokens.
 */
public final class AuthUtils {
    /**
     * Gets the authentification token for the app identified by the given appId.
     *
     * Note:
     * If the token does not exists, this method will generate a random one.
     *
     * @param context Application context.
     * @param appId An application identifier.
     * @return An existing token for the application or a new generated token.
     */
    public static String forApp(Context context, String appId) {
        if (context == null) {
            throw new IllegalArgumentException("argument 'context' is required");
        }

        if (appId == null || appId.isEmpty()) {
            throw new IllegalArgumentException("argument 'appId' is required");
        }

        SharedPreferences preferences = context.getSharedPreferences(
        "AuthUtils",
            Context.MODE_PRIVATE
        );

        String key = appId + ".token";
        String token = preferences.getString(key, null);
        if (token == null) {
            token = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, token);
            editor.apply();
        }
        return token;
    }

    public static boolean verifyToken(Context context, String appId, String token) {
        return forApp(context, appId).equals(token);
    }
}
