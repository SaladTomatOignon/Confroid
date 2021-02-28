package fr.uge.confroid.front.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import java.util.Objects;
import java.util.function.Consumer;

import androidx.core.content.ContextCompat;
import fr.uge.confroid.R;

public class ConfigListItem {
    private final String name;
    private final Runnable onClick;
    private final Drawable icon;

    public ConfigListItem(String name, Drawable icon, Runnable onClick) {
        this.name = Objects.requireNonNull(name);
        this.onClick = Objects.requireNonNull(onClick);
        this.icon = Objects.requireNonNull(icon);
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void click() {
        onClick.run();
    }

    public static ConfigListItem fromConfigName(Context context, String name) {
        String id = name.substring(0, name.lastIndexOf("."));
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(id);
        } catch (Exception e) {
            icon = ContextCompat.getDrawable(context, R.mipmap.ic_android);
        }

        return new ConfigListItem(name, icon, () -> {
            Intent intent = new Intent();
            context.startActivity(intent);
        });
    }
}
