package fr.uge.confroid.front.models;


import androidx.fragment.app.Fragment;

/**
 * Allow to register new editor to edit Value objects.
 */
public interface EditorOpener {
    /**
     * Gets a value indicating whether the editor associated to the opener
     * can handle the given args.
     * @param args Arguments
     * @return {@code} true if the opener can handle the args {@code false} otherwise.
     */
    boolean canHandle(EditorArgs args);

    /**
     * Creates a new instance of the editor associated to the opener.
     * @return new Fragment.
     */
    Fragment createEditor();
}
