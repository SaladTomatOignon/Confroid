package fr.uge.confroid.front.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.Date;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import fr.uge.confroid.R;
import fr.uge.confroid.front.ConfigVersionsActivity;
import fr.uge.confroid.storage.ConfroidPackage;

public class ConfigVersionItem {
    private final Context context;
    private final ConfroidPackage pkg;
    private final Drawable icon;

    private ConfigVersionItem(Context context, ConfroidPackage pkg, Drawable icon) {
        this.context = Objects.requireNonNull(context);
        this.pkg = Objects.requireNonNull(pkg);
        this.icon = Objects.requireNonNull(icon);
    }

    public String getVersion() {
        int version = pkg.getVersion();
        String tag = pkg.getTag();
        if (tag == null) {
            return "V" + version;
        }
        return "V" + version + " (" + tag + ")";
    }

    public String getDate() {
        return pkg.getDate().toString();
    }

    public Drawable getIcon() {
        return icon;
    }

    public void click() {
       // ConfigVersionsActivity.present(context, name);
    }

    public static ConfigVersionItem create(Context context, ConfroidPackage pkg) {
        String name = pkg.getName();
        String id = name.substring(0, name.lastIndexOf("."));
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(id);
        } catch (Exception e) {
            icon = ContextCompat.getDrawable(context, R.mipmap.ic_android);
        }
        return new ConfigVersionItem(context, pkg, icon);
    }
}
