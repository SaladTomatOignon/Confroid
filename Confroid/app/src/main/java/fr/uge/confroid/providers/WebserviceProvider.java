package fr.uge.confroid.providers;

import android.content.Context;
import android.util.Log;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import fr.uge.confroid.settings.AppSettings;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroid.web.Client;
import fr.uge.confroid.web.dto.CryptedConfroidPackage;

import fr.uge.confroid.R;

public class WebserviceProvider implements ConfigProvider {
    private static WebserviceProvider INSTANCE;
    private static final String TAG = "Cloud Provider";

    private WebserviceProvider() {

    }

    public static WebserviceProvider getInstance() {
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new WebserviceProvider();
        }

        return INSTANCE;
    }

    @Override
    public void getNames(Context context, Consumer<List<String>> callback, Consumer<String> errorCallback) {
        if (Objects.isNull(Client.getInstance())) {
            errorCallback.accept(context.getResources().getString(R.string.web_client_not_connected));
            return;
        }

        Client.getInstance().getAllConfigs(
                (cryptedList) ->
                        callback.accept(cryptedList.stream()
                                .map(CryptedConfroidPackage::getName)
                                .collect(Collectors.toList())),
                (error) ->
                        Log.w(TAG, error));
    }

    @Override
    public void getPackagesByName(String name, Context context, Consumer<List<ConfroidPackage>> callback, Consumer<String> errorCallback) {
        if (!AppSettings.getINSTANCE().isConnected()) {
            errorCallback.accept(context.getResources().getString(R.string.web_client_not_connected));
            return;
        }

        Client.getInstance().getConfigsByName(name,
                (cryptedList) ->
                        callback.accept(cryptedList.stream()
                        .map(cryptedPackage -> Client.getInstance().decryptPackage(cryptedPackage))
                        .collect(Collectors.toList())),
                (error) ->
                        Log.w(TAG, error));
    }

    @Override
    public void savePackage(Context context, ConfroidPackage confroidPackage, Consumer<String> successCallback, Consumer<String> errorCallback) {
        if (!AppSettings.getINSTANCE().isConnected()) {
            errorCallback.accept(context.getResources().getString(R.string.web_client_not_connected));
            return;
        }

        Client.getInstance().sendConfig(
                confroidPackage.getConfig(),
                confroidPackage.getName(),
                confroidPackage.getVersion(),
                confroidPackage.getTag(),
                confroidPackage.getDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                response -> successCallback.accept("Ok"),
                error -> errorCallback.accept(error.getMessage())
        );
    }
}
