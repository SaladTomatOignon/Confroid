package fr.uge.confroid.web.service.confroidstorageservice.controllers;

import fr.uge.confroid.web.service.confroidstorageservice.exceptions.AuthenticationFailedException;
import fr.uge.confroid.web.service.confroidstorageservice.exceptions.ConfigurationNotFoundException;
import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import fr.uge.confroid.web.service.confroidstorageservice.models.User;
import fr.uge.confroid.web.service.confroidstorageservice.services.ConfigService;
import fr.uge.confroid.web.service.confroidstorageservice.services.UserService;
import fr.uge.confroid.web.service.confroidstorageservice.utils.CryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ConfigController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private UserService userService;

    @PostMapping("/getConfig")
    public Configuration getConfig(@RequestParam(name = "user", required = true) User user,
                                   @RequestParam(name = "name", required = true) String name,
                                   @RequestParam(name = "version", required = true) String version) {

        var password = CryptUtils.hash(user.getPassword() + CryptUtils.hash(user.getUsername()));
        if (!userService.authenticate(user.getUsername(), password)) {
            throw new AuthenticationFailedException();
        }

        var config = configService.findByNameAndVersion(name, version);
        if (Objects.isNull(config)) {
            throw new ConfigurationNotFoundException(name, version);
        }

        return config;
    }

    @PostMapping("/saveConfig")
    public void saveConfig(@RequestParam(name = "user", required = true) User user,
                           @RequestParam(name = "config", required = true) Configuration config) {

        var password = CryptUtils.hash(user.getPassword() + CryptUtils.hash(user.getUsername()));
        if (!userService.authenticate(user.getUsername(), password)) {
            throw new AuthenticationFailedException();
        }

        configService.save(config);
    }
}
