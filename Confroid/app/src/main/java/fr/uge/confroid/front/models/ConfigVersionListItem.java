package fr.uge.confroid.front.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import fr.uge.confroid.R;
import fr.uge.confroid.front.ConfigEditorActivity;
import fr.uge.confroid.storage.ConfroidPackage;

public class ConfigVersion {
    private final Context context;
    private final Drawable icon;
    private final ConfroidPackage confroidPackage;

    private ConfigVersion(Context context, ConfroidPackage confroidPackage, Drawable icon) {
        this.context = Objects.requireNonNull(context);
        this.confroidPackage = Objects.requireNonNull(confroidPackage);
        this.icon = Objects.requireNonNull(icon);
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
        ConfigEditorActivity.present(context, confroidPackage);
    }

    public static ConfigVersion create(Context context, ConfroidPackage confroidPackage) {
        String name = confroidPackage.getName();
        String id = name.substring(0, name.lastIndexOf("."));
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(id);
        } catch (Exception e) {
            icon = ContextCompat.getDrawable(context, R.mipmap.ic_android);
        }
        return new ConfigVersion(context, confroidPackage, icon);
    }
}
