package fr.uge.confroid.front.fragments;

import android.content.Context;
import android.os.Bundle;

import java.lang.annotation.Annotation;
import java.util.List;

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
        EditorPage page = editor.peekPage();
        this.onUpdatePage(page);
    }

    /**
     * Push the given {@code page} to the editor history.
     * @param name Name of the page to push.
     * @param value Value to edit in the page.
     */
    public void push(String name, Value value) {
        editor.pushPage(EditorPage.create(editor, name, value));
    }

    /**
     * Push the given {@code page} to the editor history.
     * @param name Name of the page to push.
     * @param value Value to edit in the page.
     * @param annotations Annotations associated to the value to to edit.
     */
    public void push(String name, Value value, List<Annotation> annotations) {
        editor.pushPage(EditorPage.create(editor, name, value, annotations));
    }

    /**
     * Updates the {@link Value} associated the current page.
     * @param newValue New value.
     */
    public void update(Value newValue) {
        editor.onChange(newValue);
    }

    /**
     * Updates the {@link Value} associated the current page and recall {@link EditorFragment#onUpdatePage}.
     * @param newValue New value.
     */
    public void updateAndRefresh(Value newValue) {
        editor.onChange(newValue);
        onUpdatePage(editor.peekPage());
    }

    /**
     * Lifecycle hook called once the page data is initialized and after
     * each time {@link EditorFragment#updateAndRefresh(Value)} is method is called.
     * @param page The current page to edit.
     */
    abstract void onUpdatePage(EditorPage page);
}
