package fr.uge.confroidlib;

public class ConfroidIntents {
    public static final String PACKAGE_NAME = "fr.uge.confroid";
    public static final String INTENT_CONFIG_EDITOR = "fr.uge.confroid.activity.ConfigEditorActivity";
    public static final String INTENT_TOKEN_DISPENSER = "fr.uge.confroid.receivers.TokenDispenser";
    public static final String INTENT_CONFIGURATION_PUSHER = "fr.uge.confroid.services.ConfigurationPusher";
    public static final String INTENT_CONFIGURATION_PULLER = "fr.uge.confroid.services.ConfigurationPuller";
    public static final String INTENT_CONFIGURATION_VERSIONS = "fr.uge.confroid.services.ConfigurationVersions";

    public static final String EXTRA_TAG = "tag";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_TOKEN = "token";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_VERSION = "version";
    public static final String EXTRA_VERSIONS = "versions";
    public static final String EXTRA_SUCCESS = "success";
    public static final String EXTRA_RECEIVER = "receiver";
    public static final String EXTRA_REQUEST_ID = "requestId";
    public static final String EXTRA_EXPIRATION = "expiration";
    public static final String EXTRA_DATE = "date";
}
