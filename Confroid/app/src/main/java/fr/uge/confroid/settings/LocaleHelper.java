package fr.uge.confroid.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper {

    /**
     * Set the application language at runtime.
     *
     * @param context A context to update resources
     * @param language An ISO 639 alpha-2 or alpha-3 language code, or a language subtag
     *                 up to 8 characters in length. See the Locale class description about
     *                 valid language values
     * @return A Context with the given configuration override
     */
    public static Context setLocale(Context context, String language) {
        // Updating the language for devices above Android Nougat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }

        // For devices having lower version of Android
        return updateResourcesLegacy(context, language);
    }

    /**
     * Set the application language at runtime.
     * For versions above Android Nougat
     *
     * @param context A context to update resources
     * @param language An ISO 639 alpha-2 or alpha-3 language code, or a language subtag
     *                 up to 8 characters in length. See the Locale class description about
     *                 valid language values
     * @return A Context with the given configuration override
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }


    /**
     * Set the application language at runtime.
     * For versions below Android Nougat
     *
     * @param context A context to update resources
     * @param language An ISO 639 alpha-2 or alpha-3 language code, or a language subtag
     *                 up to 8 characters in length. See the Locale class description about
     *                 valid language values
     * @return A Context with the given configuration override
     */
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}
