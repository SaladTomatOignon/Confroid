package fr.uge.confroid.providers;

import android.content.Context;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import fr.uge.confroid.storage.ConfroidDatabase;
import fr.uge.confroid.storage.ConfroidPackage;

public class DatabaseProvider implements ConfigProvider {
    private static DatabaseProvider INSTANCE;

    private DatabaseProvider() {

    }

    public static DatabaseProvider getInstance() {
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new DatabaseProvider();
        }

        return INSTANCE;
    }

    @Override
    public void getNames(Context context, Consumer<List<String>> callback, Consumer<String> errorCallback) {
        try {
            ConfroidDatabase.exec(context, dao -> callback.accept(dao.findAllNames()));
        } catch (Exception e) {
            if (!Objects.isNull(errorCallback)) {
                errorCallback.accept(e.getMessage());
            }
        }
    }

    @Override
    public void getPackagesByName(String name, Context context, Consumer<List<ConfroidPackage>> callback, Consumer<String> errorCallback) {
        try {
            ConfroidDatabase.exec(context, dao -> callback.accept(dao.findAllVersions(name)));
        } catch (Exception e) {
            if (!Objects.isNull(errorCallback)) {
                errorCallback.accept(e.getMessage());
            }
        }
    }

    @Override
    public void savePackage(Context context, ConfroidPackage confroidPackage, Consumer<String> successCallback, Consumer<String> errorCallback) {
        try {
            ConfroidDatabase.exec(context, dao -> dao.update(confroidPackage));
            if (!Objects.isNull(successCallback)) {
                successCallback.accept("Ok");
            }
        } catch (Exception e) {
            if (!Objects.isNull(errorCallback)) {
                errorCallback.accept(e.getMessage());
            }
        }
    }

    @Override
    public void removePackage(Context context, ConfroidPackage confroidPackage, Consumer<String> successCallback, Consumer<String> errorCallback) {
        try {
            ConfroidDatabase.exec(context, dao -> dao.delete(confroidPackage));
            if (!Objects.isNull(successCallback)) {
                successCallback.accept("Ok");
            }
        } catch (Exception e) {
            if (!Objects.isNull(errorCallback)) {
                errorCallback.accept(e.getMessage());
            }
        }
    }
}
