package fr.uge.confroid.front.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.models.Editor;
import fr.uge.confroid.front.models.EditorSession;

abstract class EditorFragment extends Fragment {
    private Editor editor;
    private EditorSession session;

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
        session = editor.pop();
        this.onReady(session.getValue());
    }

    public void push(EditorSession session) {
        editor.push(session);
    }

    public void emitChange(Value newValue) {
        session.setValue(newValue);
        editor.save();
    }

    abstract void onReady(Value value);
}
