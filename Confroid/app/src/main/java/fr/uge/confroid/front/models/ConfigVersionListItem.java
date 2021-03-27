package fr.uge.confroid.front.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import fr.uge.confroid.R;
import fr.uge.confroid.front.ConfigEditorActivity;
import fr.uge.confroid.providers.ProviderType;
import fr.uge.confroid.storage.ConfroidPackage;

public class ConfigVersionListItem {
    private final Context context;
    private final Drawable icon;
    private final ConfroidPackage confroidPackage;
    private final ProviderType providerType;

    private ConfigVersionListItem(Context context, ConfroidPackage confroidPackage, Drawable icon, ProviderType providerType) {
        this.context = Objects.requireNonNull(context);
        this.confroidPackage = Objects.requireNonNull(confroidPackage);
        this.icon = Objects.requireNonNull(icon);
        this.providerType = Objects.requireNonNull(providerType);
    }

    public String getVersion() {
        int version = confroidPackage.getVersion();
        String tag = confroidPackage.getTag();
        if (tag == null) {
            return "V" + version;
        }
        return "V" + version + " (" + tag + ")";
    }

    public String getDate() {
        return confroidPackage.getDate().toString();
    }

    public Drawable getIcon() {
        return icon;
    }

    public void click() {
        ConfigEditorActivity.present(context, confroidPackage, providerType);
    }

    public static ConfigVersionListItem create(Context context, ConfroidPackage confroidPackage, ProviderType providerType) {
        String name = confroidPackage.getName();
        String id = name.substring(0, name.lastIndexOf("."));
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(id);
        } catch (Exception e) {
            icon = ContextCompat.getDrawable(context, R.mipmap.ic_android);
        }
        return new ConfigVersionListItem(context, confroidPackage, icon, providerType);
    }
}
