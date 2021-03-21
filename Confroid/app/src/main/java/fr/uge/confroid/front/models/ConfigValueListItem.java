package fr.uge.confroid.front.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.widget.EditText;

import java.util.Objects;
import java.util.function.Consumer;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroidlib.BundleUtils;

/**
 * Representation of a data shown inside
 * {@link fr.uge.confroid.front.holders.ConfigValueListItemHolder}.
 */
public class ConfigValueListItem {
    private final Context context;
    private final String name;
    private final String preview;
    private final Drawable icon;
    private final Value value;

    private Runnable editListener;
    private Runnable deleteListener;
    private Consumer<String> renameListener;

    private ConfigValueListItem(
        Context context,
        String name,
        String preview,
        Drawable icon,
        Value value
    ) {
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(name);
        this.icon = Objects.requireNonNull(icon);
        this.preview = Objects.requireNonNull(preview);
        this.value = Objects.requireNonNull(value);
    }

    public boolean canBeRenamed() {
        return renameListener != null;
    }

    public boolean canBeDeleted() {
        return deleteListener != null;
    }


    public void edit() {
        if (editListener == null) {
            throw new AssertionError("Missing editListener");
        }

        editListener.run();
    }

    public void delete() {
        if (renameListener == null) {
            throw new AssertionError("Missing renameListener");
        }

        deleteListener.run();
    }

    public void rename() {
        if (renameListener == null) {
            throw new AssertionError("Missing renameListener");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_rename);

        final EditText input = new EditText(context);
        input.setText(name);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.label_rename, (dialog, which) -> {
            String newName = input.getText().toString();
            if (!newName.isEmpty() && !newName.equals(name)) {
                renameListener.accept(newName);
            }
        });
        builder.setNegativeButton(R.string.label_cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }


    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return value.valueType().name();
    }

    public String getPreview() {
        return preview;
    }


    public void setOnEditListener(Runnable editListener) {
        this.editListener = editListener;
    }

    public void setOnDeleteListener(Runnable deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setOnRenameListener(Consumer<String> renameListener) {
        this.renameListener = renameListener;
    }


    public static ConfigValueListItem create(Context context, String name, Value value) {
        Drawable icon;
        String preview = value.toString();
        switch (value.valueType()) {
            case MAP:
                icon = ContextCompat.getDrawable(context, R.drawable.ic_type_object);
                int size = Configuration.filterKeywords(value).getMap().size();
                preview = size + " keys";
                break;
            case ARRAY:
                icon = ContextCompat.getDrawable(context, R.drawable.ic_type_array);
                int length = Configuration.filterKeywords(value).getArray().length;
                preview = length + " items";
                break;
            case STRING:
                icon = ContextCompat.getDrawable(context, R.drawable.ic_type_text);
                break;
            case BOOLEAN:
                icon = ContextCompat.getDrawable(context, R.drawable.ic_type_boolean);
                break;
            default:
                icon = ContextCompat.getDrawable(context, R.drawable.ic_type_number);
                break;
        }

        return new ConfigValueListItem(context, name, preview, icon, value);
    }
}
