package fr.uge.confroid.web;

import java.util.Objects;

import javax.crypto.SecretKey;

import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.utils.CryptUtils;

public class Client {
    private static final String SALT = "Confroid";

    private final UserInfos userInfos;
    private final SecretKey key;

    public Client(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        this.userInfos = new UserInfos(username, CryptUtils.hash(CryptUtils.hash(password)));
        this.key = CryptUtils.getKeyFromPassword(CryptUtils.hash(password), SALT);
    }

    public void sendConfig(Configuration config, String name, String version) {
        String encryptedConfig = CryptUtils.encrypt(config.toJson(), key);
        ConfigurationContext configCtx = new ConfigurationContext(name, version, encryptedConfig);
        RequestContext request = new RequestContext(userInfos, configCtx);

        // TODO : Faire la requete HTTP au service web
    }

    public Configuration getConfig(String name, String version) {
        RequestContext request = new RequestContext(userInfos, new ConfigurationContext(name, version, null));

        ConfigurationContext configCtx = null; // TODO : Faire la requete HTTP au service web

        String decryptedConfig = CryptUtils.decrypt(configCtx.getConfig(), key);
        return Configuration.fromJson(decryptedConfig);
    }
}
