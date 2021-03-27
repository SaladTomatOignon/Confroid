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
        ConfroidDatabase.exec(context, dao -> callback.accept(dao.findAllNames()));
    }

    @Override
    public void getPackagesByName(String name, Context context, Consumer<List<ConfroidPackage>> callback, Consumer<String> errorCallback) {
        ConfroidDatabase.exec(context, dao -> callback.accept(dao.findAllVersions(name)));
    }

    @Override
    public void savePackage(Context context, ConfroidPackage confroidPackage, Consumer<String> successCallback, Consumer<String> errorCallback) {
        ConfroidDatabase.exec(context, dao -> dao.update(confroidPackage));
        successCallback.accept("Ok");
    }
}
