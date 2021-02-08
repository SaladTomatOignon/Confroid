package fr.uge.confroid.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;
import java.util.UUID;

/**
 * Stores application authentication tokens.
 */
public class AuthToken {
    private final Context context;

    public AuthToken(Context context) {
        this.context = Objects.requireNonNull(context);
    }

    /**
     * Gets the authentification token for the app identified by the given appId.
     *
     * Note:
     * If the token does not exists, this method will generate a random one.
     *
     * @param appId An application identifier.
     * @return An existing token or a new generated token.
     */
    public String get(String appId) {
        if (appId == null || appId.isEmpty()) {
            throw new IllegalArgumentException("argument 'appId' is required");
        }

        SharedPreferences preferences = context.getSharedPreferences(
            getClass().getName(),
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

}
