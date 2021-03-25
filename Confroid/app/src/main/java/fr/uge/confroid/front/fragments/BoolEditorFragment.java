package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.BooleanValue;
import fr.uge.confroid.configuration.Value;

public class BoolEditorFragment extends EditorFragment implements CompoundButton.OnCheckedChangeListener {
    private SwitchCompat toggle;
    public static BoolEditorFragment newInstance(Value value) {
        if (value.isBoolean()) {
            return new BoolEditorFragment();
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bool_editor, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggle = view.findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(this);
    }

    @Override
    void onUpdateValue(Value value) {
        toggle.setChecked(value.getBoolean());
        toggle.setText(value.getBoolean() ? "true" : "false");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        toggle.setText(isChecked ? "true" : "false");
        update(new BooleanValue(isChecked));
    }
}
