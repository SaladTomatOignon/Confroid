package fr.uge.confroid.web;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.crypto.SecretKey;

import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.utils.CryptUtils;

public class Client {
    private static final String SALT = "Confroid";
    private static final String BASE_ADDRESS = "http://xxx.xxx.xxx.xxx:8080/";

    private final UserInfos userInfos;
    private final SecretKey key;
    private final Context context;

    public Client(String username, String password, Context context) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        this.context = Objects.requireNonNull(context);

        this.userInfos = new UserInfos(username, CryptUtils.hash(CryptUtils.hash(password)));
        this.key = CryptUtils.getKeyFromPassword(CryptUtils.hash(password), SALT);
    }

    /**
     * Upload a configuration to the web service.
     * Requires authentication.
     * Asynchronous.
     *
     * @param config The configuration to upload
     * @param name The name of the configuration (i.e package)
     * @param version The configuration version
     */
    public void sendConfig(Configuration config, String name, String version) {
        String encryptedConfig = CryptUtils.encrypt(config.toJson(), key);
        ConfigurationContext configCtx = new ConfigurationContext(name, version, encryptedConfig);
        RequestContext requestCtx = new RequestContext(userInfos, configCtx);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = BASE_ADDRESS + "saveConfig";

        try {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();

            String requestPostJson = new Gson().toJson(requestCtx);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requestPostJson), future, future);
            queue.add(request);
        } catch (Exception e) {
            Log.e("Error while sending config to web service", e.getMessage() + " Name: " + name + " Version: " + version);
        }
    }

    /**
     * Retrieve a configuration from the web service.
     * Requires authentication.
     * Synchronous.
     *
     * @param name The name of the configuration (i.e package)
     * @param version The configuration version
     * @return The configuration or null if not found
     */
    public Configuration getConfig(String name, String version) {
        RequestContext requestCtx = new RequestContext(userInfos, new ConfigurationContext(name, version, null));
        ConfigurationContext configCtx = null;

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = BASE_ADDRESS + "getConfig";

        try {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();

            String requestPostJson = new Gson().toJson(requestCtx);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requestPostJson), future, future);
            queue.add(request);

            JSONObject response = future.get();
            configCtx = new Gson().fromJson(response.toString(), ConfigurationContext.class);
        } catch (Exception e) {
            Log.e("Error while getting config from web service", e.getMessage() + " Name: " + name + " Version: " + version);

            return null;
        }

        String decryptedConfig = CryptUtils.decrypt(configCtx.getConfig(), key);
        return Configuration.fromJson(decryptedConfig);
    }
}
