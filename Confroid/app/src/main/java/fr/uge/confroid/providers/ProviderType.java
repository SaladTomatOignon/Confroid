package fr.uge.confroid.providers;

public enum ProviderType {
    DATABASE(DatabaseProvider.getInstance()),
    CLOUD(WebserviceProvider.getInstance()),
    FILES(FilesProvider.getInstance());

    private final ConfigProvider provider;

    ProviderType(ConfigProvider provider) {
        this.provider = provider;
    }

    /**
     * @return The configurations provider associated to this Enum.
     */
    public ConfigProvider getProvider() {
        return provider;
    }
}
