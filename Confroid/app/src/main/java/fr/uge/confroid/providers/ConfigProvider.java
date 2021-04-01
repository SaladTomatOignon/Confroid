package fr.uge.confroid.providers;

import android.content.Context;

import java.util.List;
import java.util.function.Consumer;

import fr.uge.confroid.storage.ConfroidPackage;

public interface ConfigProvider {

    /**
     * Retrieves the list of all configurations names.
     * Asynchronous
     *
     * @param context A context to retrieve data
     * @param callback The function called when data is retrieved
     * @param errorCallback The function called if data recovery failed
     */
    void getNames(Context context, Consumer<List<String>> callback, Consumer<String> errorCallback);

    /**
     * Retrieves the list of all Confroid packages associated to the given name.
     * Asynchronous
     *
     * @param name The name of the packages to retrieve
     * @param context A context to retrieve data
     * @param callback The function called when data is retrieved
     * @param errorCallback The function called if data recovery failed
     */
    void getPackagesByName(String name, Context context, Consumer<List<ConfroidPackage>> callback, Consumer<String> errorCallback);

    /**
     * Saves the package to where it has been retrieved.
     *
     * @param context A context to save data
     * @param confroidPackage The package to save
     * @param successCallback The function called when data is saved successfully
     * @param errorCallback The function called if saving failed
     */
    void savePackage(Context context, ConfroidPackage confroidPackage, Consumer<String> successCallback, Consumer<String> errorCallback);

    /**
     * Removes the package to where it has been retrieved.
     *
     * @param context A context to remove data
     * @param confroidPackage The package to remove
     * @param successCallback The function called when data is removed successfully
     * @param errorCallback The function called if removing failed
     */
    void removePackage(Context context, ConfroidPackage confroidPackage, Consumer<String> successCallback, Consumer<String> errorCallback);
}
