package fr.uge.confroid.front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.uge.confroid.R;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Integer;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.storage.ConfroidDatabase;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroid.storage.ConfroidPackageDao;
import fr.uge.confroid.web.Client;

public class MenuConfiguarationPrinter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_configuaration_printer);

        //ConfroidPackageDao dao;
        //ConfroidDatabase database;

        //database = Room.inMemoryDatabaseBuilder(getApplicationContext(), ConfroidDatabase.class).build();
        //dao = database.packageDao();
        //dao.create(new ConfroidPackage("p0", 0, new Date(), new Configuration(new Integer(0)), "T"));


        List listConf = new ArrayList<String>();
        listConf.add(new ConfroidPackage("p0", 0, new Date(), new Configuration(new Integer(0)), "T").toString());
        listConf.add(new ConfroidPackage("p1", 0, new Date(), new Configuration(new Integer(0)), "T").toString());
        listConf.add(new ConfroidPackage("p2", 0, new Date(), new Configuration(new Integer(0)), "T").toString());
        listConf.add(new ConfroidPackage("p3", 0, new Date(), new Configuration(new Integer(0)), "T").toString());


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listConf);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(arrayAdapter);
    }
}