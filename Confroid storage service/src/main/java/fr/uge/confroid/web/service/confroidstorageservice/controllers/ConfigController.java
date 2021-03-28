package fr.uge.confroid.web.service.confroidstorageservice.controllers;

import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import fr.uge.confroid.web.service.confroidstorageservice.services.ConfigService;
import fr.uge.confroid.web.service.confroidstorageservice.services.UserService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/configurations/")
@Validated
public class ConfigController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<Configuration>> getAllConfigs() {
        return ResponseEntity.ok(configService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<Configuration>> getConfigsByName(@PathVariable String name) {
        return ResponseEntity.ok(configService.findByName(name));
    }

    @GetMapping("/{name}/{version}")
    public ResponseEntity<Configuration> getConfigByNameAndVersion(@PathVariable String name, @PathVariable int version) {
        return ResponseEntity.of(configService.findByNameAndVersion(name, version));
    }

    @GetMapping("/{name}/tags/{tag}")
    public ResponseEntity<Configuration> getConfigByNameAndVersion(@PathVariable String name, @PathVariable String tag) {
        return ResponseEntity.of(configService.findByNameAndTag(name, tag));
    }

    @PutMapping()
    public ResponseEntity<Configuration> saveConfig(@Valid @RequestBody Configuration config) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(configService.saveOrUpdate(config));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteConfig(@PathVariable String name) {
        configService.deleteByName(name);

        return ResponseEntity.ok().build();
    }
}
