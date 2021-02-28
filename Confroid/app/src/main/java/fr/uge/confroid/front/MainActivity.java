package fr.uge.confroid.front;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import fr.uge.confroid.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_explore).setOnClickListener(view -> {
           startActivity(new Intent(this, ConfigNamesActivity.class));
        });
    }
}