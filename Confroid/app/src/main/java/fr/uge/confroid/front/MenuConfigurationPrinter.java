package fr.uge.confroid.front;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import fr.uge.confroid.R;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Integer;
import fr.uge.confroid.storage.ConfroidPackage;

public class MenuConfigurationPrinter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_configuration_printer);
        ArrayList list = new ArrayList<String>();
        list.add(new ConfroidPackage("p", 0, new Date(), new Configuration(new Integer(0))).toString());
        list.add(new ConfroidPackage("p1", 1, new Date(), new Configuration(new Integer(2))).toString());
        list.add(new ConfroidPackage("p2", 3, new Date(), new Configuration(new Integer(3))).toString());
        list.add(new ConfroidPackage("p3", 4, new Date(), new Configuration(new Integer(4))).toString());


        String[] conf = new String[list.size()];
        list.toArray(conf);

        ListView lv = findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , conf);
        lv.setAdapter(arrayAdapter);
        //TextView tv = (TextView) findViewById(R.id.textView);
        //tv.setText(p.toString());
    }
}