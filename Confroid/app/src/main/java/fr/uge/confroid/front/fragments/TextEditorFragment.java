package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.ByteValue;
import fr.uge.confroid.configuration.DoubleValue;
import fr.uge.confroid.configuration.FloatValue;
import fr.uge.confroid.configuration.IntegerValue;
import fr.uge.confroid.configuration.LongValue;
import fr.uge.confroid.configuration.StringValue;
import fr.uge.confroid.front.models.EditorArgs;
import fr.uge.confroid.front.models.EditorOpener;
import fr.uge.confroidlib.annotations.ClassValidator;
import fr.uge.confroidlib.annotations.RangeValidator;
import fr.uge.confroidlib.annotations.RegexValidator;


public class TextEditorFragment extends EditorFragment implements TextWatcher {
    public static class Opener implements EditorOpener {
        @Override
        public boolean canHandle(EditorArgs args) {
            return args.getValue().isPrimitive() && !args.getValue().isBoolean();
        }

        @Override
        public Fragment createEditor() {
            return new TextEditorFragment();
        }
    }


    private static final java.lang.String TAG = "TextEditorFragment";
    private final ArrayList<Function<String, String>> validators = new ArrayList<>();
    private EditorArgs page;
    private TextInputLayout inputLayout;
    private TextInputEditText input;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validators.add(this::applyRegexValidator);
        validators.add(this::applyRangeValidator);
        validators.add(this::applyClassValidator);
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

        inputLayout = view.findViewById(R.id.input_layout);
    }

    @Override
    public void onUpdateArgs(EditorArgs args) {
        this.page = args;
        switch (args.getValue().valueType()) {
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

        input.setText(args.getValue().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        inputLayout.setErrorEnabled(false);
        inputLayout.setError("");

        try {
            String value = s.toString();
            for (Function<String, String> validator : validators) {
                value = validator.apply(value);
                if (value == null) {
                    return;
                }
            }

            switch (page.getValue().valueType()) {
                case STRING:
                    updateValue(new StringValue(value), false);
                    break;
                case BYTE:
                    updateValue(new ByteValue(Byte.parseByte(value)), false);
                case LONG:
                    updateValue(new LongValue(Long.parseLong(value)), false);
                    break;
                case INTEGER:
                    updateValue(new IntegerValue(Integer.parseInt(value)), false);
                    break;
                case FLOAT:
                    updateValue(new FloatValue(Float.parseFloat(value)), false);
                    break;
                case DOUBLE:
                    updateValue(new DoubleValue(Double.parseDouble(value)), false);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private String applyClassValidator(String s) {
        Optional<ClassValidator> validator = page.getAnnotation(ClassValidator.class);
        if (validator.isPresent()) {
            try {
                Class<?> clazz = validator.get().predicateClass();
                Predicate<String> predicate = (Predicate) clazz.newInstance();
                if (!predicate.test(s)) {
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError(getContext().getString(
                        R.string.error_class_validation, clazz.getName()
                    ));
                    return null;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    private String applyRangeValidator(String s) {
        Optional<RangeValidator> validator = page.getAnnotation(RangeValidator.class);

        if (validator.isPresent()) {
            long value = Long.parseLong(s);
            long min = validator.get().minRange();
            long max = validator.get().maxRange();
            if (value < min) {
                inputLayout.setErrorEnabled(true);
                inputLayout.setError(getContext().getString(
                    R.string.error_range_validation, min + "", max + ""
                ));
                return null;
            } else if (value > max) {
                inputLayout.setErrorEnabled(true);
                inputLayout.setError(getContext().getString(
                    R.string.error_range_validation, min + "", max + ""
                ));
                return null;
            }
        }

        return s;
    }

    private String applyRegexValidator(String s) {
        Optional<RegexValidator> validator = page.getAnnotation(RegexValidator.class);

        if (validator.isPresent()) {
            String pattern = validator.get().pattern();
            if (!s.matches(pattern)) {
                inputLayout.setErrorEnabled(true);
                inputLayout.setError(getContext().getString(
                    R.string.error_pattern_validation, pattern
                ));
                return null;
            }
        }

        return s;
    }

}
