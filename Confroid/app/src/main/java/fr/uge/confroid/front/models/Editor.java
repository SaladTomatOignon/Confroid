package fr.uge.confroid.front.models;


import fr.uge.confroid.configuration.Value;

/**
 *  Provides access to Confroid editor activity.
 */
public interface Editor {
    /**
     * Push new page to the editor history.
     * @param page The page to push.
     */
    void pushPage(EditorPage page);

    /**
     * Gets the top most page of the editor history.
     */
    EditorPage peekPage();

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
