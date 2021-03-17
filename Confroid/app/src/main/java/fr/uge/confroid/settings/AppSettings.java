package fr.uge.confroid.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AppSettings {
    private static AppSettings INSTANCE;

    /* General */
    private Language language;

    /* Cloud access */
    /* Connexion */
    private String baseAddress;
    private String login;
    private String password;
    private boolean connected;

    /* Data synchronization */
    private boolean enableDataSync;
    private int repeatInterval;
    private TimeUnit repeatIntervalTimeUnit;
    private boolean allowCellularData;
    private boolean requiresCharging;

    public static void loadSettings(Context ctx) {
        AppSettings settings = new AppSettings();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);

        String language = preferences.getString("language", "").toUpperCase();
        settings.setLanguage(Language.valueOf(language));
        settings.setBaseAddress(preferences.getString("base_address", ""));
        settings.setLogin(preferences.getString("login", ""));
        settings.setPassword(preferences.getString("password", ""));
        settings.setEnableDataSync(preferences.getBoolean("enable_data_sync", true));
        settings.setRepeatIntervalTimeUnit(TimeUnit.DAYS);
        switch (preferences.getString("upload_interval", "")) {
            case "day":
                settings.setRepeatInterval(1);
                break;
            case "week":
                settings.setRepeatInterval(7);
                break;
            case "month":
                settings.setRepeatInterval(30);
                break;
            case "year":
                settings.setRepeatInterval(365);
                break;
        }
        settings.setAllowCellularData(preferences.getBoolean("allow_cellular_data", false));
        settings.setRequiresCharging(preferences.getBoolean("requires_charging", false));

        INSTANCE = settings;
    }

    public static AppSettings getINSTANCE() {
        return INSTANCE;
    }

    public Language getLanguage() {
        return Objects.isNull(language) ? Language.EN : language;
    }

    public void setLanguage(Language language) {
        this.language = Objects.requireNonNull(language);
    }

    public String getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isEnableDataSync() {
        return enableDataSync;
    }

    public void setEnableDataSync(boolean enableDataSync) {
        this.enableDataSync = enableDataSync;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public TimeUnit getRepeatIntervalTimeUnit() {
        return Objects.isNull(repeatIntervalTimeUnit) ? TimeUnit.DAYS : repeatIntervalTimeUnit;
    }

    public void setRepeatIntervalTimeUnit(TimeUnit repeatIntervalTimeUnit) {
        this.repeatIntervalTimeUnit = Objects.requireNonNull(repeatIntervalTimeUnit);
    }

    public boolean isAllowCellularData() {
        return allowCellularData;
    }

    public void setAllowCellularData(boolean allowCellularData) {
        this.allowCellularData = allowCellularData;
    }

    public boolean isRequiresCharging() {
        return requiresCharging;
    }

    public void setRequiresCharging(boolean requiresCharging) {
        this.requiresCharging = requiresCharging;
    }
}
