package fr.uge.confroid.web;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Integer;

@RunWith(AndroidJUnit4.class)
public class WebClientTest {

    private static final Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void sendConfigTest() {
        Client client = new Client("Samy", "Batman94", appContext);

        client.sendConfig(new Configuration(new Integer(5)), "fr.uge.confroid.test", "1.0.0");
    }

    @Test
    public void getConfigTest() {
        Client client = new Client("Samy", "Batman94", appContext);

        Configuration config = client.getConfig("fr.uge.confroid.test", "1.0.0");
    }
}
