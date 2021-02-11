package fr.uge.confroid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.uge.confroid.utils.AuthUtils;
import fr.uge.confroidlib.ConfroidIntents;

/**
 * Generates auth token for applications using Confroid.
 *
 * This receiver should be called with an intent containing the following informations:
 *
 * - name: String -> the name of the application for which the token should be generated.
 * - receiver: String -> A name of a service to which the result will be broadcasted using sendBroadcast method.
 */
public class TokenDispenser extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra(ConfroidIntents.EXTRA_NAME);
        String receiver = intent.getStringExtra(ConfroidIntents.EXTRA_RECEIVER);
        if (name != null && receiver != null) {
            Intent response = new Intent(receiver);
            response.putExtra(ConfroidIntents.EXTRA_TOKEN, AuthUtils.forApp(context, name));
            context.sendBroadcast(response);
        }
    }
}
