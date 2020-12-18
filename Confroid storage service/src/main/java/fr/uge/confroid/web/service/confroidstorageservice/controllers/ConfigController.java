package fr.uge.confroid.web.service.confroidstorageservice.controllers;

import fr.uge.confroid.web.service.confroidstorageservice.exceptions.ConfigurationNotFoundException;
import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import fr.uge.confroid.web.service.confroidstorageservice.services.ConfigService;
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

    @GetMapping("/getConfig")
    public Configuration getConfig(@RequestParam(name = "name", required = true) String name, @RequestParam(name = "version", required = true) String version) {
        var config = configService.findByNameAndVersion(name, version);

        if (Objects.isNull(config)) {
            throw new ConfigurationNotFoundException(name, version);
        }

        return config;
    }

    @PostMapping("/saveConfig")
    public void saveConfig(@RequestParam(name = "config", required = true) Configuration config) {
        configService.save(config);
    }
}
