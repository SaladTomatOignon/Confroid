package fr.uge.confroid.web.service.confroidstorageservice.models;

import java.util.Objects;

public class RequestContext {
    private User user;
    private Configuration configuration;

    public RequestContext() {

    }

    public RequestContext(User user, Configuration configuration) {
        this.user = user;
        this.configuration = configuration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
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
