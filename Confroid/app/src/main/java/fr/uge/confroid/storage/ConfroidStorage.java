package fr.uge.confroid.storage;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.uge.confroid.configuration.ConfigDeserializer;
import fr.uge.confroid.configuration.ConfigSerializer;
import fr.uge.confroid.configuration.Configuration;

public class ConfroidStorage {

    /**
     * Retrieves and parses a file containing configurations.
     * The specified file must respect the Confroid format.
     *
     * @param uri The URI to the file to read
     * @param context A context to perform I/O operations
     * @return A map where the key is a name and the value contains itself a map
     * where the key is the version and the value the Confroid package
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, Map<Integer, ConfroidPackage>> readConfigs(Uri uri, Context context) throws IOException {
        Map<String, Map<Integer, TagDateConfig>> collection;

        if (!new File(uri.getPath()).exists()) {
            return new HashMap<>();
        }

        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            Type pkgCollections = new TypeToken<Map<String, Map<Integer, TagDateConfig>>>(){}.getType();
            Gson gson = new GsonBuilder().registerTypeAdapter(Configuration.class, new ConfigDeserializer()).create();
            collection = gson.fromJson(new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())), pkgCollections);
        }

        return collection.entrySet().stream().map(
                ConfroidStorage::createNamePkgEntry
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Retrieves and parses a file containing configurations.
     * The specified file must respect the Confroid format.
     *
     * The difference with {@link #readConfigs} is that a ConfroidPackage is not reinstancied
     * for each configuration.
     *
     * @param uri The desired URI
     * @param context A context to perform I/O operations
     * @return A map where the key is a name and the value contains itself a map
     * where the key is the version and the value the configuration infos
     * @throws IOException if an I/O error occurs
     */
    private static Map<String, Map<Integer, TagDateConfig>> readRawConfigs(Uri uri, Context context) throws IOException {
        if (!new File(uri.getPath()).exists()) {
            return new HashMap<>();
        }

        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            Type pkgCollections = new TypeToken<Map<String, Map<Integer, TagDateConfig>>>(){}.getType();
            Gson gson = new GsonBuilder().registerTypeAdapter(Configuration.class, new ConfigDeserializer()).create();
            return gson.fromJson(new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())), pkgCollections);
        }
    }

    /**
     * Converts from
     * Map.Entry<String, Map<Integer, TagDateConfig>> to
     * Map.Entry<String, Map<Integer, ConfroidPackage>>
     *
     * @param namePkg The value to convert
     * @return The value converted
     */
    private static Map.Entry<String, Map<Integer, ConfroidPackage>> createNamePkgEntry(Map.Entry<String, Map<Integer, ConfroidStorage.TagDateConfig>> namePkg) {
        return new AbstractMap.SimpleEntry<String, Map<Integer, ConfroidPackage>>(namePkg.getKey(),
                namePkg.getValue().entrySet().stream().map(
                        versionPkg -> getVersionPkgEntry(versionPkg, namePkg.getKey())
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    /**
     * Converts from
     * Map.Entry<Integer, TagDateConfig> to
     * Map.Entry<Integer, ConfroidPackage>
     *
     * @param versionPkg The value to convert
     * @param namePkg The name of the package
     * @return The value converted
     */
    private static Map.Entry<Integer, ConfroidPackage> getVersionPkgEntry(Map.Entry<Integer, ConfroidStorage.TagDateConfig> versionPkg, String namePkg) {
        return new AbstractMap.SimpleEntry<Integer, ConfroidPackage>(versionPkg.getKey(),
                new ConfroidPackage(namePkg, versionPkg.getKey(), versionPkg.getValue().getDate(), versionPkg.getValue().getConfiguration(), versionPkg.getValue().getTag())
        );
    }

    /**
     * Saves the given configuration in a file that may contain more configurations.
     * If the version of the package already exists, the existing package is updated.
     *
     * @param pkg The package to save
     * @param uri The URI to the file to write
     * @param context A context to perform I/O operations
     * @return True if the package already existed and has been updated, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public static boolean writeConfig(ConfroidPackage pkg, Uri uri, Context context) throws IOException {
        Map<String, Map<Integer, TagDateConfig>> collection = readRawConfigs(uri, context);
        boolean valueUpdated = addPackageToCollection(collection, pkg);

        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Configuration.class, new ConfigSerializer()).create();
            outputStream.write(gson.toJson(collection).getBytes(StandardCharsets.UTF_8.name()));
        }

        return valueUpdated;
    }

    /**
     * Add a package to the collection.
     * If the version of the package already exists, the existing package is updated.
     *
     * @param collection The collection where to save the package
     * @param pkg The package to save
     * @return True if the package already existed and has been updated, false otherwise
     */
    private static boolean addPackageToCollection(Map<String, Map<Integer, TagDateConfig>> collection, ConfroidPackage pkg) {
        TagDateConfig newTdc = new TagDateConfig(pkg.getTag(), pkg.getDate(), pkg.getConfig());

        if (collection.containsKey(pkg.getName())) {
            Map<Integer, TagDateConfig> versionPkg = collection.get(pkg.getName());
            if (versionPkg.containsKey(pkg.getVersion())) {
                versionPkg.replace(pkg.getVersion(), newTdc);
                return true;
            } else {
                versionPkg.put(pkg.getVersion(), newTdc);
                return false;
            }
        } else {
            HashMap<Integer, TagDateConfig> versionPkg = new HashMap<>();
            versionPkg.put(pkg.getVersion(), newTdc);
            collection.put(pkg.getName(), versionPkg);
            return false;
        }
    }

    private static class TagDateConfig {
        private final String tag;
        private final Date date;
        private final Configuration configuration;

        public TagDateConfig(String tag, Date date, Configuration configuration) {
            this.tag = tag;
            this.date = date;
            this.configuration = configuration;
        }

        public String getTag() {
            return tag;
        }

        public Date getDate() {
            return date;
        }

        public Configuration getConfiguration() {
            return configuration;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TagDateConfig that = (TagDateConfig) o;
            return Objects.equals(tag, that.tag) &&
                    date.equals(that.date) &&
                    configuration.equals(that.configuration);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tag, date, configuration);
        }
    }
}
