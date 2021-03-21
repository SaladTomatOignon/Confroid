package fr.uge.confroid.front.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.Objects;

import androidx.core.content.ContextCompat;
import fr.uge.confroid.R;
import fr.uge.confroid.front.ConfigVersionsActivity;

public class ConfigNameListItem {
    private final Context context;
    private final String name;
    private final Drawable icon;

    private ConfigNameListItem(Context context, String name, Drawable icon) {
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(name);
        this.icon = Objects.requireNonNull(icon);
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void click() {
        ConfigVersionsActivity.present(context, name);
    }

    public static ConfigNameListItem create(Context context, String name) {
        String id = name.substring(0, name.lastIndexOf("."));
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(id);
        } catch (Exception e) {
            icon = ContextCompat.getDrawable(context, R.mipmap.ic_android);
        }

        return new ConfigNameListItem(context, name, icon);
    }
}
