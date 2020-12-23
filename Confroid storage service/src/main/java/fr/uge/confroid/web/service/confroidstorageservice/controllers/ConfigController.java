package fr.uge.confroid.web.service.confroidstorageservice.controllers;

import fr.uge.confroid.web.service.confroidstorageservice.exceptions.AuthenticationFailedException;
import fr.uge.confroid.web.service.confroidstorageservice.exceptions.ConfigurationNotFoundException;
import fr.uge.confroid.web.service.confroidstorageservice.exceptions.InvalidParameterException;
import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import fr.uge.confroid.web.service.confroidstorageservice.models.RequestContext;
import fr.uge.confroid.web.service.confroidstorageservice.services.ConfigService;
import fr.uge.confroid.web.service.confroidstorageservice.services.UserService;
import fr.uge.confroid.web.service.confroidstorageservice.utils.CryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ConfigController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private UserService userService;

    @PostMapping("/getConfig")
    public Configuration getConfig(@RequestBody RequestContext ctx) {
        var invalidParameter = getInvalidParameter(ctx, false);
        if (!Objects.isNull(invalidParameter)) {
            throw new InvalidParameterException(invalidParameter);
        }

        var user = ctx.getUser();
        var name = ctx.getConfiguration().getName();
        var version = ctx.getConfiguration().getVersion();

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
    public void saveConfig(@RequestBody RequestContext ctx) {
        var invalidParameter = getInvalidParameter(ctx, true);
        if (!Objects.isNull(invalidParameter)) {
            throw new InvalidParameterException(invalidParameter);
        }

        var user = ctx.getUser();
        var config = ctx.getConfiguration();

        var password = CryptUtils.hash(user.getPassword() + CryptUtils.hash(user.getUsername()));
        if (!userService.authenticate(user.getUsername(), password)) {
            throw new AuthenticationFailedException();
        }

        configService.save(config);
    }

    private String getInvalidParameter(RequestContext ctx, boolean configContentRequired) {
        String parameterName = null;

        try {
            parameterName = "Context";
            Objects.requireNonNull(ctx);

            parameterName = "User";
            Objects.requireNonNull(ctx.getUser());
            parameterName = "Username";
            Objects.requireNonNull(ctx.getUser().getUsername());
            parameterName = "Password";
            Objects.requireNonNull(ctx.getUser().getPassword());

            parameterName = "Configuration";
            Objects.requireNonNull(ctx.getConfiguration());
            parameterName = "Configuration name";
            Objects.requireNonNull(ctx.getConfiguration().getName());
            parameterName = "Configuration version";
            Objects.requireNonNull(ctx.getConfiguration().getVersion());
            if (configContentRequired) {
                parameterName = "Configuration content";
                Objects.requireNonNull(ctx.getConfiguration().getConfig());
            }
        } catch (NullPointerException e) {
            return parameterName;
        }

        return null;
    }
}
