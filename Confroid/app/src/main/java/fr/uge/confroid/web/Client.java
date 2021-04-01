package fr.uge.confroid.web;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.crypto.SecretKey;

import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.settings.AppSettings;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroid.utils.CryptUtils;
import fr.uge.confroid.web.dto.CryptedConfroidPackage;

public class Client {
    private static final String SALT = "Confroid";

    private static Client INSTANCE;

    private final User user;
    private final SecretKey key;

    private final RequestQueue queue;

    Client(String username, String password, Context context) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        this.user = new User(username, CryptUtils.hash(CryptUtils.hash(password)));
        this.key = CryptUtils.getKeyFromPassword(CryptUtils.hash(password), SALT);

        this.queue = Volley.newRequestQueue(context);
    }

    /**
     * Initializes a new client to communicate with Confroid Storage Service.
     * Sends an authentication request and make available the client if the
     * authentication is successful.
     *
     * @param username The username of the client
     * @param password The password of the client
     * @param context A Context to use for sending requests
     * @param statusCallback Function called with 'true' as argument if the login succeed, and
     *                       'false' if the login failed. May be null
     */
    public static void initClient(String username, String password, Context context, Consumer<Boolean> statusCallback) {
        Client client = new Client(username, password, context);

        if (Objects.isNull(client.baseAddress()) || client.baseAddress().isEmpty()) {
            Log.w("Confroid Storage Service", "Base address is null or empty");
            if (!Objects.isNull(statusCallback)) {
                statusCallback.accept(false);
            }
            return;
        }

        client.login(
                (response) ->  {
                    Client.INSTANCE = client;
                    AppSettings.getINSTANCE().setConnected(true);
                    if (!Objects.isNull(statusCallback)) {
                        statusCallback.accept(true);
                    }
                },
                (error) -> {
                    Log.e("Confroid Storage Service", "Authentication failed");
                    AppSettings.getINSTANCE().setConnected(false);
                    Client.INSTANCE = null;
                    if (!Objects.isNull(statusCallback)) {
                        statusCallback.accept(false);
                    }
                }
        );
    }

    private String baseAddress() {
        return AppSettings.getINSTANCE().getBaseAddress();
    }

    /**
     * Send a request login to the web service
     * Asynchronous
     *
     * @param successCallback The authentication success callback
     * @param failCallback The authentication error callback
     */
    private void login(Response.Listener<JSONObject> successCallback, Response.ErrorListener failCallback) {
        String url = String.join("/", baseAddress(), "users", "login");
        JSONObject response = null;

        try {
            String requestPostJson = new Gson().toJson(user);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(requestPostJson), successCallback, failCallback);

            queue.add(request);
        } catch (Exception e) {
            Log.e("Error while sending authentication request to web service", e.getMessage());
        }
    }

    public static Client getInstance() {
        return INSTANCE;
    }

    /**
     * Upload a configuration to the web service.
     * Requires authentication.
     * Asynchronous.
     *
     * @param config The configuration to upload
     * @param name The name of the configuration (i.e package)
     * @param version The configuration version
     * @param date The date where the config has been created
     * @param listener The success callback
     * @param errorListener The error callback
     */
    public void sendConfig(Configuration config, String name, int version, LocalDateTime date, Response.Listener<CryptedConfroidPackage> listener, Response.ErrorListener errorListener) {
        sendConfig(config, name, version, null, date, listener, errorListener);
    }

    /**
     * Uploads a configuration to the web service.
     * Requires authentication.
     * Asynchronous.
     *
     * @param config The configuration to upload
     * @param name The name of the configuration (i.e package)
     * @param version The configuration version
     * @param tag The configuration tag
     * @param date The date where the config has been created
     * @param listener The success callback
     * @param errorListener The error callback
     */
    public void sendConfig(Configuration config, String name, int version, String tag, LocalDateTime date, Response.Listener<CryptedConfroidPackage> listener, Response.ErrorListener errorListener) {
        String encryptedConfig = CryptUtils.encrypt(config.toJson(), key);
        CryptedConfroidPackage pkg = new CryptedConfroidPackage(name, version, date.toString(), encryptedConfig, tag);

        String url = baseAddress() + "/configurations/";

        try {
            String requestPostJson = new Gson().toJson(pkg);
            JsonConfroidPackageRequest request = new JsonConfroidPackageRequest(Request.Method.PUT, url, new JSONObject(requestPostJson), listener, errorListener);
            queue.add(request);
        } catch (Exception e) {
            Log.e("Error while sending config to web service", e.getMessage() + " Name: " + name + " Version: " + version);
        }
    }

    /**
     * Retrieves a configuration from the web service, by its name and its version.
     * Requires authentication.
     * Asynchronous.
     *
     * @param name The name of the configuration (i.e package)
     * @param version The configuration version
     * @param listener The success callback
     * @param errorListener The error callback
     * @return True if the request has been sent, false otherwise
     */
    public boolean getConfig(String name, int version, Response.Listener<CryptedConfroidPackage> listener, Response.ErrorListener errorListener) {
        String url = String.join("/", baseAddress(), "configurations", name, String.valueOf(version));
        return sendGetRequest(url, listener, errorListener);
    }

    /**
     * Retrieves a configuration from the web service, by its name and its tag.
     * Requires authentication.
     * Asynchronous.
     *
     * @param name The name of the configuration (i.e package)
     * @param tag The configuration tag
     * @param listener The success callback
     * @param errorListener The error callback
     * @return True if the request has been sent, false otherwise
     */
    public boolean getConfig(String name, String tag, Response.Listener<CryptedConfroidPackage> listener, Response.ErrorListener errorListener) {
        String url = String.join("/", baseAddress(), "configurations", name, "tags", tag);
        return sendGetRequest(url, listener, errorListener);
    }

    /**
     * Retrieves all versions stored in the web service that correspond to the given name.
     * Requires authentication.
     * Asynchronous.
     *
     * @param name The name of the configurations
     * @param listener The success callback
     * @param errorListener The error callback
     * @return True if the request has been sent, false otherwise
     */
    public boolean getConfigsByName(String name, Response.Listener<List<CryptedConfroidPackage>> listener, Response.ErrorListener errorListener) {
        String url = String.join("/", baseAddress(), "configurations", name);
        return sendGetRequestList(url, listener, errorListener);
    }

    /**
     * Retrieves all versions stored in the web service.
     * Requires authentication.
     * Asynchronous.
     *
     * @param listener The success callback
     * @param errorListener The error callback
     * @return True if the request has been sent, false otherwise
     */
    public boolean getAllConfigs(Response.Listener<List<CryptedConfroidPackage>> listener, Response.ErrorListener errorListener) {
        String url = String.join("/", baseAddress(), "configurations/");
        return sendGetRequestList(url, listener, errorListener);
    }

    /**
     * Sends a GET request to the specified url.
     * Response json format should be a CryptedConfroidPackage
     * Asynchronous.
     *
     * @param url The url to send the GET request
     * @param listener The success callback
     * @param errorListener The error callback
     * @return True if the request has been sent, false otherwise
     */
    private boolean sendGetRequest(String url, Response.Listener<CryptedConfroidPackage> listener, Response.ErrorListener errorListener) {
        try {
            JsonConfroidPackageRequest request = new JsonConfroidPackageRequest(Request.Method.GET, url, null, listener, errorListener);
            queue.add(request);
        } catch (Exception e) {
            Log.e("Error while getting config from web service", e.getMessage() + " Address: " + url);
            return false;
        }

        return true;
    }

    /**
     * Sends a GET request to the specified url.
     * Response json format should be a list of CryptedConfroidPackage
     * Asynchronous.
     *
     * @param url The url to send the GET request
     * @param listener The success callback
     * @param errorListener The error callback
     * @return True if the request has been sent, false otherwise
     */
    private boolean sendGetRequestList(String url, Response.Listener<List<CryptedConfroidPackage>> listener, Response.ErrorListener errorListener) {
        try {
            JsonConfroidPackagesRequest request = new JsonConfroidPackagesRequest(Request.Method.GET, url, null, listener, errorListener);
            queue.add(request);
        } catch (Exception e) {
            Log.e("Error while getting config from web service", e.getMessage() + " Address: " + url);
            return false;
        }

        return true;
    }

    /**
     * Deletes all configs with the given name from the web service.
     *
     * @param name The configs name to delete
     * @param listener The success callback
     * @param errorListener The error callback
     * @return True if the request has been sent, false otherwise
     */
    public boolean deleteConfigs(String name, Response.Listener<CryptedConfroidPackage> listener, Response.ErrorListener errorListener) {
        String url = String.join("/", baseAddress(), "configurations", name);

        try {
            JsonConfroidPackageRequest request = new JsonConfroidPackageRequest(Request.Method.DELETE, url, null, listener, errorListener);
            queue.add(request);
        } catch (Exception e) {
            Log.e("Error while deleting configs from web service", e.getMessage() + " Address: " + url);
            return false;
        }

        return true;
    }

    /**
     * Deletes a config according to the name and the version given in arguments.
     *
     * @param name The config name to delete
     * @param version The version to delete
     * @param listener The success callback
     * @param errorListener The error callback
     * @return True if the request has been sent, false otherwise
     */
    public boolean deleteConfig(String name, int version, Response.Listener<CryptedConfroidPackage> listener, Response.ErrorListener errorListener) {
        String url = String.join("/", baseAddress(), "configurations", name, String.valueOf(version));

        try {
            JsonConfroidPackageRequest request = new JsonConfroidPackageRequest(Request.Method.DELETE, url, null, listener, errorListener);
            queue.add(request);
        } catch (Exception e) {
            Log.e("Error while deleting config from web service", e.getMessage() + " Address: " + url);
            return false;
        }

        return true;
    }

    /**
     * Decrypts the given configuration using the client secret key.
     *
     * @param cryptedConfig The crypted configuration
     * @return The uncrypted configuration
     */
    public Configuration decryptConfig(String cryptedConfig) {
        String decryptedConfig = CryptUtils.decrypt(cryptedConfig, key);
        return Configuration.fromJson(decryptedConfig);
    }

    /**
     * Decrypts the given package using the client secret key.
     *
     * @param cryptedPackage The crypted package
     * @return The uncrypted package, or null if decryption failed.
     */
    public ConfroidPackage decryptPackage(CryptedConfroidPackage cryptedPackage) {
        Date convertedDatetime = Date.from(
                LocalDateTime.parse(cryptedPackage.getCreationDate())
                        .atZone(ZoneId.systemDefault()).toInstant()
        );

        try {
            return new ConfroidPackage(
                    cryptedPackage.getName(),
                    cryptedPackage.getVersion(),
                    convertedDatetime,
                    decryptConfig(cryptedPackage.getConfig()),
                    cryptedPackage.getTag()
            );
        } catch (Exception e) {
            return null;
        }
    }
}
