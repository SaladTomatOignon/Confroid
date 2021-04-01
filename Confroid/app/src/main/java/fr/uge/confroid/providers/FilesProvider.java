package fr.uge.confroid.providers;

import android.content.Context;
import android.net.Uri;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import fr.uge.confroid.R;
import fr.uge.confroid.settings.AppSettings;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroid.storage.ConfroidStorage;

public class FilesProvider implements ConfigProvider {
    private static FilesProvider INSTANCE;

    private FilesProvider() {

    }

    public static FilesProvider getInstance() {
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new FilesProvider();
        }

        return INSTANCE;
    }

    @Override
    public void getNames(Context context, Consumer<List<String>> callback, Consumer<String> errorCallback) {
        Uri fileUri = AppSettings.getINSTANCE().getConfigFilePath();
        if (Objects.isNull(fileUri) && !Objects.isNull(errorCallback)) {
            errorCallback.accept(context.getResources().getString(R.string.file_not_imported));
            return;
        }

        try {
            callback.accept(Lists.newArrayList(
                    ConfroidStorage.readConfigs(fileUri, context).keySet()
            ));
        } catch (IOException e) {
            if (!Objects.isNull(errorCallback)) {
                errorCallback.accept(context.getResources().getString(R.string.file_read_error));
            }
        }
    }

    @Override
    public void getPackagesByName(String name, Context context, Consumer<List<ConfroidPackage>> callback, Consumer<String> errorCallback) {
        Uri fileUri = AppSettings.getINSTANCE().getConfigFilePath();
        if (Objects.isNull(fileUri) && !Objects.isNull(errorCallback)) {
            errorCallback.accept(context.getResources().getString(R.string.file_not_imported));
            return;
        }

        try {
            callback.accept(Lists.newArrayList(
                    ConfroidStorage.readConfigs(fileUri, context).get(name).values()
            ));
        } catch (IOException e) {
            if (!Objects.isNull(errorCallback)) {
                errorCallback.accept(context.getResources().getString(R.string.file_read_error));
            }
        }
    }

    @Override
    public void savePackage(Context context, ConfroidPackage confroidPackage, Consumer<String> successCallback, Consumer<String> errorCallback) {
        /*
         * We need to re-ask to the user to re open the file in order to grant authorization.
         * This is tedious for the user so we choose to don't save the file automaticaly at the end
         * of the edition. But the user can still update its configuration with the button on the UI.
         */
    }

    @Override
    public void removePackage(Context context, ConfroidPackage confroidPackage, Consumer<String> successCallback, Consumer<String> errorCallback) {
        Uri fileUri = AppSettings.getINSTANCE().getConfigFilePath();
        if (Objects.isNull(fileUri) && !Objects.isNull(errorCallback)) {
            errorCallback.accept(context.getResources().getString(R.string.file_not_imported));
            return;
        }

        try {
            ConfroidStorage.deleteConfig(confroidPackage.getName(), confroidPackage.getVersion(), fileUri, context);
            if (!Objects.isNull(successCallback)) {
                successCallback.accept("Ok");
            }
        } catch (IOException e) {
            if (!Objects.isNull(errorCallback)) {
                errorCallback.accept(context.getResources().getString(R.string.file_read_error));
            }
        }
    }
}
