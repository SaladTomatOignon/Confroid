package fr.uge.confroid.front.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.ArrayValue;
import fr.uge.confroid.configuration.FloatValue;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.models.EditorArgs;
import fr.uge.confroid.front.models.EditorOpener;
import fr.uge.confroidlib.annotations.GeoCoordinates;

public class GeoCoordinatesEditorFragment extends EditorFragment {
    public static class Opener implements EditorOpener {
        @Override
        public boolean canHandle(EditorArgs args) {
            Value value = args.getValue();
            return value.isArray() && args.getAnnotation(GeoCoordinates.class).isPresent();
        }

        @Override
        public Fragment createEditor() {
            return new GeoCoordinatesEditorFragment();
        }
    }

    private static final int LOCATION_PERMISSION_CODE = 10;
    private static final String TAG = "GeoCoordinatesEditorFragment";

    private EditText inputLatitude;
    private EditText inputLongitude;
    private List<Value> editableEntries;
    private List<Value> unEditableEntries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_geocoordinates_editor, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputLatitude = view.findViewById(R.id.input_latitude);
        inputLatitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float latitude = Float.parseFloat(s.toString());
                    editableEntries.set(0, new FloatValue(latitude));

                    ArrayList<Value> coordinates = new ArrayList<>();
                    coordinates.addAll(editableEntries);
                    coordinates.addAll(unEditableEntries);

                    updateValue(new ArrayValue(coordinates.toArray(new Value[0])), false);
                } catch(NumberFormatException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });

        inputLongitude = view.findViewById(R.id.input_longitude);
        inputLongitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float longitude = Float.parseFloat(s.toString());
                    editableEntries.set(1, new FloatValue(longitude));

                    ArrayList<Value> coordinates = new ArrayList<>();
                    coordinates.addAll(editableEntries);
                    coordinates.addAll(unEditableEntries);

                    updateValue(new ArrayValue(coordinates.toArray(new Value[0])), false);
                } catch(NumberFormatException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });

        Button button = view.findViewById(R.id.btn_pick_location);
        button.setOnClickListener(__ -> ensureLocationPermissionThenReadLocation());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ensureLocationPermissionThenReadLocation();
            } else {
                Toast.makeText(getContext(), R.string.error_location_permission, Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onUpdateArgs(EditorArgs args) {
        ArrayValue array = (ArrayValue) args.getValue();
        editableEntries = array.editableEntries();
        unEditableEntries = array.unEditableEntries();

        Value[] value = args.getValue().getArray();
        if (editableEntries.size() == 0) {
            inputLatitude.setText("0");
            inputLongitude.setText("0");
            editableEntries.add(new FloatValue(0f));
            editableEntries.add(new FloatValue(0f));
        } else if (value.length == 1) {
            inputLatitude.setText(String.valueOf(editableEntries.get(0).getFloat()));
            editableEntries.add(new FloatValue(0f));
        } else {
            inputLatitude.setText(String.valueOf(editableEntries.get(0).getFloat()));
            inputLongitude.setText(String.valueOf(editableEntries.get(1).getFloat()));
        }
    }


    private void ensureLocationPermissionThenReadLocation() {
        Context context = getContext();
        // Check whether the permission is already granted or not.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_PERMISSION_CODE);
            // wait for onRequestPermissionsResult()
            return;
        }

        // The permission is already granted.
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, location -> {
            inputLatitude.setText(String.valueOf(location.getLatitude()));
            inputLongitude.setText(String.valueOf(location.getLongitude()));
        });
    }
}
