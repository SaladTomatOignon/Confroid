package fr.uge.confroid.web;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.IntegerValue;
import fr.uge.confroid.settings.AppSettings;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WebClientTest {

    private static final String configName = "fr.uge.confroid.test";

    private static Client client;

    @BeforeClass
    public static void initClient() {
        String login = "Samy";
        String password = "Batman94";
        Context ctx = getApplicationContext();
        AppSettings.loadSettings(ctx);
        Client.initClient(login, password, ctx, null);
        client = new Client(login, password, ctx);

        if (Objects.isNull(AppSettings.getINSTANCE().getBaseAddress()) ||
                AppSettings.getINSTANCE().getBaseAddress().isEmpty()) {
            throw new AssertionError("Base address is null or empty, can not make tests");
        }
    }

    @AfterClass
    public static void removeTestConfigsStatic() {
        client.deleteConfigs(configName, null, null);
    }

    @Before
    public void removeTestConfigs() {
        client.deleteConfigs(configName,
                response -> {
                    // Nothing
                }, error -> {
                    throw new AssertionError(error);
                });

        try {
            Thread.sleep(250); // Sleep a bit while we clear the previous values
        } catch (Exception e) {

        }
    }

    @Test
    public void sendConfigTest() {
        Configuration configToSend = new Configuration(new IntegerValue(5));

        client.sendConfig(configToSend,
                configName,
                1,
                LocalDateTime.now(),
                response -> {
                    Configuration receivedConfig = client.decryptConfig(response.getConfig());

                    assertEquals(configToSend, receivedConfig);
                    assertEquals(configName, response.getName());
                    assertEquals(1, response.getVersion());
                    assertNull(response.getTag());
                }, error -> {
                    throw new AssertionError(error);
                });
    }

    @Test
    public void getConfigByNameAndVersionTest() {
        Configuration configToSend = new Configuration(new IntegerValue(5));

        client.sendConfig(configToSend,
                configName, 1,
                LocalDateTime.now(),null, null);

        try {
            Thread.sleep(250); // Sleep a bit while we insert the previous value
        } catch (Exception e) {

        }

        client.getConfig(configName,
                1,
                response -> {
                    Configuration receivedConfig = client.decryptConfig(response.getConfig());

                    assertEquals(configToSend, receivedConfig);
                    assertEquals(configName, response.getName());
                    assertEquals(1, response.getVersion());
                    assertNull(response.getTag());
                }, error -> {
                    throw new AssertionError(error);
                });
    }

    @Test
    public void getAllConfigsByNameTest() {
        Configuration configToSend = new Configuration(new IntegerValue(5));

        client.sendConfig(configToSend,
                configName, 1,
                LocalDateTime.now(),null, null);

        client.sendConfig(configToSend,
                configName, 2,
                LocalDateTime.now(),null, null);

        try {
            Thread.sleep(250); // Sleep a bit while we insert the previous values
        } catch (Exception e) {

        }

        client.getConfigsByName(configName,
                response -> {
                    List<Configuration> receivedConfigs = response.stream().map(
                            cryptedConfig -> client.decryptConfig(cryptedConfig.getConfig())).collect(Collectors.toList());

                    assertEquals(configToSend, receivedConfigs.get(0));
                    assertEquals(configToSend, receivedConfigs.get(1));
                    assertEquals(configName, response.get(0).getName());
                    assertEquals(configName, response.get(1).getName());
                    assertNull(response.get(0).getTag());
                    assertNull(response.get(1).getTag());
                }, error -> {
                    throw new AssertionError(error);
                });
    }
}
