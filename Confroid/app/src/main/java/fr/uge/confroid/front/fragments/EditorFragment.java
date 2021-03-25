package fr.uge.confroid.front.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.models.Editor;
import fr.uge.confroid.front.models.EditorPage;

/**
 * Base fragment that handle logic shared by all editors.
 */
abstract class EditorFragment extends Fragment {
    private Editor editor;
    private Value mapValue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Editor){
            this.editor = (Editor) context;
        }  else {
            throw new ClassCastException(context.toString() + " must implement ValueChangeListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditorPage session = editor.peekPage();
        mapValue = editor.resolveReference(session.getValue());
        this.onUpdateValue(mapValue);
    }

    /**
     * Push the given {@code page} to the editor history.
     * @param page Page to push.
     */
    public void push(EditorPage page) {
        editor.pushPage(page);
    }

    /**
     * Updates the {@link Value} associated the current page.
     * @param newValue New value.
     */
    public void update(Value newValue) {
        mapValue.setValue(newValue);
        onUpdateValue(mapValue);
    }

    /**
     * Lifecycle hook called once the page data is initialized and after
     * each time {@link EditorFragment#update(Value)} is method is called.
     * @param value The value associated to the page.
     */
    abstract void onUpdateValue(Value value);
}
