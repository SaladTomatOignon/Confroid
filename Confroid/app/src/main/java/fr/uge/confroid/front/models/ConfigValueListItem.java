package fr.uge.confroid.front.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.widget.EditText;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroidlib.annotations.Description;

/**
 * Representation of a data shown inside
 * {@link fr.uge.confroid.front.holders.ConfigValueListItemHolder}.
 */
public class ConfigValueListItem {
    private final Context context;
    private final String name;
    private final Value value;
    private final String description;

    private Runnable editListener;
    private Runnable deleteListener;
    private Consumer<String> renameListener;

    private ConfigValueListItem(Context context, String name, Value value, String description) {
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
        this.description = description;

    }

    /**
     * Gets a value indicating whether the item can be renamed.
     */
    public boolean canBeRenamed() {
        return renameListener != null;
    }

    /**
     * Gets a value indicating whether the item can be deleted.
     */
    public boolean canBeDeleted() {
        return deleteListener != null;
    }


    public boolean hasDescription() {
        return description != null;
    }


    /**
     * Push the item value to the editor history.
     */
    public void edit() {
        if (editListener == null) {
            throw new AssertionError("Missing editListener");
        }

        editListener.run();
    }

    /**
     * Deletes the item.
     */
    public void delete() {
        if (deleteListener == null) {
            throw new AssertionError("Missing deleteListener");
        }

        deleteListener.run();
    }

    /**
     * Show an {@link AlertDialog} to ask the user to enter new
     * name for the item and edit the name if possible.
     */
    public void rename() {
        if (renameListener == null) {
            throw new AssertionError("Missing renameListener");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_dialog_rename_value);

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
        return ContextCompat.getDrawable(context, value.drawable());
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return value.valueType().name();
    }

    public String getPreview() {
        return value.preview();
    }

    public String getDescription() {
        return description;
    }

    /**
     * Call the given {@code editListener} when
     * the item is clicked.
     * @param editListener Listener to register.
     */
    public void setOnEditListener(Runnable editListener) {
        this.editListener = editListener;
    }

    /**
     * Allow the item to be deleted and call the given {@code deleteListener} when
     * the item is deleted.
     * @param deleteListener Listener to register.
     */
    public void setOnDeleteListener(Runnable deleteListener) {
        this.deleteListener = deleteListener;
    }

    /**
     * Allow the item to be renamed and call the given {@code renameListener} when
     * the item is renamed.
     * @param renameListener Listener to register.
     */
    public void setOnRenameListener(Consumer<String> renameListener) {
        this.renameListener = renameListener;
    }


    /**
     * Creates new instance of {@link ConfigValueListItem} object.
     *
     * Note:
     *  This method will resolve the reference of the given {@code value} if its
     *  a reference.
     *
     * @param context Current activity context.
     * @param name Name of the item.
     * @param value Value to edit when the item is clicked.
     * @param annotations List of annotations associated to the element.
     * @return new {@link ConfigValueListItem} object
     */
    public static ConfigValueListItem create(Context context, String name, Value value, List<Annotation> annotations) {
        EditorContext editorContext = (EditorContext) context;
        value = editorContext.resolveReference(value);

        Optional<Description> desc =  annotations.stream()
                .filter(e -> e instanceof Description)
                .map(e -> (Description) e)
                .findFirst();

        return new ConfigValueListItem(context, name, value, desc.map(Description::description).orElse(null));
    }

    /**
     * Creates new instance of {@link ConfigValueListItem} object.
     *
     * Note:
     *  This method will resolve the reference of the given {@code value} if its
     *  a reference.
     *
     * @param context Current activity context.
     * @param name Name of the item.
     * @param value Value to edit when the item is clicked.
     * @return new {@link ConfigValueListItem} object
     */
    public static ConfigValueListItem create(Context context, String name, Value value) {
        return create(context, name, value, new ArrayList<>());
    }
}
