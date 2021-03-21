package fr.uge.confroid.front;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;
import fr.uge.confroid.R;
import fr.uge.confroidlib.ConfroidUtils;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Demo.create(this);

        findViewById(R.id.btn_explore).setOnClickListener(view -> {
           startActivity(new Intent(this, ConfigNamesActivity.class));
        });
    }
}