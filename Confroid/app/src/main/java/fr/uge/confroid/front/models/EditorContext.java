package fr.uge.confroid.front.models;


import fr.uge.confroid.configuration.Value;

/**
 *  Provides access to Confroid editor activity.
 */
public interface EditorContext {
    /**
     * Called when the value of the current visible editor change.
     */
    void onChange(Value newValue);

    /**
     * Push new editor to the activity history.
     * @param args Arguments to pass to the editor.
     */
    void pushEditor(EditorArgs args);

    /**
     * Gets the arguments of the current visible editor.
     */
    EditorArgs currentEditorArgs();

    /**
     * A value can be a reference to an another value in confroid,
     * see {@link fr.uge.confroidlib.BundleUtils#REF_KEYWORD}.
     * Calling this method will return original reference value
     * according to the root value of the editor if the given one is a reference.
     * @param value The value to resolve.
     * @return The reference value if the given one is a reference otherwise the value.
     */
    Value resolveReference(Value value);
}
