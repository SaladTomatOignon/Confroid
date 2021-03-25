package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.ByteValue;
import fr.uge.confroid.configuration.DoubleValue;
import fr.uge.confroid.configuration.FloatValue;
import fr.uge.confroid.configuration.IntegerValue;
import fr.uge.confroid.configuration.LongValue;
import fr.uge.confroid.configuration.StringValue;
import fr.uge.confroid.configuration.Value;


public class TextEditorFragment extends EditorFragment implements TextWatcher {
    private static final java.lang.String TAG = "TextEditorFragment";

    private EditText input;
    private Value value;

    public static TextEditorFragment newInstance(Value value) {
        if (value.isPrimitive() && ! value.isBoolean()) {
            return new TextEditorFragment();
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_editor, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        input = view.findViewById(R.id.input);
        input.addTextChangedListener(this);
    }

    @Override
    public void onUpdateValue(Value value) {
        this.value = value;

        switch (value.valueType()) {
            case STRING:
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case BYTE:
            case LONG:
            case INTEGER:
                input.setInputType(
                    InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_SIGNED
                );
                break;
            case FLOAT:
            case DOUBLE:
                input.setInputType(
                    InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED
                );
                break;
        }

        input.setText(value.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            switch (value.valueType()) {
                case STRING:
                    update(new StringValue(s.toString()));
                    break;
                case BYTE:
                    update(new ByteValue(
                        Byte.parseByte(s.toString())
                    ));
                case LONG:
                    update(new LongValue(
                        Long.parseLong(s.toString())
                    ));
                    break;
                case INTEGER:
                    update(new IntegerValue(
                        Integer.parseInt(s.toString())
                    ));
                    break;
                case FLOAT:
                    update(new FloatValue(
                        Float.parseFloat(s.toString())
                    ));
                    break;
                case DOUBLE:
                    update(new DoubleValue(
                        Double.parseDouble(s.toString())
                    ));
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
