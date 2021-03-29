package fr.uge.confroid.front;

import fr.uge.confroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import fr.uge.confroid.front.adapters.ConfigsBrowserAdapter;
import fr.uge.confroid.settings.AppSettings;
import fr.uge.confroid.storage.ConfroidStorage;

public class ConfigBrowserActivity extends AppCompatActivity {
    private ConfigsBrowserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_browser);

        ConfigsBrowserAdapter sectionsPagerAdapter = new ConfigsBrowserAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        this.adapter = sectionsPagerAdapter;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> ConfroidStorage.performFileSearch(this));

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConfroidStorage.OPEN_DOCUMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (!Objects.isNull(data)) {
                AppSettings.getINSTANCE().setConfigFilePath(this, data.getData());
                if (!Objects.isNull(adapter)) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppSettings.getINSTANCE().setConfigFilePath(this, null);
    }
}