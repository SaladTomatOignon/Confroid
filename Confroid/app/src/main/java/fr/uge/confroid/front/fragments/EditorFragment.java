package fr.uge.confroid.front.fragments;

import android.content.Context;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.models.EditorContext;
import fr.uge.confroid.front.models.EditorArgs;

/**
 * Base fragment that handle logic shared by all editors.
 */
abstract class EditorFragment extends Fragment {
    private EditorContext editorContext;

    @Override
    public void onAttach(@NotNull Context context) {
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
        EditorArgs page = editorContext.currentEditorArgs();
        this.onUpdateArgs(page);
    }

    /**
     * Push new editor to the activity history.
     * @param name Name of the editor.
     * @param value Value of the editor.
     */
    public void pushEditor(String name, Value value) {
        editorContext.pushEditor(EditorArgs.create(editorContext, name, value));
    }

    /**
     * Push new editor to the activity history.
     * @param name Name of the editor.
     * @param value Value of the editor.
     * @param annotations Annotations associated to the value to to edit.
     */
    public void pushEditor(String name, Value value, List<Annotation> annotations) {
        editorContext.pushEditor(EditorArgs.create(editorContext, name, value, annotations));
    }

    /**
     * Updates the {@link Value} associated the current editor.
     * @param newValue New value.
     */
    public void updateValue(Value newValue, boolean refresh) {
        editorContext.onChange(newValue);
        if (refresh) {
            onUpdateArgs(editorContext.currentEditorArgs());
        }
    }

    /**
     * Lifecycle hook called once the editor data is initialized and after
     * each time {@link EditorFragment#updateValue(Value, boolean)} method is called with
     * {@code refresh} arg set to {@code true}.
     * @param args Arguments of the editor.
     */
    abstract void onUpdateArgs(EditorArgs args);
}
