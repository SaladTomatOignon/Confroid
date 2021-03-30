package fr.uge.confroiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;
import java.util.Map;

import fr.uge.confroidlib.ConfroidUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating some objects ... (including primitives types)
        ShoppingPreferences prefs = DemoUtils.createShoppingPreferences();
        Map<String, Integer> map = DemoUtils.createIntegerMap();
        Integer[] intArray = DemoUtils.createIntegerArray();
        List<Integer> lst = DemoUtils.createIntegerList();
        int primitive = DemoUtils.createIntegerPrimitive();

        // Send these objects to Confroid storage
        findViewById(R.id.store).setOnClickListener(view -> {
            ConfroidUtils.saveConfiguration(this, "shoppingPreferences", prefs, "stable");
            ConfroidUtils.saveConfiguration(this, "hashmap", map, null);
            ConfroidUtils.saveConfiguration(this, "array", intArray, null);
            ConfroidUtils.saveConfiguration(this, "list", lst, null);
            ConfroidUtils.saveConfiguration(this, "primitive", primitive, null);
        });

        // Launch Confroid editing activity for the ShoppingPreferences object
        findViewById(R.id.edit).setOnClickListener(view -> {
            ConfroidUtils.editObject(this, prefs, (editedPrefs) -> { /* Do what you want */ });
        });
    }
}