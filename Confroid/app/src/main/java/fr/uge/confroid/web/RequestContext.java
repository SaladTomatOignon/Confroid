package fr.uge.confroid.web;

import java.util.Objects;

public class RequestContext {
    private UserInfos user;
    private ConfigurationContext configuration;

    public RequestContext() {

    }

    public RequestContext(UserInfos user, ConfigurationContext configuration) {
        this.user = user;
        this.configuration = configuration;
    }

    public UserInfos getUser() {
        return user;
    }

    public void setUser(UserInfos user) {
        this.user = user;
    }

    public ConfigurationContext getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ConfigurationContext configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestContext that = (RequestContext) o;
        return user.equals(that.user) &&
                configuration.equals(that.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, configuration);
    }
}
