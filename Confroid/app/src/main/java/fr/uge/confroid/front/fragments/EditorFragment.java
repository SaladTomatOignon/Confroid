package fr.uge.confroid.front.fragments;

import android.content.Context;
import android.os.Bundle;

import java.lang.annotation.Annotation;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.models.EditorContext;
import fr.uge.confroid.front.models.EditorPage;

/**
 * Base fragment that handle logic shared by all editors.
 */
abstract class EditorFragment extends Fragment {
    private EditorContext editorContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditorContext){
            this.editorContext = (EditorContext) context;
        }  else {
            throw new ClassCastException(context.toString() + " must implement ValueChangeListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditorPage page = editorContext.peekPage();
        this.onUpdatePage(page);
    }

    /**
     * Push the given {@code page} to the editor history.
     * @param name Name of the page to push.
     * @param value Value to edit in the page.
     */
    public void push(String name, Value value) {
        editorContext.pushPage(EditorPage.create(editorContext, name, value));
    }

    /**
     * Push the given {@code page} to the editor history.
     * @param name Name of the page to push.
     * @param value Value to edit in the page.
     * @param annotations Annotations associated to the value to to edit.
     */
    public void push(String name, Value value, List<Annotation> annotations) {
        editorContext.pushPage(EditorPage.create(editorContext, name, value, annotations));
    }

    /**
     * Updates the {@link Value} associated the current page.
     * @param newValue New value.
     */
    public void update(Value newValue) {
        editorContext.onChange(newValue);
    }

    /**
     * Updates the {@link Value} associated the current page and recall {@link EditorFragment#onUpdatePage}.
     * @param newValue New value.
     */
    public void updateAndRefresh(Value newValue) {
        editorContext.onChange(newValue);
        onUpdatePage(editorContext.peekPage());
    }

    /**
     * Lifecycle hook called once the page data is initialized and after
     * each time {@link EditorFragment#updateAndRefresh(Value)} is method is called.
     * @param page The current page to edit.
     */
    abstract void onUpdatePage(EditorPage page);
}
