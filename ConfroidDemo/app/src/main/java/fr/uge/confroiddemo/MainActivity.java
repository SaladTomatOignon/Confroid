package fr.uge.confroiddemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;
import java.util.Map;

import fr.uge.confroidlib.ConfroidUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("Confroid application test");

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

        // Launch Confroid editing activity for the ShoppingPreferences object and edit it (in local)
        findViewById(R.id.edit).setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                Toast.makeText(this, "Maximum authorized SDK version : 28", Toast.LENGTH_SHORT).show();
            }
            try {
                ConfroidUtils.editObject(this, prefs, (editedPrefs) -> { /* Do what you want */ });
            } catch (ActivityNotFoundException anfe) {
                Toast.makeText(this, "You need to install Confroid first", Toast.LENGTH_SHORT).show();
            }
        });

        // Launch Confroid editing activity for the ShoppingPreferences object and edit (then update the database)
        findViewById(R.id.update).setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                Toast.makeText(this, "Maximum authorized SDK version : 28", Toast.LENGTH_SHORT).show();
            }
            try {
                ConfroidUtils.updateObject(this, "shoppingPreferences", "stable", (editedPrefs) -> { /* Do what you want */ });
            } catch (ActivityNotFoundException anfe) {
                Toast.makeText(this, "You need to install Confroid first", Toast.LENGTH_SHORT).show();
            }
        });
    }
}